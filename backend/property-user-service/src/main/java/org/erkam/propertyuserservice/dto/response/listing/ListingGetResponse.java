package org.erkam.propertyuserservice.dto.response.listing;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Long id;
    private String title;
    private String description;
    private PropertyType type;
    private BigDecimal price;
    private ListingStatus status;
    private Integer area;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate publishedDate;
}
