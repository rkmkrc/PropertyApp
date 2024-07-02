package org.erkam.propertyapp.dto.converter;

import org.erkam.propertyapp.dto.request.UserSaveRequest;
import org.erkam.propertyapp.model.User;

public class UserConverter {
    public static User toUser(UserSaveRequest request) {
        return User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }
}
