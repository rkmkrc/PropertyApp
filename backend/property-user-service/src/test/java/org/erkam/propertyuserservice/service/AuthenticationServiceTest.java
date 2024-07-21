package org.erkam.propertyuserservice.service;

import org.erkam.propertyuserservice.dto.request.auth.AuthenticationRequest;
import org.erkam.propertyuserservice.dto.request.auth.RegisterRequest;
import org.erkam.propertyuserservice.dto.response.auth.AuthenticationResponse;
import org.erkam.propertyuserservice.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    private AuthenticationService authenticationService;
    private UserService userService;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        jwtService = mock(JwtService.class);
        authenticationManager = mock(AuthenticationManager.class);
        authenticationService = new AuthenticationService(userService, jwtService, authenticationManager);
    }

    @Test
    void register_success() {
        // Given
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");

        User user = new User();
        user.setEmail("test@example.com");

        String token = "generated-jwt-token";

        when(userService.register(any(RegisterRequest.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn(token);

        // When
        AuthenticationResponse response = authenticationService.register(registerRequest);

        // Then
        assertEquals(token, response.getToken());
        verify(userService, times(1)).register(any(RegisterRequest.class));
        verify(jwtService, times(1)).generateToken(any(User.class));
    }

    @Test
    void authenticate_success() {
        // Given
        AuthenticationRequest authRequest = new AuthenticationRequest();
        authRequest.setEmail("test@example.com");
        authRequest.setPassword("password");

        User user = new User();
        user.setEmail("test@example.com");

        String token = "generated-jwt-token";

        when(userService.getByEmail(anyString())).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn(token);

        // When
        AuthenticationResponse response = authenticationService.authenticate(authRequest);

        // Then
        assertEquals(token, response.getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService, times(1)).getByEmail(anyString());
        verify(jwtService, times(1)).generateToken(any(User.class));
    }
}
