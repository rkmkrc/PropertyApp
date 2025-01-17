package org.erkam.propertyuserservice.client.listing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.erkam.propertyuserservice.client.listing.ListingClient;
import org.erkam.propertyuserservice.client.listing.dto.request.ListingSaveRequest;
import org.erkam.propertyuserservice.client.listing.dto.request.ListingUpdateRequest;
import org.erkam.propertyuserservice.client.listing.dto.request.ListingUpdateStatusRequest;
import org.erkam.propertyuserservice.client.listing.dto.response.ListingDeleteResponse;
import org.erkam.propertyuserservice.client.listing.dto.response.ListingSaveResponse;
import org.erkam.propertyuserservice.client.listing.dto.response.ListingUpdateResponse;
import org.erkam.propertyuserservice.client.listing.dto.response.ListingUpdateStatusResponse;
import org.erkam.propertyuserservice.constants.LogMessage;
import org.erkam.propertyuserservice.constants.enums.MessageStatus;
import org.erkam.propertyuserservice.dto.response.GenericResponse;
import org.erkam.propertyuserservice.dto.response.listing.ListingGetResponse;
import org.erkam.propertyuserservice.exception.user.UserException;
import org.erkam.propertyuserservice.exception.user.UserExceptionMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ListingService {
    private final ListingClient listingClient;

    public ListingGetResponse addListing(ListingSaveRequest request) {
        log.debug("Request to listing service: {}", request);

        ResponseEntity<GenericResponse<ListingGetResponse>> response;
        try {
            response = listingClient.save(request);
            log.debug("Raw response from listing service: {}", response);
            log.debug("Response body from listing service: {}", response.getBody());
        } catch (IllegalArgumentException ex) {
            log.error("Illegal argument exception: {}", ex.getMessage(), ex);
            throw new IllegalStateException("Error invoking listingClient.save(request): " + ex.getMessage(), ex);
        } catch (Exception e) {
            log.error("Exception occurred while calling listing service: {}", e.getMessage(), e);
            throw new UserException.ListingCouldNotCreatedException("Error while calling listing service");
        }

        if (response == null || response.getBody() == null) {
            log.error("Response or response body is null");
            throw new UserException.ListingCouldNotCreatedException("Null response from listing service");
        }

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Listing could not be created: {}", response.getBody().getMessage());
            throw new UserException.ListingCouldNotCreatedException(response.getBody().getMessage());
        }

        return response.getBody().getData();
    }

    public List<ListingGetResponse> getAllListingsOfUser(Long userId) {

        ResponseEntity<GenericResponse<List<ListingGetResponse>>> response = listingClient.getListingsByUserId(userId);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_HAS_NOT_ANY_LISTINGS));
            log.error(LogMessage.generate(MessageStatus.NEG, response.getBody().getMessage()));
            throw new UserException.UserHasNotAnyListingsException(response.getBody().getMessage());
        }
        return response.getBody().getData();
    }

    public List<ListingGetResponse> getActiveListingsOfUser(Long userId) {

        ResponseEntity<GenericResponse<List<ListingGetResponse>>> response = listingClient.getActiveListingsOfUser(userId);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_HAS_NOT_ANY_ACTIVE_LISTINGS));
            log.error(LogMessage.generate(MessageStatus.NEG, response.getBody().getMessage()));
            throw new UserException.UserHasNotAnyActiveListingsException(response.getBody().getMessage());
        }
        return response.getBody().getData();
    }

    public List<ListingGetResponse> getPassiveListingsOfUser(Long userId) {

        ResponseEntity<GenericResponse<List<ListingGetResponse>>> response = listingClient.getPassiveListingsOfUser(userId);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_HAS_NOT_ANY_PASSIVE_LISTINGS));
            log.error(LogMessage.generate(MessageStatus.NEG, response.getBody().getMessage()));
            throw new UserException.UserHasNotAnyPassiveListingsException(response.getBody().getMessage());
        }
        return response.getBody().getData();
    }

    public ListingDeleteResponse deleteListing(Long id) {

        ResponseEntity<GenericResponse<ListingDeleteResponse>> response = listingClient.deleteById(id);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.LISTING_COULD_NOT_DELETED));
            log.error(LogMessage.generate(MessageStatus.NEG, response.getBody().getMessage()));
            throw new UserException.ListingCouldNotDeletedException(response.getBody().getMessage());
        }
        return response.getBody().getData();
    }


    public ListingUpdateStatusResponse updateTheStatusOfTheListing(ListingUpdateStatusRequest request) {

        ResponseEntity<GenericResponse<ListingUpdateStatusResponse>> response = listingClient.updateTheStatusOfTheListing(request);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.LISTING_STATUS_COULD_NOT_UPDATED));
            log.error(LogMessage.generate(MessageStatus.NEG, response.getBody().getMessage()));
            throw new UserException.ListingStatusCouldNotUpdatedException(response.getBody().getMessage());
        }
        return response.getBody().getData();
    }

    public ListingGetResponse updateTheListing(Long id, ListingUpdateRequest request) {

        ResponseEntity<GenericResponse<ListingGetResponse>> response = listingClient.updateTheListing(id, request);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.LISTING_COULD_NOT_UPDATED));
            log.error(LogMessage.generate(MessageStatus.NEG, response.getBody().getMessage()));
            throw new UserException.ListingCouldNotUpdatedException(response.getBody().getMessage());
        }
        return response.getBody().getData();
    }
}
