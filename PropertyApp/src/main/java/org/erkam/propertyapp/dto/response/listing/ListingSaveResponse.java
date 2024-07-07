package org.erkam.propertyapp.dto.response.listing;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.erkam.propertyapp.dto.request.listing.ListingSaveRequest;

@Getter
@Setter
@Builder
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
