# Payment Service — Observability Stack

Spring Boot payment service instrumented with a modern observability stack using:

- OpenTelemetry
- Prometheus
- Grafana
- Loki
- Tempo
- Promtail

---

# Architecture

```text
Spring Boot
│
├── Metrics ───────► Prometheus ───► Grafana
│
├── Logs ──────────► Promtail ─────► Loki ─────► Grafana
│
└── Traces ───────► OpenTelemetry Collector ───► Tempo ───► Grafana

```

# Screenshots

## Metrics Dashboard

![metrics-dashboard](src/main/resources/docs/screenshots/metrics-dashboard.png)

## Logs Dashboard

![logs-dashboard](src/main/resources/docs/screenshots/logs-dashboard.png)

## Tracing Dashboard

![tracing-dashboard](src/main/resources/docs/screenshots/tracing-dashboard.png)

# Observability Features

- RED metrics (Rate, Errors, Duration)
- JVM monitoring
- HikariCP monitoring
- Structured JSON logging
- Distributed tracing
- Centralized logging
- Grafana alerting
- OpenTelemetry instrumentation

# Future Improvements

- Kubernetes deployment
- Grafana provisioning
- Business metrics
- Exemplars support
- CI/CD integration