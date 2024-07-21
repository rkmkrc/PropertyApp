package org.erkam.propertyuserservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.erkam.propertyuserservice.dto.request.auth.AuthenticationRequest;
import org.erkam.propertyuserservice.dto.request.auth.RegisterRequest;
import org.erkam.propertyuserservice.dto.response.auth.AuthenticationResponse;
import org.erkam.propertyuserservice.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @Operation(
            description = "Post endpoint for registering.",
            summary = "Registering with a user information, email and password.",
            responses = {
                    @ApiResponse(
                            description = "Successfully registered.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized, email in use.",
                            responseCode = "403"
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @Operation(
            description = "Post endpoint for authentication.",
            summary = "Login with email and password.",
            responses = {
                    @ApiResponse(
                            description = "Successfully logged in.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Wrong email or password.",
                            responseCode = "403"
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
