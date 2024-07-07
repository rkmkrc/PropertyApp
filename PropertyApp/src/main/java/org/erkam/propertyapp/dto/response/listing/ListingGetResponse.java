package org.erkam.propertyapp.dto.response.listing;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.erkam.propertyapp.model.enums.PropertyType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ListingGetResponse {
    private String title;
    private String description;
    private PropertyType type;
    private BigDecimal price;
    private Integer area;
    private LocalDateTime publishedDate;
}
