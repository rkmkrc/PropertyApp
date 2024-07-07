package org.erkam.propertyservice.dto.response.listing;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.erkam.propertyservice.model.Listing;

@Getter
@Setter
@Builder
public class ListingDeleteResponse {
    private String responseMessage;
    public static ListingDeleteResponse of(Listing listing) {
        return ListingDeleteResponse.builder()
                .responseMessage(
                        "Listing deleted successfully."
                                + "Id: " + listing.getId()
                                + ", Title: " + listing.getTitle()
                                + ", Type: " + listing.getType()
                                + ", Price: " + listing.getPrice())
                .build();
    }
}
