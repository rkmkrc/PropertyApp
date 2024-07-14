package org.erkam.propertylistingreviewservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.erkam.propertylistingreviewservice.constants.LogMessage;
import org.erkam.propertylistingreviewservice.constants.enums.MessageStatus;
import org.erkam.propertylistingreviewservice.consumer.dto.ListingDto;
import org.erkam.propertylistingreviewservice.model.Listing;
import org.erkam.propertylistingreviewservice.model.enums.ListingStatus;
import org.erkam.propertylistingreviewservice.repository.ListingRepository;
import org.erkam.propertylistingservice.constants.ListingSuccessMessage;
import org.erkam.propertylistingservice.exception.listing.ListingException;
import org.erkam.propertylistingservice.exception.listing.ListingExceptionMessage;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ListingReviewService {
    private final ListingRepository listingRepository;

    // This method updates the status of the listing to ACTIVE.
    public void handleListingReview(ListingDto listingDto) {
        log.info(LogMessage.generate(MessageStatus.POS, "Handling listing review", listingDto.getId()));
        Listing listing = listingRepository.findById(listingDto.getId())
                .orElseThrow(() -> {
                    log.error(LogMessage.generate(MessageStatus.NEG, ListingExceptionMessage.LISTING_NOT_FOUND, listingDto.getId()));
                    throw new ListingException.ListingNotFoundException(ListingExceptionMessage.LISTING_NOT_FOUND, listingDto.getId());
                });
        listing.setStatus(ListingStatus.ACTIVE);
        listingRepository.save(listing);
        log.info(LogMessage.generate(MessageStatus.POS, ListingSuccessMessage.LISTING_STATUS_UPDATED, listingDto.getId()));
    }
}
