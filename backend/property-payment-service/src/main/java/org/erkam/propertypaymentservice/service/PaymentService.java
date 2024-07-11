package org.erkam.propertypaymentservice.service;

import lombok.extern.slf4j.Slf4j;
import org.erkam.propertypaymentservice.constants.LogMessage;
import org.erkam.propertypaymentservice.constants.PaymentSuccessMessage;
import org.erkam.propertypaymentservice.constants.enums.MessageStatus;
import org.erkam.propertypaymentservice.dto.request.PaymentRequest;
import org.erkam.propertypaymentservice.dto.response.GenericResponse;
import org.erkam.propertypaymentservice.dto.response.PaymentResponse;
import org.springframework.stereotype.Service;

// NOTE: This is a mock service for payment if I have time I will implement it.
// TODO: Implement real service and DB for payments
@Service
@Slf4j
public class PaymentService {
    public GenericResponse<PaymentResponse> receivePayment(PaymentRequest request) {
        log.info(LogMessage.generate(MessageStatus.POS, PaymentSuccessMessage.PAYMENT_SUCCESSFULLY_RECEIVED));
        return GenericResponse.success(PaymentResponse.of(request));
    }
}
