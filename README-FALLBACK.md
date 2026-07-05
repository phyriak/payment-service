# Resilience i ochrona aplikacji przed przeciążeniem

## Cel

Celem konfiguracji było zwiększenie odporności aplikacji na:

* duży ruch użytkowników,
* wyczerpanie puli połączeń do bazy danych,
* awarie bazy danych,
* długie timeouty.

Do realizacji wykorzystano mechanizmy dostępne w Spring Boot, HikariCP oraz Resilience4j.

---

# Tomcat Thread Pool

Tomcat odpowiada za obsługę przychodzących żądań HTTP.

```yaml
server:
  tomcat:
    threads:
      max: 20
      min-spare: 5

    accept-count: 100
    max-connections: 200
```

## Parametry

### threads.max

Maksymalna liczba jednocześnie wykonywanych żądań HTTP.

Po osiągnięciu limitu nowe żądania trafiają do kolejki (`accept-count`).

### accept-count

Liczba oczekujących żądań.

Po jej przekroczeniu Tomcat zaczyna odrzucać nowe połączenia, co może skutkować:

* `SocketException`
* `NoHttpResponseException`
* timeoutami po stronie klienta.

### max-connections

Maksymalna liczba otwartych połączeń TCP.

Nie oznacza liczby jednocześnie wykonywanych requestów — część połączeń może pozostawać w stanie Keep-Alive.

---

# HikariCP

HikariCP zarządza pulą połączeń z bazą danych.

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      connection-timeout: 3000
      idle-timeout: 600000
      max-lifetime: 1800000
```

## Parametry

### maximum-pool-size

Maksymalna liczba aktywnych połączeń do bazy.

Jeżeli wszystkie połączenia są zajęte:

* kolejne żądania czekają,
* maksymalnie przez `connection-timeout`.

### connection-timeout

Maksymalny czas oczekiwania na wolne połączenie.

Po jego przekroczeniu rzucany jest:

```
SQLTransientConnectionException
```

Krótki timeout pozwala szybciej zwolnić wątki aplikacji.

---

# Bulkhead

Bulkhead chroni ograniczone zasoby aplikacji przed przeciążeniem.

W projekcie został zastosowany dla operacji korzystających z bazy danych.

```java
@Bulkhead(
    name = "database",
    type = Bulkhead.Type.SEMAPHORE,
    fallbackMethod = "bulkheadFallback"
)
```

Konfiguracja:

```yaml
resilience4j:
  bulkhead:
    instances:
      database:
        maxConcurrentCalls: 10
        maxWaitDuration: 0ms
```

## Działanie

Bulkhead ogranicza liczbę jednoczesnych wywołań metody.

Jeżeli limit zostanie przekroczony:

* nie następuje oczekiwanie,
* natychmiast rzucany jest `BulkheadFullException`,
* wykonywany jest fallback.

Dzięki temu użytkownik otrzymuje odpowiedź w ciągu kilku milisekund zamiast oczekiwać na timeout.

---

# Circuit Breaker

Circuit Breaker chroni aplikację przed ciągłym wykonywaniem operacji na niedostępnej bazie danych.

```java
@CircuitBreaker(
    name = "database",
    fallbackMethod = "circuitBreakerFallback"
)
```

Konfiguracja:

```yaml
resilience4j:
  circuitbreaker:
    instances:
      database:
        slidingWindowSize: 20
        minimumNumberOfCalls: 10
        failureRateThreshold: 50
        waitDurationInOpenState: 10s
```

## Działanie

Circuit Breaker monitoruje liczbę błędów.

Po przekroczeniu progu:

* przechodzi do stanu OPEN,
* kolejne żądania nie wykonują zapytań do bazy,
* od razu wykonywany jest fallback.

Po upływie `waitDurationInOpenState` przechodzi do stanu HALF_OPEN i ponownie testuje dostępność bazy.

---

# Fallback

Każdy mechanizm posiada własny fallback.

## Bulkhead

Odpowiada za przeciążenie aplikacji.

```java
public Payment bulkheadFallback(Long id, BulkheadFullException ex) {
    throw new ServiceOverloadedException(
            "Payment service is temporarily overloaded",
            ex
    );
}
```

## Circuit Breaker

Odpowiada za niedostępność bazy.

```java
public Payment circuitBreakerFallback(Long id, Throwable ex) {
    throw new DatabaseUnavailableException(
            "Database is temporarily unavailable",
            ex
    );
}
```

Rozdzielenie fallbacków pozwala odróżnić przeciążenie aplikacji od awarii bazy danych.

---

# Global Exception Handler

Wyjątki są mapowane na odpowiedzi HTTP.

```java
@ExceptionHandler(ServiceOverloadedException.class)
```

zwraca

```
HTTP 503 Service Unavailable
```

oraz

```java
@ExceptionHandler(DatabaseUnavailableException.class)
```

również zwraca

```
HTTP 503 Service Unavailable
```

Dzięki temu klient otrzymuje czytelny komunikat zamiast błędu technicznego lub timeoutu.

---

# Współpraca mechanizmów

```
HTTP Request
      │
      ▼
Tomcat
      │
      ▼
Controller
      │
      ▼
Service
      │
      ├── Bulkhead
      │       │
      │       ├── limit nieprzekroczony → wykonanie zapytania
      │       └── limit przekroczony → fallback
      │
      └── Circuit Breaker
              │
              ├── baza działa → wykonanie zapytania
              └── baza niedostępna → fallback
```

---

# Ograniczenia rozwiązania

Największym ograniczeniem jest pula wątków Tomcata.

Bulkhead działa dopiero po wejściu do metody serwisowej.

Oznacza to, że:

```
Client
   │
   ▼
Tomcat
   │
   ├── brak wolnych wątków
   │
   └── request nie trafia do Springa
```

W takim przypadku:

* fallback nie zostanie wykonany,
* klient może otrzymać timeout,
* mogą pojawić się `SocketException` lub `NoHttpResponseException`.

Nie jest to ograniczenie Resilience4j, lecz serwera HTTP.

---

# Jak zabezpieczyć wyczerpaną pulę Tomcata?

Na poziomie aplikacji możliwości są ograniczone.

Można:

* zwiększyć liczbę wątków Tomcata,
* dobrać odpowiedni rozmiar puli HikariCP,
* skrócić timeout połączeń do bazy,
* stosować Bulkhead oraz Circuit Breaker,
* zwracać HTTP 503 zamiast oczekiwania na timeout.

Natomiast nie da się ochronić aplikacji przed przeciążeniem Tomcata za pomocą samego Resilience4j, ponieważ żądania są odrzucane jeszcze przed wejściem do kodu aplikacji.

W środowisku produkcyjnym ochronę przed przeciążeniem realizuje się najczęściej na poziomie infrastruktury:

* API Gateway,
* NGINX,
* HAProxy,
* Kubernetes Ingress,
* Load Balancer.

To właśnie te komponenty mogą ograniczać liczbę połączeń, kolejkować ruch lub zwracać HTTP 429/503 jeszcze przed przekazaniem żądania do Tomcata.

---

# Podsumowanie

Zastosowane mechanizmy zwiększają odporność aplikacji na awarie oraz przeciążenie zasobów wewnętrznych:

* **Tomcat Thread Pool** odpowiada za obsługę żądań HTTP.
* **HikariCP** zarządza połączeniami z bazą danych.
* **Bulkhead** chroni pulę połączeń przed przeciążeniem i realizuje strategię *Fail Fast*.
* **Circuit Breaker** zapobiega wykonywaniu zapytań do niedostępnej bazy.
* **Fallback** zapewnia kontrolowaną odpowiedź zamiast błędu technicznego.
* **Global Exception Handler** mapuje wyjątki na odpowiedzi HTTP 503.

Takie połączenie mechanizmów pozwala znacząco zwiększyć odporność aplikacji, ograniczyć liczbę timeoutów oraz zachować stabilność systemu podczas awarii i wzmożonego ruchu. Jednocześnie należy pamiętać, że ochrona przed całkowitym wyczerpaniem zasobów serwera HTTP wymaga również odpowiedniej konfiguracji infrastruktury znajdującej się przed aplikacją.


Typowa Architektura systemu

```
Internet
│
▼
NGINX / API Gateway
│
├── Rate Limiting
├── Connection Limiting
├── Queue
├── Load Balancing
▼
Spring Boot (Tomcat)
▼
Bulkhead
▼
HikariCP
▼
PostgreSQL
```