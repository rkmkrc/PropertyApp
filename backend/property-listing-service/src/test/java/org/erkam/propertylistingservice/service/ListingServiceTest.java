package org.erkam.propertylistingservice.service;

import jakarta.servlet.http.HttpServletRequest;
import org.erkam.propertylistingservice.constants.ListingSuccessMessage;
import org.erkam.propertylistingservice.constants.PropertyAppConstants;
import org.erkam.propertylistingservice.constants.enums.MessageStatus;
import org.erkam.propertylistingservice.dto.converter.ListingConverter;
import org.erkam.propertylistingservice.dto.request.listing.ListingSaveRequest;
import org.erkam.propertylistingservice.dto.request.listing.ListingUpdateRequest;
import org.erkam.propertylistingservice.dto.request.listing.ListingUpdateStatusRequest;
import org.erkam.propertylistingservice.dto.response.GenericResponse;
import org.erkam.propertylistingservice.dto.response.listing.*;
import org.erkam.propertylistingservice.exception.listing.ListingException;
import org.erkam.propertylistingservice.model.Listing;
import org.erkam.propertylistingservice.model.enums.ListingStatus;
import org.erkam.propertylistingservice.model.enums.PropertyType;
import org.erkam.propertylistingservice.producer.ListingReviewProducer;
import org.erkam.propertylistingservice.producer.dto.ListingDto;
import org.erkam.propertylistingservice.repository.ListingRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ListingServiceTest {

    private ListingService listingService;
    private ListingRepository listingRepository;
    private ListingReviewProducer listingReviewProducer;

    private ListingSaveRequest listingSaveRequest;
    private ListingUpdateRequest listingUpdateRequest;
    private ListingUpdateStatusRequest listingUpdateStatusRequest;
    private Listing listing;
    private HttpServletRequest httpRequest;

    @BeforeEach
    void setUp() {
        listingRepository = mock(ListingRepository.class);
        listingReviewProducer = mock(ListingReviewProducer.class);
        listingService = new ListingService(listingRepository, listingReviewProducer);

        listingSaveRequest = Instancio.create(ListingSaveRequest.class);
        listingUpdateRequest = Instancio.create(ListingUpdateRequest.class);
        listingUpdateStatusRequest = Instancio.create(ListingUpdateStatusRequest.class);
        listing = Instancio.create(Listing.class);
        httpRequest = mock(HttpServletRequest.class);
    }

    @Test
    void save_success() {
        // Given
        when(listingRepository.save(any(Listing.class))).thenReturn(listing);
        when(listingRepository.findByTitleAndPriceAndAreaAndDescriptionAndType(
                anyString(), any(BigDecimal.class), anyInt(), anyString(), any(PropertyType.class)))
                .thenReturn(Optional.empty());

        // When
        GenericResponse<ListingGetResponse> response = listingService.save(listingSaveRequest);

        // Then
        assertNotNull(response);
        assertEquals(PropertyAppConstants.SUCCESS, response.getMessage());
        verify(listingRepository, times(1)).save(any(Listing.class));
        verify(listingReviewProducer, times(1)).sendNotification(any(ListingDto.class));
    }

    @Test
    void save_duplicateListing_throwsException() {
        // Given
        when(listingRepository.findByTitleAndPriceAndAreaAndDescriptionAndType(
                anyString(), any(BigDecimal.class), anyInt(), anyString(), any(PropertyType.class)))
                .thenReturn(Optional.of(listing));

        // When / Then
        assertThrows(ListingException.DuplicateListingException.class, () -> listingService.save(listingSaveRequest));
    }

    @Test
    void updateListing_success() {
        // Given
        when(httpRequest.getAttribute("userId")).thenReturn(1L);
        when(listingRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(listing));
        when(listingRepository.save(any(Listing.class))).thenReturn(listing);

        // When
        GenericResponse<ListingGetResponse> response = listingService.updateListing(1L, listingUpdateRequest, httpRequest);

        // Then
        assertNotNull(response);
        assertEquals(PropertyAppConstants.SUCCESS, response.getMessage());
        verify(listingRepository, times(1)).save(any(Listing.class));
    }

    @Test
    void updateListing_listingNotFound_throwsException() {
        // Given
        when(httpRequest.getAttribute("userId")).thenReturn(1L);
        when(listingRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ListingException.ListingNotFoundOrYouDontHavePermissionException.class,
                () -> listingService.updateListing(1L, listingUpdateRequest, httpRequest));
    }

    @Test
    void getAll_success() {
        // Given
        when(listingRepository.findAll()).thenReturn(List.of(listing));

        // When
        GenericResponse<List<ListingGetResponse>> response = listingService.getAll();

        // Then
        assertNotNull(response);
        assertEquals(PropertyAppConstants.SUCCESS, response.getMessage());
        verify(listingRepository, times(1)).findAll();
    }

    @Test
    void getAll_noDataOnDatabase_throwsException() {
        // Given
        when(listingRepository.findAll()).thenReturn(List.of());

        // When / Then
        assertThrows(ListingException.NoDataOnDatabaseException.class, () -> listingService.getAll());
    }

    @Test
    void getById_success() {
        // Given
        when(listingRepository.findById(anyLong())).thenReturn(Optional.of(listing));

        // When
        GenericResponse<ListingGetResponse> response = listingService.getById(1L);

        // Then
        assertNotNull(response);
        assertEquals(PropertyAppConstants.SUCCESS, response.getMessage());
        verify(listingRepository, times(1)).findById(anyLong());
    }

    @Test
    void getById_listingNotFound_throwsException() {
        // Given
        when(listingRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ListingException.ListingNotFoundException.class, () -> listingService.getById(1L));
    }

    @Test
    void deleteById_success() {
        // Given
        when(httpRequest.getAttribute("userId")).thenReturn(1L);
        when(listingRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(listing));

        // When
        GenericResponse<ListingDeleteResponse> response = listingService.deleteById(1L, httpRequest);

        // Then
        assertNotNull(response);
        assertEquals(PropertyAppConstants.SUCCESS, response.getMessage());
        verify(listingRepository, times(1)).delete(any(Listing.class));
    }

    @Test
    void deleteById_listingNotFound_throwsException() {
        // Given
        when(httpRequest.getAttribute("userId")).thenReturn(1L);
        when(listingRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ListingException.ListingNotFoundOrYouDontHavePermissionException.class,
                () -> listingService.deleteById(1L, httpRequest));
    }

    @Test
    void getListingsByUserId_success() {
        // Given
        when(listingRepository.findListingsByUserId(anyLong())).thenReturn(List.of(listing));

        // When
        GenericResponse<List<ListingGetResponse>> response = listingService.getListingsByUserId(1L);

        // Then
        assertNotNull(response);
        assertEquals(PropertyAppConstants.SUCCESS, response.getMessage());
        verify(listingRepository, times(1)).findListingsByUserId(anyLong());
    }

    @Test
    void getListingsByUserId_noListingsFound_throwsException() {
        // Given
        when(listingRepository.findListingsByUserId(anyLong())).thenReturn(List.of());

        // When / Then
        assertThrows(ListingException.NoDataOnDatabaseException.class, () -> listingService.getListingsByUserId(1L));
    }

    @Test
    void getActiveListingsByUserId_success() {
        // Given
        when(listingRepository.findListingsByUserIdAndStatus(anyLong(), any(ListingStatus.class)))
                .thenReturn(List.of(listing));

        // When
        GenericResponse<List<ListingGetResponse>> response = listingService.getActiveListingsByUserId(1L);

        // Then
        assertNotNull(response);
        assertEquals(PropertyAppConstants.SUCCESS, response.getMessage());
        verify(listingRepository, times(1)).findListingsByUserIdAndStatus(anyLong(), eq(ListingStatus.ACTIVE));
    }

    @Test
    void getActiveListingsByUserId_noListingsFound_throwsException() {
        // Given
        when(listingRepository.findListingsByUserIdAndStatus(anyLong(), eq(ListingStatus.ACTIVE)))
                .thenReturn(List.of());

        // When / Then
        assertThrows(ListingException.NoActiveListingsFoundForThisUserException.class, () -> listingService.getActiveListingsByUserId(1L));
    }

    @Test
    void getPassiveListingsByUserId_success() {
        // Given
        when(listingRepository.findListingsByUserIdAndStatus(anyLong(), any(ListingStatus.class)))
                .thenReturn(List.of(listing));

        // When
        GenericResponse<List<ListingGetResponse>> response = listingService.getPassiveListingsByUserId(1L);

        // Then
        assertNotNull(response);
        assertEquals(PropertyAppConstants.SUCCESS, response.getMessage());
        verify(listingRepository, times(1)).findListingsByUserIdAndStatus(anyLong(), eq(ListingStatus.PASSIVE));
    }

    @Test
    void getPassiveListingsByUserId_noListingsFound_throwsException() {
        // Given
        when(listingRepository.findListingsByUserIdAndStatus(anyLong(), eq(ListingStatus.PASSIVE)))
                .thenReturn(List.of());

        // When / Then
        assertThrows(ListingException.NoPassiveListingsFoundForThisUserException.class, () -> listingService.getPassiveListingsByUserId(1L));
    }

    @Test
    void changeStatusOfTheListing_success() {
        // Given
        when(httpRequest.getAttribute("userId")).thenReturn(1L);
        when(listingRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(listing));
        when(listingRepository.save(any(Listing.class))).thenReturn(listing);

        // When
        GenericResponse<ListingUpdateStatusResponse> response = listingService.changeStatusOfTheListing(listingUpdateStatusRequest, httpRequest);

        // Then
        assertNotNull(response);
        assertEquals(PropertyAppConstants.SUCCESS, response.getMessage());
        verify(listingRepository, times(1)).save(any(Listing.class));
    }

    @Test
    void changeStatusOfTheListing_listingNotFound_throwsException() {
        // Given
        when(httpRequest.getAttribute("userId")).thenReturn(1L);
        when(listingRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ListingException.ListingNotFoundOrYouDontHavePermissionException.class,
                () -> listingService.changeStatusOfTheListing(listingUpdateStatusRequest, httpRequest));
    }

    @Test
    void getAllActive_success() {
        // Given
        when(listingRepository.findByStatus(any(ListingStatus.class))).thenReturn(List.of(listing));

        // When
        GenericResponse<List<ListingGetResponse>> response = listingService.getAllActive();

        // Then
        assertNotNull(response);
        assertEquals(PropertyAppConstants.SUCCESS, response.getMessage());
        verify(listingRepository, times(1)).findByStatus(eq(ListingStatus.ACTIVE));
    }

    @Test
    void getAllActive_noListingsFound_throwsException() {
        // Given
        when(listingRepository.findByStatus(eq(ListingStatus.ACTIVE))).thenReturn(List.of());

        // When / Then
        assertThrows(ListingException.NoDataOnDatabaseException.class, () -> listingService.getAllActive());
    }
}
