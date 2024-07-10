package org.erkam.propertyuserservice.client.listing.dto.response;

import lombok.*;
import org.erkam.propertyuserservice.client.listing.dto.request.ListingSaveRequest;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListingSaveResponse {
    private String responseMessage;
    public static ListingSaveResponse of(ListingSaveRequest request) {
        return ListingSaveResponse.builder()
                .responseMessage(
                        "Listing created successfully."
                                + " Title: " + request.getTitle()
                                + ", Description: " + request.getDescription()
                                + ", Price: " + request.getPrice()
                                + ", Type: " + request.getType())
                .build();
    }
}
