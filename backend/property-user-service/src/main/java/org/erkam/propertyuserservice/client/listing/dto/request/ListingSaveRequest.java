package org.erkam.propertyuserservice.client.listing.dto.request;

import lombok.*;
import org.erkam.propertyuserservice.model.enums.PropertyType;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListingSaveRequest {
    private Long userId;
    private String title;
    private String description;
    private PropertyType type ;
    private BigDecimal price;
    private Integer area;
}
