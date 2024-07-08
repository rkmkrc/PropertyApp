package org.erkam.propertyuserservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.erkam.propertyuserservice.dto.request.user.UserSaveRequest;
import org.erkam.propertyuserservice.dto.response.GenericResponse;
import org.erkam.propertyuserservice.dto.response.user.UserDeleteResponse;
import org.erkam.propertyuserservice.dto.response.user.UserGetResponse;
import org.erkam.propertyuserservice.dto.response.user.UserSaveResponse;
import org.erkam.propertyuserservice.model.User;
import org.erkam.propertyuserservice.service.UserService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserSaveRequest userSaveRequest;
    private User user;
    private UserSaveResponse userSaveResponse;
    private UserGetResponse userGetResponse;
    private UserDeleteResponse userDeleteResponse;
    private GenericResponse<UserSaveResponse> saveResponse;
    private GenericResponse<List<UserGetResponse>> getAllResponse;
    private GenericResponse<UserGetResponse> getByIdResponse;
    private GenericResponse<UserDeleteResponse> deleteResponse;

    @BeforeEach
    void setUp() {
        userSaveRequest = Instancio.create(UserSaveRequest.class);

        user = new User();
        user.setId(1L);
        user.setName(userSaveRequest.getName());
        user.setSurname(userSaveRequest.getSurname());
        user.setEmail(userSaveRequest.getEmail());

        userSaveResponse = UserSaveResponse.of(userSaveRequest);

        userGetResponse = UserGetResponse.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();

        userDeleteResponse = UserDeleteResponse.of(user);

        saveResponse = GenericResponse.success(userSaveResponse);
        getAllResponse = GenericResponse.success(Arrays.asList(userGetResponse));
        getByIdResponse = GenericResponse.success(userGetResponse);
        deleteResponse = GenericResponse.success(userDeleteResponse);
    }

    @Test
    void save_successfully() throws Exception {
        // Given
        when(userService.save(any(UserSaveRequest.class))).thenReturn(saveResponse);

        String body = objectMapper.writeValueAsString(userSaveRequest);

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/users")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.responseMessage").value(userSaveResponse.getResponseMessage()));
        verify(userService, times(1)).save(any(UserSaveRequest.class));
    }

    @Test
    void getAll_successfully() throws Exception {
        // Given
        when(userService.getAll()).thenReturn(getAllResponse);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value(userGetResponse.getName()))
                .andExpect(jsonPath("$.data[0].surname").value(userGetResponse.getSurname()))
                .andExpect(jsonPath("$.data[0].email").value(userGetResponse.getEmail()))
                .andExpect(jsonPath("$.data[0].phoneNumber").value(userGetResponse.getPhoneNumber()));
        verify(userService, times(1)).getAll();
    }

    @Test
    void getById_successfully() throws Exception {
        // Given
        when(userService.getById(anyLong())).thenReturn(getByIdResponse);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value(userGetResponse.getName()))
                .andExpect(jsonPath("$.data.surname").value(userGetResponse.getSurname()))
                .andExpect(jsonPath("$.data.email").value(userGetResponse.getEmail()))
                .andExpect(jsonPath("$.data.phoneNumber").value(userGetResponse.getPhoneNumber()));
        verify(userService, times(1)).getById(anyLong());
    }

    @Test
    void deleteById_successfully() throws Exception {
        // Given
        when(userService.deleteById(anyLong())).thenReturn(deleteResponse);

        // When
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.responseMessage").value(userDeleteResponse.getResponseMessage()));
        verify(userService, times(1)).deleteById(anyLong());
    }
}
