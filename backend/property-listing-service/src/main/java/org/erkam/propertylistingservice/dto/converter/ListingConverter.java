package org.erkam.propertylistingservice.dto.converter;


import org.erkam.propertylistingservice.dto.request.listing.ListingSaveRequest;
import org.erkam.propertylistingservice.dto.request.listing.ListingUpdateRequest;
import org.erkam.propertylistingservice.dto.response.listing.ListingGetResponse;
import org.erkam.propertylistingservice.model.Listing;
import org.erkam.propertylistingservice.model.enums.ListingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ListingConverter {
    public static Listing toListing(ListingSaveRequest request) {
        return Listing.builder()
                .userId(request.getUserId())
                .title(request.getTitle())
                .description(request.getDescription())
                .area(request.getArea())
                .price(request.getPrice())
                .type(request.getType())
                .status(ListingStatus.IN_REVIEW)
                .publishedDate(LocalDate.now())
                .build();
    }

    public static ListingGetResponse toListingGetResponse(Listing listing) {
        return ListingGetResponse.builder()
                .id(listing.getId())
                .title(listing.getTitle())
                .description(listing.getDescription())
                .area(listing.getArea())
                .status(listing.getStatus())
                .publishedDate(listing.getPublishedDate())
                .price(listing.getPrice())
                .type(listing.getType())
                .build();
    }

    public static List<ListingGetResponse> toListingGetResponseList(List<Listing> listings) {
        return listings.stream()
                .map(ListingConverter::toListingGetResponse)
                .collect(Collectors.toList());
    }
}
