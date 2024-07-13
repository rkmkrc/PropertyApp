package org.erkam.propertylistingservice.service;

import org.erkam.propertylistingservice.constants.PropertyAppConstants;
import org.erkam.propertylistingservice.dto.converter.ListingConverter;
import org.erkam.propertylistingservice.dto.request.listing.ListingSaveRequest;
import org.erkam.propertylistingservice.dto.response.GenericResponse;
import org.erkam.propertylistingservice.dto.response.listing.ListingDeleteResponse;
import org.erkam.propertylistingservice.dto.response.listing.ListingGetResponse;
import org.erkam.propertylistingservice.dto.response.listing.ListingSaveResponse;
import org.erkam.propertylistingservice.exception.listing.ListingException;
import org.erkam.propertylistingservice.exception.listing.ListingExceptionMessage;
import org.erkam.propertylistingservice.model.Listing;
import org.erkam.propertylistingservice.repository.ListingRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListingServiceTest {

    @Mock
    private ListingRepository listingRepository;

    @InjectMocks
    private ListingService listingService;

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        request.setAttribute("userId", 1L);
    }

    @Test
    void save_success() {
        // Given
        ListingSaveRequest request = Instancio.of(ListingSaveRequest.class).create();

        when(listingRepository.save(any(Listing.class))).thenReturn(new Listing());

        // When
        GenericResponse<ListingSaveResponse> response = listingService.save(request);

        // Then
        verify(listingRepository, times(1)).save(any(Listing.class));
        assertNotNull(response);
        assertTrue(response.getMessage().equals(PropertyAppConstants.SUCCESS));
    }

    @Test
    void save_shouldThrowException_whenListingAlreadyExists() {
        // Given
        ListingSaveRequest request = Instancio.of(ListingSaveRequest.class).create();
        Listing listing = ListingConverter.toListing(request);

        // Mocking repository to return an Optional containing the listing object
        when(listingRepository.findByTitleAndPriceAndAreaAndDescriptionAndTypeAndStatus(
                listing.getTitle(),
                listing.getPrice(),
                listing.getArea(),
                listing.getDescription(),
                listing.getType(),
                listing.getStatus()
        )).thenReturn(Optional.of(listing));

        // When
        ListingException.DuplicateListingException exception = assertThrows(ListingException.DuplicateListingException.class, () -> {
            listingService.save(request);
        });

        // Then
        assertEquals(ListingExceptionMessage.DUPLICATE_LISTING + " with title: " + request.getTitle(), exception.getMessage());
        verify(listingRepository, times(0)).save(any(Listing.class));
    }

    @Test
    void getAll_success() {
        // Given
        List<Listing> listings = new ArrayList<>();
        listings.add(new Listing());

        when(listingRepository.findAll()).thenReturn(listings);

        // When
        GenericResponse<List<ListingGetResponse>> response = listingService.getAll();

        // Then
        verify(listingRepository, times(1)).findAll();
        assertNotNull(response);
        assertTrue(response.getMessage().equals(PropertyAppConstants.SUCCESS));
        assertFalse(response.getData().isEmpty());
    }

    @Test
    void getAll_shouldThrowException_whenNoDataExists() {
        when(listingRepository.findAll()).thenReturn(new ArrayList<>());

        // When
        ListingException.NoDataOnDatabaseException exception = assertThrows(ListingException.NoDataOnDatabaseException.class, () -> {
            listingService.getAll();
        });

        // Then
        assertEquals(ListingExceptionMessage.NO_DATA_ON_DATABASE, exception.getMessage());
        verify(listingRepository, times(1)).findAll();
    }

    @Test
    void getById_success() {
        // Given
        Long listingId = 1L;
        Listing listing = Instancio.of(Listing.class).create();

        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));

        // When
        GenericResponse<ListingGetResponse> response = listingService.getById(listingId);

        // Then
        verify(listingRepository, times(1)).findById(listingId);
        assertNotNull(response);
        assertTrue(response.getMessage().equals(PropertyAppConstants.SUCCESS));
    }

    @Test
    void getById_shouldThrowException_whenListingNotFound() {
        // Given
        Long listingId = 1L;
        when(listingRepository.findById(listingId)).thenReturn(Optional.empty());

        // When
        ListingException.ListingNotFoundException exception = assertThrows(ListingException.ListingNotFoundException.class, () -> {
            listingService.getById(listingId);
        });

        // Then
        assertEquals(ListingExceptionMessage.LISTING_NOT_FOUND + " with id: " + listingId, exception.getMessage());
        verify(listingRepository, times(1)).findById(listingId);
    }

    @Test
    void deleteById_success() {
        // Given
        Long listingId = 1L;
        Listing listing = Instancio.of(Listing.class).create();

        when(listingRepository.findByIdAndUserId(listingId, 1L)).thenReturn(Optional.of(listing));
        doNothing().when(listingRepository).delete(listing);

        // When
        GenericResponse<ListingDeleteResponse> response = listingService.deleteById(listingId, request);

        // Then
        verify(listingRepository, times(1)).findByIdAndUserId(listingId, 1L);
        verify(listingRepository, times(1)).delete(listing);
        assertNotNull(response);
        assertTrue(response.getMessage().equals(PropertyAppConstants.SUCCESS));
    }

    @Test
    void deleteById_shouldThrowException_whenListingNotFound() {
        // Given
        Long listingId = 1L;

        when(listingRepository.findByIdAndUserId(listingId, 1L)).thenReturn(Optional.empty());

        // When
        ListingException.ListingNotFoundOrYouDontHavePermissionException exception = assertThrows(ListingException.ListingNotFoundOrYouDontHavePermissionException.class, () -> {
            listingService.deleteById(listingId, request);
        });

        // Then
        assertEquals(ListingExceptionMessage.LISTING_NOT_FOUND_OR_YOU_DONT_HAVE_PERMISSION , exception.getMessage());
        verify(listingRepository, times(1)).findByIdAndUserId(listingId, 1L);
    }
}
