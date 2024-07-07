package org.erkam.propertyapp.service;

import org.erkam.propertyapp.constants.PropertyAppConstants;
import org.erkam.propertyapp.dto.request.user.UserSaveRequest;
import org.erkam.propertyapp.dto.response.GenericResponse;
import org.erkam.propertyapp.dto.response.user.UserDeleteResponse;
import org.erkam.propertyapp.dto.response.user.UserGetResponse;
import org.erkam.propertyapp.dto.response.user.UserSaveResponse;
import org.erkam.propertyapp.exception.user.UserException;
import org.erkam.propertyapp.exception.user.UserExceptionMessage;
import org.erkam.propertyapp.model.User;
import org.erkam.propertyapp.repository.UserRepository;
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

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_success() {
        UserSaveRequest request = Instancio.of(UserSaveRequest.class).create();
        request.setEmail("test@example.com");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(new User());

        GenericResponse<UserSaveResponse> response = userService.save(request);

        verify(userRepository, times(1)).save(any(User.class));
        assertNotNull(response);
        assertTrue(response.getMessage().equals(PropertyAppConstants.SUCCESS));
    }

    @Test
    void save_shouldThrowException_whenUserAlreadyExists() {
        UserSaveRequest request = Instancio.of(UserSaveRequest.class).create();
        request.setEmail("existing@example.com");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        UserException.UserAlreadyExistException exception = assertThrows(UserException.UserAlreadyExistException.class, () -> {
            userService.save(request);
        });

        assertEquals(UserExceptionMessage.USER_ALREADY_EXISTS + " email: " + request.getEmail(), exception.getMessage());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void getAll_success() {
        List<User> users = new ArrayList<>();
        users.add(new User());

        when(userRepository.findAll()).thenReturn(users);

        GenericResponse<List<UserGetResponse>> response = userService.getAll();

        verify(userRepository, times(1)).findAll();
        assertNotNull(response);
        assertTrue(response.getMessage().equals(PropertyAppConstants.SUCCESS));
        assertFalse(response.getData().isEmpty());
    }

    @Test
    void getAll_shouldThrowException_whenNoDataExists() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        UserException.NoDataOnDatabaseException exception = assertThrows(UserException.NoDataOnDatabaseException.class, () -> {
            userService.getAll();
        });

        assertEquals(UserExceptionMessage.NO_DATA_ON_DATABASE, exception.getMessage());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getById_success() {
        Long userId = 1L;
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        GenericResponse<UserGetResponse> response = userService.getById(userId);

        verify(userRepository, times(1)).findById(userId);
        assertNotNull(response);
        assertTrue(response.getMessage().equals(PropertyAppConstants.SUCCESS));

    }

    @Test
    void getById_shouldThrowException_whenUserNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserException.UserNotFoundException exception = assertThrows(UserException.UserNotFoundException.class, () -> {
            userService.getById(userId);
        });

        assertEquals(UserExceptionMessage.USER_NOT_FOUND + " with id: " + userId, exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void deleteById_success() {
        Long userId = 1L;
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        GenericResponse<UserDeleteResponse> response = userService.deleteById(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(user);
        assertNotNull(response);
        assertTrue(response.getMessage().equals(PropertyAppConstants.SUCCESS));
    }

    @Test
    void deleteById_shouldThrowException_whenUserNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserException.UserNotFoundException exception = assertThrows(UserException.UserNotFoundException.class, () -> {
            userService.deleteById(userId);
        });

        assertEquals(UserExceptionMessage.USER_NOT_FOUND + " with id: " + userId, exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }
}
