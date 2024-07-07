package org.erkam.propertyservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.erkam.propertyservice.constants.ListingSuccessMessage;
import org.erkam.propertyservice.constants.LogMessage;
import org.erkam.propertyservice.constants.enums.MessageStatus;
import org.erkam.propertyservice.dto.converter.ListingConverter;
import org.erkam.propertyservice.dto.request.listing.ListingSaveRequest;
import org.erkam.propertyservice.dto.response.GenericResponse;
import org.erkam.propertyservice.dto.response.listing.ListingDeleteResponse;
import org.erkam.propertyservice.dto.response.listing.ListingGetResponse;
import org.erkam.propertyservice.dto.response.listing.ListingSaveResponse;
import org.erkam.propertyservice.exception.listing.ListingException;
import org.erkam.propertyservice.exception.listing.ListingExceptionMessage;
import org.erkam.propertyservice.model.Listing;
import org.erkam.propertyservice.repository.ListingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ListingService {
    private final ListingRepository listingRepository;

    // Check if there is a duplicate listing exists or not, if exists then throw an exception
    // else save it return a ListingSaveResponse
    public GenericResponse<ListingSaveResponse> save(ListingSaveRequest request) {
        Listing listing = ListingConverter.toListing(request);
        if (isDuplicate(listing)) {
            log.error(LogMessage.generate(MessageStatus.NEG, ListingExceptionMessage.DUPLICATE_LISTING, request.getTitle()));
            throw new ListingException.DuplicateListingException(ListingExceptionMessage.DUPLICATE_LISTING, request.getTitle());
        }
        listingRepository.save(listing);
        log.info(LogMessage.generate(MessageStatus.POS, ListingSuccessMessage.LISTING_CREATED, request.getTitle()));
        return GenericResponse.success(ListingSaveResponse.of(request));
    }

    // Get all listings from database if there is no data on database then throw an exception,
    // else convert listings to ListingGetResponse list then return it.
    public GenericResponse<List<ListingGetResponse>> getAll() {
        List<Listing> listings = listingRepository.findAll();
        if (listings.isEmpty()) {
            log.error(LogMessage.generate(MessageStatus.NEG, ListingExceptionMessage.NO_DATA_ON_DATABASE));
            throw new ListingException.NoDataOnDatabaseException(ListingExceptionMessage.NO_DATA_ON_DATABASE);
        }
        log.info(LogMessage.generate(MessageStatus.POS, ListingSuccessMessage.ALL_LISTINGS_FETCHED));
        return GenericResponse.success(ListingConverter.toListingGetResponseList(listings));

    }

    // First check by id, to find out whether listing exists or not,
    // if not exists then throw an exception, else return ListingGetResponse
    public GenericResponse<ListingGetResponse> getById(Long id) {
        Listing listing = listingRepository.findById(id).orElseThrow(() -> {
            log.error(LogMessage.generate(MessageStatus.NEG, ListingExceptionMessage.LISTING_NOT_FOUND, id));
            return new ListingException.ListingNotFoundException(ListingExceptionMessage.LISTING_NOT_FOUND, id);
        });
        log.info(LogMessage.generate(MessageStatus.POS, ListingSuccessMessage.LISTING_FETCHED, listing.getId()));
        return GenericResponse.success(ListingConverter.toListingGetResponse(listing));
    }

    // First check by id, to find out whether listing exists or not,
    // if not exists then throw an exception, else delete the listing and return ListingDeleteResponse
    public GenericResponse<ListingDeleteResponse> deleteById(Long id) {
        Listing listing = listingRepository.findById(id).orElseThrow(() -> {
            log.error(LogMessage.generate(MessageStatus.NEG, ListingExceptionMessage.LISTING_NOT_FOUND, id));
            return new ListingException.ListingNotFoundException(ListingExceptionMessage.LISTING_NOT_FOUND, id);
        });
        listingRepository.delete(listing);
        log.info(LogMessage.generate(MessageStatus.POS, ListingSuccessMessage.LISTING_DELETED, listing.getId()));
        return GenericResponse.success(ListingDeleteResponse.of(listing));
    }

    // This method checks for a duplicate listing in repository
    // returns true if give listing parameter is exactly the same except the id field of a listing.
    private boolean isDuplicate(Listing listing) {
        return listingRepository.findByTitleAndPriceAndAreaAndDescriptionAndTypeAndStatus(
                listing.getTitle(),
                listing.getPrice(),
                listing.getArea(),
                listing.getDescription(),
                listing.getType(),
                listing.getStatus()
        ).isPresent();
    }
}
