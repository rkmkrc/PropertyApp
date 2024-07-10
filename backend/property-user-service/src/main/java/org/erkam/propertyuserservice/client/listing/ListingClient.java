package org.erkam.propertyuserservice.client.listing;

import org.erkam.propertyuserservice.client.listing.dto.request.ListingSaveRequest;
import org.erkam.propertyuserservice.client.listing.dto.response.ListingSaveResponse;
import org.erkam.propertyuserservice.dto.response.GenericResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "listing-service", url = "localhost:8083/api/v1/listings")
public interface ListingClient {

    @PostMapping
    ResponseEntity<GenericResponse<ListingSaveResponse>> save(@RequestBody ListingSaveRequest request);
}
