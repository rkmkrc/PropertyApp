package org.erkam.propertyuserservice.client.listing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.erkam.propertyuserservice.client.listing.ListingClient;
import org.erkam.propertyuserservice.client.listing.dto.request.ListingSaveRequest;
import org.erkam.propertyuserservice.client.listing.dto.response.ListingSaveResponse;
import org.erkam.propertyuserservice.constants.LogMessage;
import org.erkam.propertyuserservice.constants.enums.MessageStatus;
import org.erkam.propertyuserservice.dto.response.GenericResponse;
import org.erkam.propertyuserservice.exception.user.UserException;
import org.erkam.propertyuserservice.exception.user.UserExceptionMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ListingService {
    private final ListingClient listingClient;

    public ListingSaveResponse addListing(ListingSaveRequest request) {

        ResponseEntity<GenericResponse<ListingSaveResponse>> response = listingClient.save(request);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.LISTING_COULD_NOT_CREATED));
            log.error(LogMessage.generate(MessageStatus.NEG, response.getBody().getMessage()));
            throw new UserException.ListingCouldNotCreatedException(response.getBody().getMessage());
        }
        return response.getBody().getData();
    }

}
