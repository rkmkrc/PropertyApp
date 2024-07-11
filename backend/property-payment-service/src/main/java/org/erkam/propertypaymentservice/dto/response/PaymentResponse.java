package org.erkam.propertypaymentservice.dto.response;

import lombok.*;
import org.erkam.propertypaymentservice.dto.request.PaymentRequest;

@Getter
@Setter
@Builder
public class PaymentResponse {
    private String responseMessage;
    public static PaymentResponse of(PaymentRequest request) {
        return PaymentResponse.builder()
                .responseMessage(
                        "Payment received successfully."
                                + " Customer ID: " + request.getCustomerId()
                                + ", Email : " + request.getEmail()
                                + ", Product: " + request.getPackageType()
                                + ", Total Amount: " + request.getAmount())
                .build();
    }
}
