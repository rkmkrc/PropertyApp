package org.erkam.propertyapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.erkam.propertyapp.constants.LogMessage;
import org.erkam.propertyapp.constants.UserSuccessMessage;
import org.erkam.propertyapp.constants.enums.MessageStatus;
import org.erkam.propertyapp.dto.request.UserSaveRequest;
import org.erkam.propertyapp.dto.converter.UserConverter;
import org.erkam.propertyapp.dto.response.GenericResponse;
import org.erkam.propertyapp.dto.response.UserSaveResponse;
import org.erkam.propertyapp.exception.user.UserException;
import org.erkam.propertyapp.exception.user.UserExceptionMessage;
import org.erkam.propertyapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public GenericResponse<UserSaveResponse> save(UserSaveRequest request) {
        // Check by email, to find out user exists or not.
        if (userRepository.existsByEmail(request.getEmail())) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.userAlreadyExist, request.getEmail()));
            throw new UserException.UserAlreadyExistException(UserExceptionMessage.userAlreadyExist, request.getEmail());
        }
        userRepository.save(UserConverter.toUser(request));
        log.info(LogMessage.generate(MessageStatus.POS, UserSuccessMessage.USER_CREATED, request.getEmail()));
        return GenericResponse.success(UserSaveResponse.of(request));
    }
}
