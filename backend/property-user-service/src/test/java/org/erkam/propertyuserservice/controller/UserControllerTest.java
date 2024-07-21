package org.erkam.propertyuserservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.erkam.propertyuserservice.client.listing.dto.request.ListingSaveRequest;
import org.erkam.propertyuserservice.client.listing.dto.request.ListingUpdateRequest;
import org.erkam.propertyuserservice.client.listing.dto.request.ListingUpdateStatusRequest;
import org.erkam.propertyuserservice.client.listing.dto.response.ListingDeleteResponse;
import org.erkam.propertyuserservice.client.listing.dto.response.ListingUpdateStatusResponse;
import org.erkam.propertyuserservice.dto.request.user.BuyPackageRequest;
import org.erkam.propertyuserservice.dto.request.user.UserSaveRequest;
import org.erkam.propertyuserservice.dto.response.GenericResponse;
import org.erkam.propertyuserservice.dto.response.listing.ListingGetResponse;
import org.erkam.propertyuserservice.dto.response.product.BuyPackageResponse;
import org.erkam.propertyuserservice.dto.response.product.PackageGetResponse;
import org.erkam.propertyuserservice.dto.response.user.UserDeleteResponse;
import org.erkam.propertyuserservice.dto.response.user.UserGetResponse;
import org.erkam.propertyuserservice.dto.response.user.UserSaveResponse;
import org.erkam.propertyuserservice.service.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = UserController.class)
@Import(UserControllerTest.TestSecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserSaveRequest userSaveRequest;
    private UserSaveResponse userSaveResponse;
    private UserGetResponse userGetResponse;
    private ListingSaveRequest listingSaveRequest;
    private ListingGetResponse listingGetResponse;
    private BuyPackageRequest buyPackageRequest;
    private BuyPackageResponse buyPackageResponse;
    private PackageGetResponse packageGetResponse;
    private ListingDeleteResponse listingDeleteResponse;
    private ListingUpdateStatusRequest listingUpdateStatusRequest;
    private ListingUpdateStatusResponse listingUpdateStatusResponse;
    private ListingUpdateRequest listingUpdateRequest;

    @BeforeEach
    void setUp() {
        userSaveRequest = Instancio.create(UserSaveRequest.class);
        userSaveResponse = UserSaveResponse.of(userSaveRequest);
        userGetResponse = Instancio.create(UserGetResponse.class);
        listingSaveRequest = Instancio.create(ListingSaveRequest.class);
        listingGetResponse = Instancio.create(ListingGetResponse.class);
        buyPackageRequest = Instancio.create(BuyPackageRequest.class);
        buyPackageResponse = Instancio.create(BuyPackageResponse.class);
        packageGetResponse = Instancio.create(PackageGetResponse.class);
        listingDeleteResponse = Instancio.create(ListingDeleteResponse.class);
        listingUpdateStatusRequest = Instancio.create(ListingUpdateStatusRequest.class);
        listingUpdateStatusResponse = Instancio.create(ListingUpdateStatusResponse.class);
        listingUpdateRequest = Instancio.create(ListingUpdateRequest.class);
    }

    @Test
    void save_success() throws Exception {
        // Given
        Mockito.when(userService.save(any(UserSaveRequest.class))).thenReturn(
                GenericResponse.success(userSaveResponse)
        );

        // When
        String body = objectMapper.writeValueAsString(userSaveRequest);
        ResultActions resultActions = mockMvc.perform(post("/api/v1/users")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.responseMessage").value(userSaveResponse.getResponseMessage()));
        verify(userService, times(1)).save(any(UserSaveRequest.class));
    }

    @Test
    void getAll_success() throws Exception {
        Mockito.when(userService.getAll())
                .thenReturn(GenericResponse.success(List.of(userGetResponse)));
        ResultActions resultActions = mockMvc.perform(get("/api/v1/users"));

        resultActions.andExpect(status().isOk());
        verify(userService, times(1)).getAll();
    }

    @Test
    void getCurrentUser_success() throws Exception {
        Mockito.when(userService.getCurrentUser(any(HttpServletRequest.class)))
                .thenReturn(GenericResponse.success(userGetResponse));
        ResultActions resultActions = mockMvc.perform(get("/api/v1/users/user"));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(userService, times(1)).getCurrentUser(any(HttpServletRequest.class));
    }

    @Test
    void getById_success() throws Exception {
        Mockito.when(userService.getById(any(Long.class)))
                .thenReturn(GenericResponse.success(userGetResponse));
        ResultActions resultActions = mockMvc.perform(get("/api/v1/users/{id}", 1L));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(userService, times(1)).getById(any(Long.class));
    }

    @Test
    void deleteById_success() throws Exception {
        GenericResponse<UserDeleteResponse> deleteResponse = GenericResponse.success(Instancio.create(UserDeleteResponse.class));
        Mockito.when(userService.deleteById(any(Long.class)))
                .thenReturn(deleteResponse);
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/users/{id}", 1L));

        resultActions.andExpect(status().isOk());
        verify(userService, times(1)).deleteById(any(Long.class));
    }

    @Test
    void addListing_success() throws Exception {
        Mockito.when(userService.addListing(any(ListingSaveRequest.class)))
                .thenReturn(GenericResponse.success(listingGetResponse));

        String body = objectMapper.writeValueAsString(listingSaveRequest);
        ResultActions resultActions = mockMvc.perform(post("/api/v1/users/listings")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(userService, times(1)).addListing(any(ListingSaveRequest.class));
    }

    @Test
    void buyPackage_success() throws Exception {
        Mockito.when(userService.buyPackage(any(BuyPackageRequest.class)))
                .thenReturn(GenericResponse.success(buyPackageResponse));

        String body = objectMapper.writeValueAsString(buyPackageRequest);
        ResultActions resultActions = mockMvc.perform(post("/api/v1/users/packages")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(userService, times(1)).buyPackage(any(BuyPackageRequest.class));
    }

    @Test
    void getPackages_success() throws Exception {
        Mockito.when(userService.getAllPackages())
                .thenReturn(GenericResponse.success(List.of(packageGetResponse)));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/users/packages"));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(userService, times(1)).getAllPackages();
    }

    @Test
    void getAllListings_success() throws Exception {
        Mockito.when(userService.getAllListings())
                .thenReturn(GenericResponse.success(List.of(listingGetResponse)));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/users/listings"));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(userService, times(1)).getAllListings();
    }

    @Test
    void getPassiveListings_success() throws Exception {
        Mockito.when(userService.getPassiveListings())
                .thenReturn(GenericResponse.success(List.of(listingGetResponse)));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/users/listings/passive"));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(userService, times(1)).getPassiveListings();
    }

    @Test
    void getActiveListings_success() throws Exception {
        Mockito.when(userService.getActiveListings())
                .thenReturn(GenericResponse.success(List.of(listingGetResponse)));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/users/listings/active"));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(userService, times(1)).getActiveListings();
    }

    @Test
    void deleteListingById_success() throws Exception {
        Mockito.when(userService.deleteListingById(any(Long.class)))
                .thenReturn(GenericResponse.success(listingDeleteResponse));

        ResultActions resultActions = mockMvc.perform(delete("/api/v1/users/listings/{id}", 1L));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(userService, times(1)).deleteListingById(any(Long.class));
    }

    @Test
    void updateStatusOfAListing_success() throws Exception {
        Mockito.when(userService.updateTheStatusOfTheListing(any(ListingUpdateStatusRequest.class)))
                .thenReturn(GenericResponse.success(listingUpdateStatusResponse));

        String body = objectMapper.writeValueAsString(listingUpdateStatusRequest);
        ResultActions resultActions = mockMvc.perform(put("/api/v1/users/listings/status")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(userService, times(1)).updateTheStatusOfTheListing(any(ListingUpdateStatusRequest.class));
    }

    @Test
    void updateTheListing_success() throws Exception {
        Mockito.when(userService.updateTheListing(any(Long.class), any(ListingUpdateRequest.class)))
                .thenReturn(GenericResponse.success(listingGetResponse));

        String body = objectMapper.writeValueAsString(listingUpdateRequest);
        ResultActions resultActions = mockMvc.perform(put("/api/v1/users/listings/{id}", 1L)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
        verify(userService, times(1)).updateTheListing(any(Long.class), any(ListingUpdateRequest.class));
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
