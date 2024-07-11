package org.erkam.propertyuserservice.client.payment;

import org.erkam.propertyuserservice.client.payment.dto.request.PaymentRequest;
import org.erkam.propertyuserservice.dto.response.GenericResponse;
import org.erkam.propertyuserservice.client.payment.dto.response.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "payment-service", url = "localhost:8084/api/v1/payments")
public interface PaymentClient {

    @PostMapping
    ResponseEntity<GenericResponse<PaymentResponse>> receivePayment(@RequestBody PaymentRequest request);
}
