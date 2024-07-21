package org.erkam.propertyuserservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.erkam.propertyuserservice.dto.request.auth.AuthenticationRequest;
import org.erkam.propertyuserservice.dto.request.auth.RegisterRequest;
import org.erkam.propertyuserservice.dto.response.auth.AuthenticationResponse;
import org.erkam.propertyuserservice.service.AuthenticationService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@ContextConfiguration(classes = AuthController.class)
@Import(AuthControllerTest.TestSecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequest registerRequest;
    private AuthenticationRequest authenticationRequest;
    private AuthenticationResponse authenticationResponse;

    @BeforeEach
    void setUp() {
        registerRequest = Instancio.create(RegisterRequest.class);
        authenticationRequest = Instancio.create(AuthenticationRequest.class);
        authenticationResponse = Instancio.create(AuthenticationResponse.class);
    }

    @Test
    void register_success() throws Exception {
        // Given
        Mockito.when(authenticationService.register(any(RegisterRequest.class))).thenReturn(authenticationResponse);

        // When
        String body = objectMapper.writeValueAsString(registerRequest);
        ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/register")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(authenticationResponse.getToken()));
        verify(authenticationService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    void login_success() throws Exception {
        // Given
        Mockito.when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(authenticationResponse);

        // When
        String body = objectMapper.writeValueAsString(authenticationRequest);
        ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/login")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(authenticationResponse.getToken()));
        verify(authenticationService, times(1)).authenticate(any(AuthenticationRequest.class));
    }

    @Configuration
    @EnableWebSecurity
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable()) // Disable CSRF protection for tests
                    .authorizeHttpRequests(authz -> authz
                            .anyRequest().permitAll()
                    )
                    .httpBasic(withDefaults());
            return http.build();
        }
    }
}
