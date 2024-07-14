package org.erkam.propertyuserservice.client.listing.dto.response;

import lombok.*;
import org.erkam.propertyuserservice.client.listing.dto.request.ListingUpdateStatusRequest;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListingUpdateStatusResponse {
    private String responseMessage;
    public static ListingUpdateStatusResponse of(ListingUpdateStatusRequest request) {
        return ListingUpdateStatusResponse.builder()
                .responseMessage(
                        "Listing status updated as "+ request.getStatus() + " successfully.")
                .build();
    }
}