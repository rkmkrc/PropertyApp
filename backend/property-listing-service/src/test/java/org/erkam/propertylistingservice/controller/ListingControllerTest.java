package org.erkam.propertylistingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.erkam.propertylistingservice.dto.request.listing.ListingSaveRequest;
import org.erkam.propertylistingservice.dto.response.GenericResponse;
import org.erkam.propertylistingservice.dto.response.listing.ListingDeleteResponse;
import org.erkam.propertylistingservice.dto.response.listing.ListingGetResponse;
import org.erkam.propertylistingservice.dto.response.listing.ListingSaveResponse;
import org.erkam.propertylistingservice.model.Listing;
import org.erkam.propertylistingservice.model.enums.PropertyType;
import org.erkam.propertylistingservice.service.ListingService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ListingController.class)
class ListingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListingService listingService;

    @Autowired
    private ObjectMapper objectMapper;

    private ListingSaveRequest listingSaveRequest;
    private Listing listing;
    private ListingSaveResponse listingSaveResponse;
    private ListingGetResponse listingGetResponse;
    private ListingDeleteResponse listingDeleteResponse;
    private GenericResponse<ListingSaveResponse> saveResponse;
    private GenericResponse<List<ListingGetResponse>> getAllResponse;
    private GenericResponse<ListingGetResponse> getByIdResponse;
    private GenericResponse<ListingDeleteResponse> deleteResponse;

    @BeforeEach
    void setUp() {
        listingSaveRequest = Instancio.create(ListingSaveRequest.class);

        listing = new Listing();
        listing.setId(1L);
        listing.setTitle(listingSaveRequest.getTitle());
        listing.setType(PropertyType.FLAT);
        listing.setArea(1);
        listing.setDescription(listingSaveRequest.getDescription());
        listing.setPrice(BigDecimal.ONE);

        listingSaveResponse = ListingSaveResponse.of(listingSaveRequest);

        listingGetResponse = ListingGetResponse.builder()
                .title(listing.getTitle())
                .description(listing.getDescription())
                .price(listing.getPrice())
                .type(listing.getType())
                .area(listing.getArea())
                .publishedDate(listing.getPublishedDate())
                .build();

        listingDeleteResponse = ListingDeleteResponse.of(listing);

        saveResponse = GenericResponse.success(listingSaveResponse);
        getAllResponse = GenericResponse.success(Arrays.asList(listingGetResponse));
        getByIdResponse = GenericResponse.success(listingGetResponse);
        deleteResponse = GenericResponse.success(listingDeleteResponse);
    }

    @Test
    void save_successfully() throws Exception {
        // Given
        when(listingService.save(any(ListingSaveRequest.class))).thenReturn(saveResponse);

        String body = objectMapper.writeValueAsString(listingSaveRequest);

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/listings")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.responseMessage").value(listingSaveResponse.getResponseMessage()));
        verify(listingService, times(1)).save(any(ListingSaveRequest.class));
    }

    @Test
    void getAll_successfully() throws Exception {
        // Given
        when(listingService.getAll()).thenReturn(getAllResponse);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/listings")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value(listingGetResponse.getTitle()))
                .andExpect(jsonPath("$.data[0].description").value(listingGetResponse.getDescription()))
                .andExpect(jsonPath("$.data[0].price").value(listingGetResponse.getPrice()))
                .andExpect(jsonPath("$.data[0].area").value(listingGetResponse.getArea()))
                .andExpect(jsonPath("$.data[0].publishedDate").value(listingGetResponse.getPublishedDate()));
        verify(listingService, times(1)).getAll();
    }

    @Test
    void getById_successfully() throws Exception {
        // Given
        when(listingService.getById(anyLong())).thenReturn(getByIdResponse);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/listings/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value(listingGetResponse.getTitle()))
                .andExpect(jsonPath("$.data.description").value(listingGetResponse.getDescription()))
                .andExpect(jsonPath("$.data.price").value(listingGetResponse.getPrice()))
                .andExpect(jsonPath("$.data.area").value(listingGetResponse.getArea()))
                .andExpect(jsonPath("$.data.publishedDate").value(listingGetResponse.getPublishedDate()));
        verify(listingService, times(1)).getById(anyLong());
    }

    @Test
    void deleteById_successfully() throws Exception {
        // Given
        when(listingService.deleteById(anyLong(), any(HttpServletRequest.class))).thenReturn(deleteResponse);

        // When
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/listings/{id}", 1L)
                .requestAttr("userId", 1L)  // Add userId attribute to the request
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value(listingDeleteResponse.getResponseMessage()));
        verify(listingService, times(1)).deleteById(anyLong(), any(HttpServletRequest.class));
    }
}
