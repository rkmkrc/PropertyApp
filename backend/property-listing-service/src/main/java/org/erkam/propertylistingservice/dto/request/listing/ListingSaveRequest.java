package org.erkam.propertylistingservice.dto.request.listing;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.erkam.propertylistingservice.model.enums.PropertyType;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ListingSaveRequest {
    private String title;
    private String description;
    private PropertyType type ;
    private BigDecimal price;
    private Integer area;
}
