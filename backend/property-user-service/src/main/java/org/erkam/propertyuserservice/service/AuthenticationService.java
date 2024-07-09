package org.erkam.propertyuserservice.service;

import lombok.RequiredArgsConstructor;
import org.erkam.propertyuserservice.constants.AuthenticationSuccessMessage;
import org.erkam.propertyuserservice.constants.LogMessage;
import org.erkam.propertyuserservice.constants.enums.MessageStatus;
import org.erkam.propertyuserservice.dto.request.auth.AuthenticationRequest;
import org.erkam.propertyuserservice.dto.request.auth.RegisterRequest;
import org.erkam.propertyuserservice.dto.response.auth.AuthenticationResponse;
import org.erkam.propertyuserservice.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = userService.register(request);
        var jwtToken = jwtService.generateToken(user);
        log.info(LogMessage.generate(MessageStatus.POS, AuthenticationSuccessMessage.USER_REGISTERED, user.getEmail()));
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userService.getByEmail(request.getEmail());
        var jwtToken = jwtService.generateToken(user);
        log.info(LogMessage.generate(MessageStatus.POS, AuthenticationSuccessMessage.USER_LOGGED_IN, user.getEmail()));
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }
}