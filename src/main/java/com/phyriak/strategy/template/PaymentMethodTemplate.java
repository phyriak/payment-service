package com.phyriak.strategy.template;

import com.phyriak.strategy.PaymentStrategy;
import com.phyriak.strategy.model.PaymentResult;

public abstract class PaymentMethodTemplate implements PaymentStrategy {
    // Metoda Szablonowa - stały proces
    //implentuje processPayment ze strategii
    //tempalte dodaje stepy i kolejną abstrakcje wylłacznie z impelntacją szególej platnosci

    @Override
    public final PaymentResult processPayment(Long id) {
        validateOrder(id);
        logTransactionStart();

        // Wywołanie zewnętrznej STRATEGII wewnątrz SZABLONU
        PaymentResult result = pay();

        if (result.success()) {
            updateOrderStatus();
            sendInvoice();
            return result;
        } else {
            handleFailure();
            throw new RuntimeException("Payment fails");
        }
    }

    protected abstract PaymentResult pay();

    protected abstract void validateOrder(double amount);

    private void logTransactionStart() {
        System.out.println("2. Zapisanie transakcji jako 'W TOKU' w DB");
    }

    private void updateOrderStatus() {
        System.out.println("4. Zmiana statusu zamówienia na 'OPŁACONE' w DB");
    }

    private void sendInvoice() {
        System.out.println("5. Wysyłanie faktury PDF do klienta");
    }

    protected void handleFailure() {
        System.out.println("X. Obsługa błędu płatności");
    }
}
