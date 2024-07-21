package org.erkam.propertylistingservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.erkam.propertylistingservice.constants.ListingSuccessMessage;
import org.erkam.propertylistingservice.constants.LogMessage;
import org.erkam.propertylistingservice.constants.enums.MessageStatus;
import org.erkam.propertylistingservice.dto.converter.ListingConverter;
import org.erkam.propertylistingservice.dto.request.listing.ListingSaveRequest;
import org.erkam.propertylistingservice.dto.request.listing.ListingUpdateRequest;
import org.erkam.propertylistingservice.dto.request.listing.ListingUpdateStatusRequest;
import org.erkam.propertylistingservice.dto.response.GenericResponse;
import org.erkam.propertylistingservice.dto.response.listing.*;
import org.erkam.propertylistingservice.exception.listing.ListingException;
import org.erkam.propertylistingservice.exception.listing.ListingExceptionMessage;
import org.erkam.propertylistingservice.model.Listing;
import org.erkam.propertylistingservice.model.enums.ListingStatus;
import org.erkam.propertylistingservice.producer.ListingReviewProducer;
import org.erkam.propertylistingservice.producer.dto.ListingDto;
import org.erkam.propertylistingservice.repository.ListingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ListingService {
    private final ListingRepository listingRepository;
    private final ListingReviewProducer listingReviewProducer;

    // Check if there is a duplicate listing exists or not, if exists then throw an exception else
    // add the listing to the listing review queue to let Listing Review Service to change the
    // status from IN_REVIEW to ACTIVE
    // then save it return a ListingSaveResponse
    public GenericResponse<ListingGetResponse> save(ListingSaveRequest request) {
        Listing listing = ListingConverter.toListing(request);
        if (isDuplicate(listing)) {
            log.error(LogMessage.generate(MessageStatus.NEG, ListingExceptionMessage.DUPLICATE_LISTING, request.getTitle()));
            throw new ListingException.DuplicateListingException(ListingExceptionMessage.DUPLICATE_LISTING, request.getTitle());
        }
        listing = listingRepository.save(listing);

        log.info(LogMessage.generate(MessageStatus.POS, ListingSuccessMessage.LISTING_CREATED, request.getTitle()));
        listingReviewProducer.sendNotification(new ListingDto(listing.getId()));
        ListingGetResponse response = ListingConverter.toListingGetResponse(listing);
        return GenericResponse.success(response);
    }

    // This method requires authentication
    // Update the status of a listing if it belongs to the currently authenticated user
    // if there are no data on database then throw an exception,
    // else create a ListingUpdateResponse then return it.
    public GenericResponse<ListingGetResponse> updateListing(Long id, ListingUpdateRequest request, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");

        Listing listing = listingRepository.findByIdAndUserId(id, userId).orElseThrow(() -> {
            log.error(LogMessage.generate(MessageStatus.NEG, ListingExceptionMessage.LISTING_NOT_FOUND_OR_YOU_DONT_HAVE_PERMISSION, id));
            return new ListingException.ListingNotFoundOrYouDontHavePermissionException(ListingExceptionMessage.LISTING_NOT_FOUND_OR_YOU_DONT_HAVE_PERMISSION, id);
        });

        listingRepository.save(listing.updateByRequest(request));
        log.info(LogMessage.generate(MessageStatus.POS, ListingSuccessMessage.LISTING_UPDATED, id));

        return GenericResponse.success(ListingConverter.toListingGetResponse(listing));
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
    public GenericResponse<ListingDeleteResponse> deleteById(Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        Listing listing = listingRepository.findByIdAndUserId(id, userId).orElseThrow(() -> {
            log.error(LogMessage.generate(MessageStatus.NEG, ListingExceptionMessage.LISTING_NOT_FOUND_OR_YOU_DONT_HAVE_PERMISSION, id));
            return new ListingException.ListingNotFoundOrYouDontHavePermissionException(ListingExceptionMessage.LISTING_NOT_FOUND_OR_YOU_DONT_HAVE_PERMISSION, id);
        });
        listingRepository.delete(listing);
        log.info(LogMessage.generate(MessageStatus.POS, ListingSuccessMessage.LISTING_DELETED, listing.getId()));
        return GenericResponse.success(ListingDeleteResponse.of(listing));
    }

    // NOTE: This method requires the authentication, to list all active and passive listings of user
    // TODO: Secure this methods endpoint
    // Get all listings from database of a user specified by userId
    // if there are no data on database then throw an exception,
    // else convert listings to ListingGetResponse list then return it.
    public GenericResponse<List<ListingGetResponse>> getListingsByUserId(Long userId) {
        List<Listing> listings = listingRepository.findListingsByUserId(userId);
        if (listings.isEmpty()) {
            log.error(LogMessage.generate(MessageStatus.NEG, ListingExceptionMessage.NO_LISTING_FOUND_FOR_THIS_USER, userId));
            throw new ListingException.NoDataOnDatabaseException(ListingExceptionMessage.NO_LISTING_FOUND_FOR_THIS_USER);
        }
        log.info(LogMessage.generate(MessageStatus.POS, ListingSuccessMessage.ALL_LISTINGS_OF_THIS_USER_FETCHED, userId));
        return GenericResponse.success(ListingConverter.toListingGetResponseList(listings));
    }

    // This method checks for a duplicate listing in repository
    // returns true if give listing parameter is exactly the same except the id field of a listing.
    private boolean isDuplicate(Listing listing) {
        System.out.println("IN IS DUPLICATE");
        return listingRepository.findByTitleAndPriceAndAreaAndDescriptionAndType(
                listing.getTitle(),
                listing.getPrice(),
                listing.getArea(),
                listing.getDescription(),
                listing.getType()
        ).isPresent();
    }

    // This method does not require authentication
    // Get only active listings from database of a user specified by userId
    // if there are no data on database then throw an exception,
    // else convert listings to ListingGetResponse list then return it.
    public GenericResponse<List<ListingGetResponse>> getActiveListingsByUserId(Long userId) {
        List<Listing> listings = listingRepository.findListingsByUserIdAndStatus(userId, ListingStatus.ACTIVE);
        if (listings.isEmpty()) {
            log.error(LogMessage.generate(MessageStatus.NEG, ListingExceptionMessage.NO_ACTIVE_LISTINGS_FOUND_FOR_THIS_USER, userId));
            throw new ListingException.NoActiveListingsFoundForThisUserException(ListingExceptionMessage.NO_ACTIVE_LISTINGS_FOUND_FOR_THIS_USER);
        }
        log.info(LogMessage.generate(MessageStatus.POS, ListingSuccessMessage.ALL_LISTINGS_OF_THIS_USER_FETCHED, userId));
        return GenericResponse.success(ListingConverter.toListingGetResponseList(listings));
    }

    // This method requires authentication
    // Get only passive listings from database of a user specified by userId
    // if there are no data on database then throw an exception,
    // else convert listings to ListingGetResponse list then return it.
    public GenericResponse<List<ListingGetResponse>> getPassiveListingsByUserId(Long userId) {
        List<Listing> listings = listingRepository.findListingsByUserIdAndStatus(userId, ListingStatus.PASSIVE);
        if (listings.isEmpty()) {
            log.error(LogMessage.generate(MessageStatus.NEG, ListingExceptionMessage.NO_PASSIVE_LISTINGS_FOUND_FOR_THIS_USER, userId));
            throw new ListingException.NoPassiveListingsFoundForThisUserException(ListingExceptionMessage.NO_PASSIVE_LISTINGS_FOUND_FOR_THIS_USER);
        }
        log.info(LogMessage.generate(MessageStatus.POS, ListingSuccessMessage.ALL_PASSIVE_LISTINGS_OF_THIS_USER_FETCHED, userId));
        return GenericResponse.success(ListingConverter.toListingGetResponseList(listings));
    }

    // This method requires authentication
    // Update the status of a listing if it belongs to the currently authenticated user
    // if there are no data on database then throw an exception,
    // else create a ListingUpdateStatusResponse then return it.
    public GenericResponse<ListingUpdateStatusResponse> changeStatusOfTheListing(ListingUpdateStatusRequest request, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");

        Listing listing = listingRepository.findByIdAndUserId(request.getListingId(), userId).orElseThrow(() -> {
            log.error(LogMessage.generate(MessageStatus.NEG, ListingExceptionMessage.LISTING_NOT_FOUND_OR_YOU_DONT_HAVE_PERMISSION, request.getListingId()));
            return new ListingException.ListingNotFoundOrYouDontHavePermissionException(ListingExceptionMessage.LISTING_NOT_FOUND_OR_YOU_DONT_HAVE_PERMISSION, request.getListingId());
        });

        listing.setStatus(request.getStatus());
        listingRepository.save(listing);
        log.info(LogMessage.generate(MessageStatus.POS, ListingSuccessMessage.LISTING_STATUS_UPDATED, request.getListingId()));

        return GenericResponse.success(ListingUpdateStatusResponse.of(request));
    }

    public GenericResponse<List<ListingGetResponse>> getAllActive() {
        List<Listing> listings = listingRepository.findByStatus(ListingStatus.ACTIVE);
        if (listings.isEmpty()) {
            log.error(LogMessage.generate(MessageStatus.NEG, ListingExceptionMessage.NO_DATA_ON_DATABASE));
            throw new ListingException.NoDataOnDatabaseException(ListingExceptionMessage.NO_DATA_ON_DATABASE);
        }
        log.info(LogMessage.generate(MessageStatus.POS, ListingSuccessMessage.ALL_ACTIVE_LISTINGS_FETCHED));
        return GenericResponse.success(ListingConverter.toListingGetResponseList(listings));
    }
}
