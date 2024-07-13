package org.erkam.propertyuserservice.client.listing.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListingDeleteResponse {
    private String responseMessage;
    public static ListingDeleteResponse of(Long listingId) {
        return ListingDeleteResponse.builder()
                .responseMessage(
                        "Listing deleted successfully."
                                + "Id: " + listingId)
                .build();
    }
}
