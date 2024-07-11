package org.erkam.propertypaymentservice.model;

import lombok.*;
import org.erkam.propertypaymentservice.model.enums.PackageType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    private Long id;
    private Long userId;
    private PackageType packageType;
    private BigDecimal amount;
    private LocalDateTime issuedAt;
}
