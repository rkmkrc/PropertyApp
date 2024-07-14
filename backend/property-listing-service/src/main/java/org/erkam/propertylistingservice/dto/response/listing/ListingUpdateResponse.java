package org.erkam.propertylistingservice.dto.response.listing;

import lombok.*;
import org.erkam.propertylistingservice.model.Listing;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListingUpdateResponse {
    private String responseMessage;

    public static ListingUpdateResponse of(Listing listing) {
        return ListingUpdateResponse.builder()
                .responseMessage("Listing updated successfully. "
                        + " Title: " + listing.getTitle()
                        + ", Type: " + listing.getType()
                        + ", Description: " + listing.getDescription()
                        + ", Price: " + listing.getPrice()
                        + ", Area: " + listing.getArea()
                        + ", Status: " + listing.getStatus()
                )
                .build();
    }
}
