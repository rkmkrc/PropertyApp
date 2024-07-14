package org.erkam.propertyuserservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.erkam.propertyuserservice.client.listing.dto.request.ListingSaveRequest;
import org.erkam.propertyuserservice.client.listing.dto.request.ListingUpdateRequest;
import org.erkam.propertyuserservice.client.listing.dto.request.ListingUpdateStatusRequest;
import org.erkam.propertyuserservice.client.listing.dto.response.ListingDeleteResponse;
import org.erkam.propertyuserservice.client.listing.dto.response.ListingSaveResponse;
import org.erkam.propertyuserservice.client.listing.dto.response.ListingUpdateResponse;
import org.erkam.propertyuserservice.client.listing.dto.response.ListingUpdateStatusResponse;
import org.erkam.propertyuserservice.dto.request.user.BuyPackageRequest;
import org.erkam.propertyuserservice.dto.request.user.UserSaveRequest;
import org.erkam.propertyuserservice.dto.response.GenericResponse;
import org.erkam.propertyuserservice.dto.response.listing.ListingGetResponse;
import org.erkam.propertyuserservice.dto.response.product.PackageGetResponse;
import org.erkam.propertyuserservice.dto.response.product.BuyPackageResponse;
import org.erkam.propertyuserservice.dto.response.user.UserDeleteResponse;
import org.erkam.propertyuserservice.dto.response.user.UserGetResponse;
import org.erkam.propertyuserservice.dto.response.user.UserSaveResponse;
import org.erkam.propertyuserservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(
            description = "Post endpoint to create a user.",
            summary = "Create a user.",
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
    public ResponseEntity<GenericResponse<UserSaveResponse>> save(@RequestBody UserSaveRequest request) {
        return new ResponseEntity<>(userService.save(request), HttpStatus.CREATED);
    }

    @Operation(
            description = "Get endpoint to fetch all users.",
            summary = "Get all users.",
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
    public ResponseEntity<GenericResponse<List<UserGetResponse>>> getAll() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @Operation(
            description = "Get endpoint to fetch a user by id.",
            summary = "Get a user.",
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
    public ResponseEntity<GenericResponse<UserGetResponse>> getById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @Operation(
            description = "Delete endpoint for a user.",
            summary = "Delete a user by id.",
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
    public ResponseEntity<GenericResponse<UserDeleteResponse>> deleteById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.deleteById(id), HttpStatus.OK);
    }

    @Operation(
            description = "Post endpoint to create a listing for user.",
            summary = "Create a listing for user.",
            responses = {
                    @ApiResponse(
                            description = "Created.",
                            responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "Unauthorized.",
                            responseCode = "401"
                    ),
                    @ApiResponse(
                            description = "Forbidden.",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Bad Request.",
                            responseCode = "400"
                    )
            }
    )
    @PostMapping("/listings")
    public ResponseEntity<GenericResponse<ListingSaveResponse>> addListing(@RequestBody ListingSaveRequest request) {
            return new ResponseEntity<>(userService.addListing(request), HttpStatus.CREATED);
    }

    @Operation(
            description = "Post endpoint to buy a package for user.",
            summary = "Buy a package for user.",
            responses = {
                    @ApiResponse(
                            description = "Success.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized.",
                            responseCode = "401"
                    ),
                    @ApiResponse(
                            description = "Forbidden.",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Bad Request.",
                            responseCode = "400"
                    )
            }
    )
    @PostMapping("/packages")
    public ResponseEntity<GenericResponse<BuyPackageResponse>> buyPackage(@RequestBody BuyPackageRequest request) {
        return new ResponseEntity<>(userService.buyPackage(request), HttpStatus.OK);
    }

    @Operation(
            description = "Get endpoint of all packages of user.",
            summary = "Get all packages of user.",
            responses = {
                    @ApiResponse(
                            description = "Unauthorized.",
                            responseCode = "401"
                    ),
                    @ApiResponse(
                            description = "Forbidden.",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Bad Request.",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Success.",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping("/packages")
    public ResponseEntity<GenericResponse<List<PackageGetResponse>>> getPackages() {
        return new ResponseEntity<>(userService.getAllPackages(), HttpStatus.OK);
    }

    @Operation(
            description = "Get endpoint of all listings of user.",
            summary = "Get all listings of user.",
            responses = {
                    @ApiResponse(
                            description = "Unauthorized.",
                            responseCode = "401"
                    ),
                    @ApiResponse(
                            description = "Forbidden.",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Bad Request.",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Success.",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping("/listings")
    public ResponseEntity<GenericResponse<List<ListingGetResponse>>> getAllListings() {
        return new ResponseEntity<>(userService.getAllListings(), HttpStatus.OK);
    }

    @Operation(
            description = "Get endpoint of passive listings of user.",
            summary = "Get passive listings of user.",
            responses = {
                    @ApiResponse(
                            description = "Unauthorized.",
                            responseCode = "401"
                    ),
                    @ApiResponse(
                            description = "Forbidden.",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Bad Request.",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Success.",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping("/listings/passive")
    public ResponseEntity<GenericResponse<List<ListingGetResponse>>> getPassiveListings() {
        return new ResponseEntity<>(userService.getPassiveListings(), HttpStatus.OK);
    }

    @Operation(
            description = "Get endpoint of active listings of user.",
            summary = "Get active listings of user.",
            responses = {
                    @ApiResponse(
                            description = "Unauthorized.",
                            responseCode = "401"
                    ),
                    @ApiResponse(
                            description = "Forbidden.",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Bad Request.",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Success.",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping("/listings/active")
    public ResponseEntity<GenericResponse<List<ListingGetResponse>>> getActiveListings() {
        return new ResponseEntity<>(userService.getActiveListings(), HttpStatus.OK);
    }

    @Operation(
            description = "Delete endpoint of a listing of a user.",
            summary = "Delete a listing of a user by id.",
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
    @DeleteMapping("/listings/{id}")
    public ResponseEntity<GenericResponse<ListingDeleteResponse>> deleteListingById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.deleteListingById(id), HttpStatus.OK);
    }

    @Operation(
            description = "Update endpoint of the status a listing of a user.",
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
    @PutMapping("/listings/status")
    public ResponseEntity<GenericResponse<ListingUpdateStatusResponse>> updateStatusOfAListing(@RequestBody ListingUpdateStatusRequest request) {
        return new ResponseEntity<>(userService.updateTheStatusOfTheListing(request), HttpStatus.OK);
    }

    @Operation(
            description = "Put endpoint to update a listing of a user.",
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
    @PutMapping("/listings")
    public ResponseEntity<GenericResponse<ListingUpdateResponse>> updateTheListing(@RequestBody ListingUpdateRequest request) {
        return new ResponseEntity<>(userService.updateTheListing(request), HttpStatus.OK);
    }
}
