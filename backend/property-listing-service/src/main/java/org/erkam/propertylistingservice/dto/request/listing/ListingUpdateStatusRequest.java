package org.erkam.propertylistingservice.dto.request.listing;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.erkam.propertylistingservice.model.enums.ListingStatus;

@Getter
@Setter
@Builder
public class ListingUpdateStatusRequest {
    private ListingStatus status;
    private Long listingId;
}
