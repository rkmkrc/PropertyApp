package org.erkam.propertyapp.service;

import org.erkam.propertyapp.constants.PropertyAppConstants;
import org.erkam.propertyapp.dto.converter.ListingConverter;
import org.erkam.propertyapp.dto.request.listing.ListingSaveRequest;
import org.erkam.propertyapp.dto.response.GenericResponse;
import org.erkam.propertyapp.dto.response.listing.ListingDeleteResponse;
import org.erkam.propertyapp.dto.response.listing.ListingGetResponse;
import org.erkam.propertyapp.dto.response.listing.ListingSaveResponse;
import org.erkam.propertyapp.exception.listing.ListingException;
import org.erkam.propertyapp.exception.listing.ListingExceptionMessage;
import org.erkam.propertyapp.model.Listing;
import org.erkam.propertyapp.repository.ListingRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));
        doNothing().when(listingRepository).delete(listing);

        // When
        GenericResponse<ListingDeleteResponse> response = listingService.deleteById(listingId);

        // Then
        verify(listingRepository, times(1)).findById(listingId);
        verify(listingRepository, times(1)).delete(listing);
        assertNotNull(response);
        assertTrue(response.getMessage().equals(PropertyAppConstants.SUCCESS));
    }

    @Test
    void deleteById_shouldThrowException_whenListingNotFound() {
        // Given
        Long listingId = 1L;

        when(listingRepository.findById(listingId)).thenReturn(Optional.empty());

        // When
        ListingException.ListingNotFoundException exception = assertThrows(ListingException.ListingNotFoundException.class, () -> {
            listingService.deleteById(listingId);
        });

        // Then
        assertEquals(ListingExceptionMessage.LISTING_NOT_FOUND + " with id: " + listingId, exception.getMessage());
        verify(listingRepository, times(1)).findById(listingId);
    }
}
