package org.erkam.propertylistingservice.dto.request.listing;

import lombok.*;
import org.erkam.propertylistingservice.model.enums.PropertyType;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListingUpdateRequest {
    private Long id;
    private String title;
    private String description;
    private PropertyType type;
    private BigDecimal price;
    private Integer area;
}
