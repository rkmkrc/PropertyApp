package org.erkam.propertyuserservice.client.listing.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.erkam.propertyuserservice.model.enums.ListingStatus;

@Getter
@Setter
@Builder
public class ListingUpdateStatusRequest {
    private ListingStatus status;
    private Long listingId;
}
