package org.erkam.propertylistingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.erkam.propertylistingservice.dto.request.listing.ListingSaveRequest;
import org.erkam.propertylistingservice.dto.request.listing.ListingUpdateRequest;
import org.erkam.propertylistingservice.dto.request.listing.ListingUpdateStatusRequest;
import org.erkam.propertylistingservice.dto.response.GenericResponse;
import org.erkam.propertylistingservice.dto.response.listing.*;
import org.erkam.propertylistingservice.service.ListingService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ListingController.class)
@ContextConfiguration(classes = ListingController.class)
@Import(ListingControllerTest.TestSecurityConfig.class)
class ListingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListingService listingService;

    @Autowired
    private ObjectMapper objectMapper;

    private ListingSaveRequest listingSaveRequest;
    private ListingUpdateRequest listingUpdateRequest;
    private ListingUpdateStatusRequest listingUpdateStatusRequest;
    private ListingGetResponse listingGetResponse;
    private ListingDeleteResponse listingDeleteResponse;
    private ListingUpdateStatusResponse listingUpdateStatusResponse;

    @BeforeEach
    void setUp() {
        listingSaveRequest = Instancio.create(ListingSaveRequest.class);
        listingUpdateRequest = Instancio.create(ListingUpdateRequest.class);
        listingUpdateStatusRequest = Instancio.create(ListingUpdateStatusRequest.class);
        listingGetResponse = Instancio.create(ListingGetResponse.class);
        listingDeleteResponse = Instancio.create(ListingDeleteResponse.class);
        listingUpdateStatusResponse = Instancio.create(ListingUpdateStatusResponse.class);
    }

    @Test
    void save_success() throws Exception {
        // Given
        Mockito.when(listingService.save(any(ListingSaveRequest.class))).thenReturn(
                GenericResponse.success(listingGetResponse)
        );

        // When
        String body = objectMapper.writeValueAsString(listingSaveRequest);
        ResultActions resultActions = mockMvc.perform(post("/api/v1/listings")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(listingService, times(1)).save(any(ListingSaveRequest.class));
    }

    @Test
    void getAll_success() throws Exception {
        Mockito.when(listingService.getAll())
                .thenReturn(GenericResponse.success(List.of(listingGetResponse)));
        ResultActions resultActions = mockMvc.perform(get("/api/v1/listings"));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(listingService, times(1)).getAll();
    }

    @Test
    void getAllActive_success() throws Exception {
        Mockito.when(listingService.getAllActive())
                .thenReturn(GenericResponse.success(List.of(listingGetResponse)));
        ResultActions resultActions = mockMvc.perform(get("/api/v1/listings/active"));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(listingService, times(1)).getAllActive();
    }

    @Test
    void getById_success() throws Exception {
        Mockito.when(listingService.getById(any(Long.class)))
                .thenReturn(GenericResponse.success(listingGetResponse));
        ResultActions resultActions = mockMvc.perform(get("/api/v1/listings/{id}", 1L));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(listingService, times(1)).getById(any(Long.class));
    }

    @Test
    void deleteById_success() throws Exception {
        Mockito.when(listingService.deleteById(any(Long.class), any(HttpServletRequest.class)))
                .thenReturn(GenericResponse.success(listingDeleteResponse));
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/listings/{id}", 1L));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(listingService, times(1)).deleteById(any(Long.class), any(HttpServletRequest.class));
    }

    @Test
    void getListingsByUserId_success() throws Exception {
        Mockito.when(listingService.getListingsByUserId(any(Long.class)))
                .thenReturn(GenericResponse.success(List.of(listingGetResponse)));
        ResultActions resultActions = mockMvc.perform(get("/api/v1/listings/user/{userId}", 1L));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(listingService, times(1)).getListingsByUserId(any(Long.class));
    }

    @Test
    void getActiveListingsOfUser_success() throws Exception {
        Mockito.when(listingService.getActiveListingsByUserId(any(Long.class)))
                .thenReturn(GenericResponse.success(List.of(listingGetResponse)));
        ResultActions resultActions = mockMvc.perform(get("/api/v1/listings/user/{userId}/active", 1L));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(listingService, times(1)).getActiveListingsByUserId(any(Long.class));
    }

    @Test
    void getPassiveListingsOfUser_success() throws Exception {
        Mockito.when(listingService.getPassiveListingsByUserId(any(Long.class)))
                .thenReturn(GenericResponse.success(List.of(listingGetResponse)));
        ResultActions resultActions = mockMvc.perform(get("/api/v1/listings/user/{userId}/passive", 1L));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(listingService, times(1)).getPassiveListingsByUserId(any(Long.class));
    }

    @Test
    void updateTheStatusOfTheListing_success() throws Exception {
        Mockito.when(listingService.changeStatusOfTheListing(any(ListingUpdateStatusRequest.class), any(HttpServletRequest.class)))
                .thenReturn(GenericResponse.success(listingUpdateStatusResponse));

        String body = objectMapper.writeValueAsString(listingUpdateStatusRequest);
        ResultActions resultActions = mockMvc.perform(put("/api/v1/listings/user/status")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(listingService, times(1)).changeStatusOfTheListing(any(ListingUpdateStatusRequest.class), any(HttpServletRequest.class));
    }

    @Test
    void updateTheListing_success() throws Exception {
        Mockito.when(listingService.updateListing(any(Long.class), any(ListingUpdateRequest.class), any(HttpServletRequest.class)))
                .thenReturn(GenericResponse.success(listingGetResponse));

        String body = objectMapper.writeValueAsString(listingUpdateRequest);
        ResultActions resultActions = mockMvc.perform(put("/api/v1/listings/{id}", 1L)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(listingService, times(1)).updateListing(any(Long.class), any(ListingUpdateRequest.class), any(HttpServletRequest.class));
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
