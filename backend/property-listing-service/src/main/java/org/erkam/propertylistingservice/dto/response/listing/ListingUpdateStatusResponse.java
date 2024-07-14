package org.erkam.propertylistingservice.dto.response.listing;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.erkam.propertylistingservice.dto.request.listing.ListingUpdateStatusRequest;

@Getter
@Setter
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