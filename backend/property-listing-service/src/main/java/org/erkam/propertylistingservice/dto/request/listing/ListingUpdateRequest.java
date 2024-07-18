package org.erkam.propertylistingservice.dto.request.listing;

import lombok.*;
import org.erkam.propertylistingservice.model.enums.ListingStatus;
import org.erkam.propertylistingservice.model.enums.PropertyType;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListingUpdateRequest {
    private String title;
    private String description;
    private PropertyType type;
    private ListingStatus status;
    private BigDecimal price;
    private Integer area;
}
