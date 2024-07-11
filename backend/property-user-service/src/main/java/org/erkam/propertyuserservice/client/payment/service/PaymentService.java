package org.erkam.propertyuserservice.client.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.erkam.propertyuserservice.client.payment.PaymentClient;
import org.erkam.propertyuserservice.client.payment.dto.request.PaymentRequest;
import org.erkam.propertyuserservice.client.payment.dto.response.PaymentResponse;
import org.erkam.propertyuserservice.constants.LogMessage;
import org.erkam.propertyuserservice.constants.enums.MessageStatus;
import org.erkam.propertyuserservice.dto.response.GenericResponse;
import org.erkam.propertyuserservice.exception.user.UserException;
import org.erkam.propertyuserservice.exception.user.UserExceptionMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class PaymentService {
    private final PaymentClient paymentClient;

    public PaymentResponse receivePayment(PaymentRequest request) {
        ResponseEntity<GenericResponse<PaymentResponse>> response = paymentClient.receivePayment(request);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.PAYMENT_FAILED));
            log.error(LogMessage.generate(MessageStatus.NEG, response.getBody().getMessage()));
            throw new UserException.PaymentFailedException(response.getBody().getMessage());
        }
        return response.getBody().getData();
    }

}
