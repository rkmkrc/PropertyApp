package org.erkam.propertyservice.dto.converter;


import org.erkam.propertyservice.dto.request.listing.ListingSaveRequest;
import org.erkam.propertyservice.dto.response.listing.ListingGetResponse;
import org.erkam.propertyservice.model.Listing;
import org.erkam.propertyservice.model.enums.ListingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ListingConverter {
    public static Listing toListing(ListingSaveRequest request) {
        return Listing.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .area(request.getArea())
                .price(request.getPrice())
                .type(request.getType())
                .status(ListingStatus.IN_REVIEW)
                .publishedDate(LocalDateTime.now())
                .build();
    }

    public static List<ListingGetResponse> toListingGetResponseList(List<Listing> listings) {
        return listings.stream()
                .map(ListingConverter::toListingGetResponse)
                .collect(Collectors.toList());
    }

    public static ListingGetResponse toListingGetResponse(Listing listing) {
        return ListingGetResponse.builder()
                .title(listing.getTitle())
                .description(listing.getDescription())
                .area(listing.getArea())
                .publishedDate(listing.getPublishedDate())
                .price(listing.getPrice())
                .type(listing.getType())
                .build();
    }
}