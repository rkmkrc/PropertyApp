package org.erkam.propertyapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.erkam.propertyapp.constants.ListingSuccessMessage;
import org.erkam.propertyapp.constants.LogMessage;
import org.erkam.propertyapp.constants.enums.MessageStatus;
import org.erkam.propertyapp.dto.converter.ListingConverter;
import org.erkam.propertyapp.dto.request.listing.ListingSaveRequest;
import org.erkam.propertyapp.dto.response.GenericResponse;
import org.erkam.propertyapp.dto.response.listing.ListingDeleteResponse;
import org.erkam.propertyapp.dto.response.listing.ListingGetResponse;
import org.erkam.propertyapp.dto.response.listing.ListingSaveResponse;
import org.erkam.propertyapp.exception.listing.ListingException;
import org.erkam.propertyapp.exception.listing.ListingExceptionMessage;
import org.erkam.propertyapp.model.Listing;
import org.erkam.propertyapp.repository.ListingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ListingService {
    private final ListingRepository listingRepository;

    // TODO: Implement a check mechanism to know is there any duplicate
    //  of this listing in database
    public GenericResponse<ListingSaveResponse> save(ListingSaveRequest request) {
        // TODO: Check here and throw an exception if any duplicate exists
        listingRepository.save(ListingConverter.toListing(request));
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
}
