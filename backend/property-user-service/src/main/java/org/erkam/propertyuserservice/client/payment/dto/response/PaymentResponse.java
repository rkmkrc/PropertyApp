package org.erkam.propertyuserservice.client.payment.dto.response;

import lombok.*;
import org.erkam.propertyuserservice.client.payment.dto.request.PaymentRequest;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
