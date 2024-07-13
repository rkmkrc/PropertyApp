package org.erkam.propertylistingservice.dto.response.listing;

import lombok.*;
import org.erkam.propertylistingservice.model.enums.ListingStatus;
import org.erkam.propertylistingservice.model.enums.PropertyType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListingGetResponse {
    private Long id;
    private String title;
    private String description;
    private PropertyType type;
    private BigDecimal price;
    private ListingStatus status;
    private Integer area;
    private LocalDate publishedDate;
}
