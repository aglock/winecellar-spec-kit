package com.winecellar.auth.delivery;

public interface ActivationDeliveryPort {

    DeliveryResult deliver(String email, String activationToken);

    record DeliveryResult(String activationLink, String status) {
    }
}
