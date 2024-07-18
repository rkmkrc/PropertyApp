package org.erkam.propertyuserservice.client.listing;

import jakarta.servlet.http.HttpServletRequest;
import org.erkam.propertyuserservice.client.listing.dto.request.ListingSaveRequest;
import org.erkam.propertyuserservice.client.listing.dto.request.ListingUpdateRequest;
import org.erkam.propertyuserservice.client.listing.dto.request.ListingUpdateStatusRequest;
import org.erkam.propertyuserservice.client.listing.dto.response.ListingDeleteResponse;
import org.erkam.propertyuserservice.client.listing.dto.response.ListingSaveResponse;
import org.erkam.propertyuserservice.client.listing.dto.response.ListingUpdateResponse;
import org.erkam.propertyuserservice.client.listing.dto.response.ListingUpdateStatusResponse;
import org.erkam.propertyuserservice.dto.response.GenericResponse;
import org.erkam.propertyuserservice.dto.response.listing.ListingGetResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "listing-service", url = "localhost:8083/api/v1/listings")
public interface ListingClient {

    @PostMapping
    ResponseEntity<GenericResponse<ListingSaveResponse>> save(@RequestBody ListingSaveRequest request);

    @GetMapping("/user/{userId}")
    ResponseEntity<GenericResponse<List<ListingGetResponse>>> getListingsByUserId(@PathVariable Long userId);

    @GetMapping("/user/{userId}/active")
    ResponseEntity<GenericResponse<List<ListingGetResponse>>> getActiveListingsOfUser(@PathVariable Long userId);

    @GetMapping("/user/{userId}/passive")
    ResponseEntity<GenericResponse<List<ListingGetResponse>>> getPassiveListingsOfUser(@PathVariable Long userId);

    @DeleteMapping("/{id}")
    ResponseEntity<GenericResponse<ListingDeleteResponse>> deleteById(@PathVariable Long id);

    @PutMapping("/user/status")
    ResponseEntity<GenericResponse<ListingUpdateStatusResponse>> updateTheStatusOfTheListing(@RequestBody ListingUpdateStatusRequest request);

    @PutMapping("/{id}")
    ResponseEntity<GenericResponse<ListingUpdateResponse>> updateTheListing(@PathVariable Long id, @RequestBody ListingUpdateRequest request);
}
