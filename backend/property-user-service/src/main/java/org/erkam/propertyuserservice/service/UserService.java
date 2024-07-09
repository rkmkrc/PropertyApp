package org.erkam.propertyuserservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.erkam.propertyuserservice.constants.LogMessage;
import org.erkam.propertyuserservice.constants.UserSuccessMessage;
import org.erkam.propertyuserservice.constants.enums.MessageStatus;
import org.erkam.propertyuserservice.dto.converter.UserConverter;
import org.erkam.propertyuserservice.dto.request.auth.RegisterRequest;
import org.erkam.propertyuserservice.dto.request.user.UserSaveRequest;
import org.erkam.propertyuserservice.dto.response.GenericResponse;
import org.erkam.propertyuserservice.dto.response.user.UserDeleteResponse;
import org.erkam.propertyuserservice.dto.response.user.UserGetResponse;
import org.erkam.propertyuserservice.dto.response.user.UserSaveResponse;
import org.erkam.propertyuserservice.exception.user.UserException;
import org.erkam.propertyuserservice.exception.user.UserExceptionMessage;
import org.erkam.propertyuserservice.model.User;
import org.erkam.propertyuserservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // First check by email, to find out whether user exists or not,
    // if not exists then save if exists then throw an exception.
    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_ALREADY_EXISTS, request.getEmail()));
            throw new UserException.UserAlreadyExistException(UserExceptionMessage.USER_ALREADY_EXISTS, request.getEmail());
        }
        User user = UserConverter.toUser(request);
        userRepository.save(user);
        log.info(LogMessage.generate(MessageStatus.POS, UserSuccessMessage.USER_CREATED, request.getEmail()));
        return user;
    }

    // First check by email, to find out whether user exists or not,
    // if not exists then save if exists then throw an exception.
    public GenericResponse<UserSaveResponse> save(UserSaveRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_ALREADY_EXISTS, request.getEmail()));
            throw new UserException.UserAlreadyExistException(UserExceptionMessage.USER_ALREADY_EXISTS, request.getEmail());
        }
        userRepository.save(UserConverter.toUser(request));
        log.info(LogMessage.generate(MessageStatus.POS, UserSuccessMessage.USER_CREATED, request.getEmail()));
        return GenericResponse.success(UserSaveResponse.of(request));
    }

    // Get all users from database if there is no data on database then throw an exception,
    // else convert users to UserGetResponse list then return it.
    public GenericResponse<List<UserGetResponse>> getAll() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.NO_DATA_ON_DATABASE));
            throw new UserException.NoDataOnDatabaseException(UserExceptionMessage.NO_DATA_ON_DATABASE);
        }
        log.info(LogMessage.generate(MessageStatus.POS, UserSuccessMessage.ALL_USERS_FETCHED));
        return GenericResponse.success(UserConverter.toUserGetResponseList(users));
    }

    // First check by id, to find out whether user exists or not,
    // if not exists then throw an exception, else return UserGetResponse
    public GenericResponse<UserGetResponse> getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_NOT_FOUND, id));
            return new UserException.UserNotFoundException(UserExceptionMessage.USER_NOT_FOUND, id);
        });
        log.info(LogMessage.generate(MessageStatus.POS, UserSuccessMessage.USER_FETCHED, user.getEmail()));
        return GenericResponse.success(UserConverter.toUserGetResponse(user));
    }

    // First check by email, to find out whether user exists or not,
    // if not exists then throw an exception, else return UserGetResponse
    public User getByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_NOT_FOUND, email));
            return new UserException.UserNotFoundException(UserExceptionMessage.USER_NOT_FOUND, email);
        });
        log.info(LogMessage.generate(MessageStatus.POS, UserSuccessMessage.USER_FETCHED, user.getEmail()));
        return user;
    }

    // First check by id, to find out whether user exists or not,
    // if not exists then throw an exception, else delete the user and return UserDeleteResponse
    public GenericResponse<UserDeleteResponse> deleteById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_NOT_FOUND, id));
            return new UserException.UserNotFoundException(UserExceptionMessage.USER_NOT_FOUND, id);
        });
        userRepository.delete(user);
        log.info(LogMessage.generate(MessageStatus.POS, UserSuccessMessage.USER_DELETED, user.getEmail()));
        return GenericResponse.success(UserDeleteResponse.of(user));
    }
}
