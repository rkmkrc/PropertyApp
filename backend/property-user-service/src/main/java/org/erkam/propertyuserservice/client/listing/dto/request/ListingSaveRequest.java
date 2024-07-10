package org.erkam.propertyuserservice.client.listing.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.erkam.propertyuserservice.model.enums.PropertyType;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ListingSaveRequest {
    private Long userId;
    private String title;
    private String description;
    private PropertyType type ;
    private BigDecimal price;
    private Integer area;
}
