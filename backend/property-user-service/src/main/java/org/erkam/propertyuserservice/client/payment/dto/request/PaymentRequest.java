package org.erkam.propertyuserservice.client.payment.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.erkam.propertyuserservice.dto.request.user.BuyPackageRequest;
import org.erkam.propertyuserservice.model.User;
import org.erkam.propertyuserservice.model.enums.PackageType;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class PaymentRequest {
    private Long customerId;
    private String email;
    private PackageType packageType;
    private BigDecimal amount;

    public static PaymentRequest from(User user, BuyPackageRequest request) {
        return PaymentRequest.builder()
                .customerId(user.getId())
                .email(user.getEmail())
                .packageType(request.getType())
                // TODO: Fix amount later, this is just for mocking
                .amount(BigDecimal.valueOf(29.99))
                .build();
    }
}
