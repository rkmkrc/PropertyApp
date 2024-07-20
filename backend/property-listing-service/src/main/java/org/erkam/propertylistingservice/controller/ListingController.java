package org.erkam.propertylistingservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.erkam.propertylistingservice.dto.request.listing.ListingSaveRequest;
import org.erkam.propertylistingservice.dto.request.listing.ListingUpdateRequest;
import org.erkam.propertylistingservice.dto.request.listing.ListingUpdateStatusRequest;
import org.erkam.propertylistingservice.dto.response.GenericResponse;
import org.erkam.propertylistingservice.dto.response.listing.*;
import org.erkam.propertylistingservice.service.ListingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Listing")
@RestController
@RequestMapping("/api/v1/listings")
@RequiredArgsConstructor
public class ListingController {
    private final ListingService listingService;

    @Operation(
            description = "Post endpoint to create a listing.",
            summary = "Create a listing.",
            responses = {
                    @ApiResponse(
                            description = "Created.",
                            responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "Unauthorized.",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Bad Request.",
                            responseCode = "400"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<GenericResponse<ListingSaveResponse>> save(@RequestBody ListingSaveRequest request) {
        return new ResponseEntity<>(listingService.save(request), HttpStatus.CREATED);
    }

    @Operation(
            description = "Get endpoint to fetch all listings.",
            summary = "Get all listings.",
            responses = {
                    @ApiResponse(
                            description = "Success.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized.",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Not found.",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<GenericResponse<List<ListingGetResponse>>> getAll() {
        return new ResponseEntity<>(listingService.getAll(), HttpStatus.OK);
    }

    @Operation(
            description = "Get endpoint to fetch all active listings.",
            summary = "Get all active listings.",
            responses = {
                    @ApiResponse(
                            description = "Success.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized.",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Not found.",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping("/active")
    public ResponseEntity<GenericResponse<List<ListingGetResponse>>> getAllActive() {
        return new ResponseEntity<>(listingService.getAllActive(), HttpStatus.OK);
    }

    @Operation(
            description = "Get endpoint to fetch a listing by id.",
            summary = "Get a listing.",
            responses = {
                    @ApiResponse(
                            description = "Success.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized.",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Not found.",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<ListingGetResponse>> getById(@PathVariable Long id) {
        return new ResponseEntity<>(listingService.getById(id), HttpStatus.OK);
    }

    @Operation(
            description = "Delete endpoint for a listing.",
            summary = "Delete a listing by id.",
            responses = {
                    @ApiResponse(
                            description = "Success.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized.",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Not found.",
                            responseCode = "404"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<ListingDeleteResponse>> deleteById(@PathVariable Long id, HttpServletRequest request) {
        return new ResponseEntity<>(listingService.deleteById(id, request), HttpStatus.OK);
    }

    @Operation(
            description = "Get endpoint to fetch all listings of a user.",
            summary = "Get all listings of a user.",
            responses = {
                    @ApiResponse(
                            description = "Success.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized.",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Not found.",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<GenericResponse<List<ListingGetResponse>>> getListingsByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(listingService.getListingsByUserId(userId), HttpStatus.OK);
    }

    @Operation(
            description = "Get endpoint to fetch active listings of a user. For public usage.",
            summary = "Get active listings of a user.",
            responses = {
                    @ApiResponse(
                            description = "Success.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Not found.",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping("/user/{userId}/active")
    public ResponseEntity<GenericResponse<List<ListingGetResponse>>> getActiveListingsOfUser(@PathVariable Long userId) {
        return new ResponseEntity<>(listingService.getActiveListingsByUserId(userId), HttpStatus.OK);
    }

    @Operation(
            description = "Get endpoint to fetch passive listings of a user. Only authorized usage.",
            summary = "Get passive listings of a user.",
            responses = {
                    @ApiResponse(
                            description = "Success.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized.",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Not found.",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping("/user/{userId}/passive")
    public ResponseEntity<GenericResponse<List<ListingGetResponse>>> getPassiveListingsOfUser(@PathVariable Long userId) {
        return new ResponseEntity<>(listingService.getPassiveListingsByUserId(userId), HttpStatus.OK);
    }

    @Operation(
            description = "Put endpoint change the status of the listing of a user. Only authorized usage.",
            summary = "Update the status of a listing of a user.",
            responses = {
                    @ApiResponse(
                            description = "Success.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized.",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Not found.",
                            responseCode = "404"
                    )
            }
    )
    @PutMapping("/user/status")
    public ResponseEntity<GenericResponse<ListingUpdateStatusResponse>> updateTheStatusOfTheListing(@RequestBody ListingUpdateStatusRequest request, HttpServletRequest httpRequest) {
        return new ResponseEntity<>(listingService.changeStatusOfTheListing(request, httpRequest), HttpStatus.OK);
    }

    @Operation(
            description = "Put endpoint update the listing of a user. Only authorized usage.",
            summary = "Update a listing of a user.",
            responses = {
                    @ApiResponse(
                            description = "Success.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized.",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Not found.",
                            responseCode = "404"
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<ListingGetResponse>> updateTheListing(@PathVariable Long id, @RequestBody ListingUpdateRequest request, HttpServletRequest httpRequest) {
        return new ResponseEntity<>(listingService.updateListing(id, request, httpRequest), HttpStatus.OK);
    }
}
