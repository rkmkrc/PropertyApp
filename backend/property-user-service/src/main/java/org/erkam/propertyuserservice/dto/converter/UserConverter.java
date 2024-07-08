package org.erkam.propertyuserservice.dto.converter;

import org.erkam.propertyuserservice.dto.request.user.UserSaveRequest;
import org.erkam.propertyuserservice.dto.response.user.UserGetResponse;
import org.erkam.propertyuserservice.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserConverter {
    public static User toUser(UserSaveRequest request) {
        return User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

    public static UserGetResponse toUserGetResponse(User user) {
        return UserGetResponse.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public static List<UserGetResponse> toUserGetResponseList(List<User> users) {
        // Convert the users to response objects
        return users.stream()
                .map(UserConverter::toUserGetResponse)
                .collect(Collectors.toList());
    }
}
