package org.erkam.propertyuserservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.erkam.propertyuserservice.dto.request.user.UserSaveRequest;
import org.erkam.propertyuserservice.dto.response.GenericResponse;
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
}
