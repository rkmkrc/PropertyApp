package org.erkam.propertypaymentservice.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.erkam.propertypaymentservice.model.enums.PackageType;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class PaymentRequest {
    private Long customerId;
    private String email;
    private PackageType packageType;
    private BigDecimal amount;
}
