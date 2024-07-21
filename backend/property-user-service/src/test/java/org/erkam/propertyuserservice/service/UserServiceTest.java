package org.erkam.propertyuserservice.service;

import jakarta.servlet.http.HttpServletRequest;
import org.erkam.propertyuserservice.client.listing.dto.request.ListingSaveRequest;
import org.erkam.propertyuserservice.client.listing.dto.request.ListingUpdateRequest;
import org.erkam.propertyuserservice.client.listing.dto.request.ListingUpdateStatusRequest;
import org.erkam.propertyuserservice.client.listing.dto.response.ListingDeleteResponse;
import org.erkam.propertyuserservice.client.listing.dto.response.ListingUpdateStatusResponse;
import org.erkam.propertyuserservice.client.listing.service.ListingService;
import org.erkam.propertyuserservice.client.payment.dto.request.PaymentRequest;
import org.erkam.propertyuserservice.client.payment.dto.response.PaymentResponse;
import org.erkam.propertyuserservice.client.payment.service.PaymentService;
import org.erkam.propertyuserservice.dto.request.auth.RegisterRequest;
import org.erkam.propertyuserservice.dto.request.user.BuyPackageRequest;
import org.erkam.propertyuserservice.dto.request.user.UserSaveRequest;
import org.erkam.propertyuserservice.dto.response.GenericResponse;
import org.erkam.propertyuserservice.dto.response.listing.ListingGetResponse;
import org.erkam.propertyuserservice.dto.response.product.BuyPackageResponse;
import org.erkam.propertyuserservice.dto.response.product.PackageGetResponse;
import org.erkam.propertyuserservice.dto.response.user.UserDeleteResponse;
import org.erkam.propertyuserservice.dto.response.user.UserGetResponse;
import org.erkam.propertyuserservice.dto.response.user.UserSaveResponse;
import org.erkam.propertyuserservice.exception.user.UserException;
import org.erkam.propertyuserservice.model.User;
import org.erkam.propertyuserservice.model.enums.ListingStatus;
import org.erkam.propertyuserservice.model.enums.PropertyType;
import org.erkam.propertyuserservice.repository.UserRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;
    private ListingService listingService;
    private PaymentService paymentService;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        listingService = mock(ListingService.class);
        paymentService = mock(PaymentService.class);
        userService = new UserService(userRepository, listingService, paymentService);
        authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void register_success() {
        // Given
        RegisterRequest registerRequest = Instancio.create(RegisterRequest.class);
        User user = Instancio.create(User.class);

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User registeredUser = userService.register(registerRequest);

        // Then
        assertEquals(registerRequest.getEmail(), registeredUser.getEmail());
        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void save_success() {
        // Given
        UserSaveRequest userSaveRequest = Instancio.create(UserSaveRequest.class);
        UserSaveResponse userSaveResponse = UserSaveResponse.of(userSaveRequest);

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // When
        GenericResponse<UserSaveResponse> response = userService.save(userSaveRequest);

        // Then
        assertEquals(userSaveResponse.getResponseMessage(), response.getData().getResponseMessage());
        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getAll_success() {
        // Given
        List<User> users = Instancio.createList(User.class);

        when(userRepository.findAll()).thenReturn(users);

        // When
        GenericResponse<List<UserGetResponse>> response = userService.getAll();

        // Then
        assertFalse(response.getData().isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getById_success() {
        // Given
        User user = Instancio.create(User.class);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // When
        GenericResponse<UserGetResponse> response = userService.getById(user.getId());

        // Then
        assertEquals(user.getEmail(), response.getData().getEmail());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void deleteById_success() {
        // Given
        User user = Instancio.create(User.class);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // When
        GenericResponse<UserDeleteResponse> response = userService.deleteById(user.getId());

        // Then
        assertEquals("User deleted successfully. Name: " + user.getName()
                + ", Surname: " + user.getSurname()
                + ", Email: " + user.getEmail(), response.getData().getResponseMessage());
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    void addListing_success() {
        // Given
        ListingSaveRequest listingSaveRequest = Instancio.create(ListingSaveRequest.class);
        ListingGetResponse listingGetResponse = Instancio.create(ListingGetResponse.class);
        User user = Instancio.create(User.class);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(listingService.addListing(any(ListingSaveRequest.class))).thenReturn(listingGetResponse);

        // When
        GenericResponse<ListingGetResponse> response = userService.addListing(listingSaveRequest);

        // Then
        assertEquals(listingGetResponse.getTitle(), response.getData().getTitle());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(listingService, times(1)).addListing(any(ListingSaveRequest.class));
    }

    @Test
    void buyPackage_success() {
        // Given
        BuyPackageRequest buyPackageRequest = Instancio.create(BuyPackageRequest.class);
        BuyPackageResponse buyPackageResponse = Instancio.create(BuyPackageResponse.class);
        User user = Instancio.create(User.class);
        PaymentResponse paymentResponse = Instancio.create(PaymentResponse.class);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(paymentService.receivePayment(any(PaymentRequest.class))).thenReturn(paymentResponse);

        // When
        GenericResponse<BuyPackageResponse> response = userService.buyPackage(buyPackageRequest);

        // Then
        assertNotNull(response.getData());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(paymentService, times(1)).receivePayment(any(PaymentRequest.class));
    }

    @Test
    void getAllPackages_success() {
        // Given
        User user = Instancio.create(User.class);
        PackageGetResponse packageGetResponse = Instancio.create(PackageGetResponse.class);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // When
        GenericResponse<List<PackageGetResponse>> response = userService.getAllPackages();

        // Then
        assertFalse(response.getData().isEmpty());
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    void getAllListings_success() {
        // Given
        User user = Instancio.create(User.class);
        ListingGetResponse listingGetResponse = Instancio.create(ListingGetResponse.class);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(listingService.getAllListingsOfUser(anyLong())).thenReturn(List.of(listingGetResponse));

        // When
        GenericResponse<List<ListingGetResponse>> response = userService.getAllListings();

        // Then
        assertFalse(response.getData().isEmpty());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(listingService, times(1)).getAllListingsOfUser(anyLong());
    }

    @Test
    void getPassiveListings_success() {
        // Given
        User user = Instancio.create(User.class);
        ListingGetResponse listingGetResponse = Instancio.create(ListingGetResponse.class);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(listingService.getPassiveListingsOfUser(anyLong())).thenReturn(List.of(listingGetResponse));

        // When
        GenericResponse<List<ListingGetResponse>> response = userService.getPassiveListings();

        // Then
        assertFalse(response.getData().isEmpty());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(listingService, times(1)).getPassiveListingsOfUser(anyLong());
    }

    @Test
    void getActiveListings_success() {
        // Given
        User user = Instancio.create(User.class);
        ListingGetResponse listingGetResponse = Instancio.create(ListingGetResponse.class);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(listingService.getActiveListingsOfUser(anyLong())).thenReturn(List.of(listingGetResponse));

        // When
        GenericResponse<List<ListingGetResponse>> response = userService.getActiveListings();

        // Then
        assertFalse(response.getData().isEmpty());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(listingService, times(1)).getActiveListingsOfUser(anyLong());
    }

    @Test
    void deleteListingById_success() {
        // Given
        User user = Instancio.create(User.class);
        ListingDeleteResponse listingDeleteResponse = Instancio.create(ListingDeleteResponse.class);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(listingService.deleteListing(anyLong())).thenReturn(listingDeleteResponse);

        // When
        GenericResponse<ListingDeleteResponse> response = userService.deleteListingById(1L);

        // Then
        assertNotNull(response.getData());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(listingService, times(1)).deleteListing(anyLong());
    }

    @Test
    void updateTheStatusOfTheListing_success() {
        // Given
        ListingUpdateStatusRequest listingUpdateStatusRequest = Instancio.create(ListingUpdateStatusRequest.class);
        ListingUpdateStatusResponse listingUpdateStatusResponse = Instancio.create(ListingUpdateStatusResponse.class);
        User user = Instancio.create(User.class);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(listingService.updateTheStatusOfTheListing(any(ListingUpdateStatusRequest.class))).thenReturn(listingUpdateStatusResponse);

        // When
        GenericResponse<ListingUpdateStatusResponse> response = userService.updateTheStatusOfTheListing(listingUpdateStatusRequest);

        // Then
        assertNotNull(response.getData());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(listingService, times(1)).updateTheStatusOfTheListing(any(ListingUpdateStatusRequest.class));
    }

    @Test
    void updateTheListing_success() {
        // Given
        ListingUpdateRequest listingUpdateRequest = ListingUpdateRequest.builder()
                .area(1)
                .description("Description")
                .price(BigDecimal.ONE)
                .status(ListingStatus.ACTIVE)
                .type(PropertyType.FLAT)
                .build();
        ListingGetResponse listingGetResponse = Instancio.create(ListingGetResponse.class);
        User user = Instancio.create(User.class);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(listingService.updateTheListing(anyLong(), any(ListingUpdateRequest.class))).thenReturn(listingGetResponse);

        // When
        GenericResponse<ListingGetResponse> response = userService.updateTheListing(1L, listingUpdateRequest);

        // Then
        assertNotNull(response.getData());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(listingService, times(1)).updateTheListing(anyLong(), any(ListingUpdateRequest.class));
    }

    @Test
    void getCurrentUser_success() {
        // Given
        User user = Instancio.create(User.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getAttribute("userId")).thenReturn(user.getId());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // When
        GenericResponse<UserGetResponse> response = userService.getCurrentUser(request);

        // Then
        assertEquals(user.getEmail(), response.getData().getEmail());
        verify(userRepository, times(1)).findById(anyLong());
    }
}
