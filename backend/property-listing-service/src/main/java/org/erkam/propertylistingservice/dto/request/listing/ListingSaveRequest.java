package org.erkam.propertylistingservice.dto.request.listing;

import lombok.*;
import org.erkam.propertylistingservice.model.enums.PropertyType;

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
