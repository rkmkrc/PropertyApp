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
}