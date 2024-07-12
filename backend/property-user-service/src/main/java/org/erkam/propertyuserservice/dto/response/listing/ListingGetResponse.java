package org.erkam.propertyuserservice.dto.response.listing;

import lombok.*;
import org.erkam.propertyuserservice.model.enums.ListingStatus;
import org.erkam.propertyuserservice.model.enums.PropertyType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListingGetResponse {
    private String title;
    private String description;
    private PropertyType type;
    private BigDecimal price;
    private ListingStatus status;
    private Integer area;
    private LocalDate publishedDate;
}
