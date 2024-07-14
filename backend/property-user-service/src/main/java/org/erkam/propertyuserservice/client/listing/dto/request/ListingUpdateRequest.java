package org.erkam.propertyuserservice.client.listing.dto.request;

import lombok.*;
import org.erkam.propertyuserservice.model.enums.PropertyType;

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
