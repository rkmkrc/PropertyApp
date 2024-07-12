package org.erkam.propertyuserservice.dto.response.product;

import lombok.*;
import org.erkam.propertyuserservice.model.enums.PackageType;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageGetResponse {
    private String title;
    private PackageType type;
    private String description;
    private LocalDate expirationDate;
}
