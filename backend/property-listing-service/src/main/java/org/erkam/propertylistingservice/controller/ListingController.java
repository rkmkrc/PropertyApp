package org.erkam.propertylistingservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.erkam.propertylistingservice.dto.request.listing.ListingSaveRequest;
import org.erkam.propertylistingservice.dto.response.GenericResponse;
import org.erkam.propertylistingservice.dto.response.listing.ListingDeleteResponse;
import org.erkam.propertylistingservice.dto.response.listing.ListingGetResponse;
import org.erkam.propertylistingservice.dto.response.listing.ListingSaveResponse;
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
    public ResponseEntity<GenericResponse<ListingDeleteResponse>> deleteById(@PathVariable Long id) {
        return new ResponseEntity<>(listingService.deleteById(id), HttpStatus.OK);
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
    @GetMapping("user/{userId}")
    public ResponseEntity<GenericResponse<List<ListingGetResponse>>> getListingsByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(listingService.getListingsByUserId(userId), HttpStatus.OK);
    }
}
