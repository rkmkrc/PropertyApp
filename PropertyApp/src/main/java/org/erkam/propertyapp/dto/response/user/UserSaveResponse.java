package org.erkam.propertyapp.dto.response.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.erkam.propertyapp.dto.request.UserSaveRequest;

@Getter
@Setter
@Builder
public class UserSaveResponse {
    private String responseMessage;
    public static UserSaveResponse of(UserSaveRequest request) {
        return UserSaveResponse.builder()
                .responseMessage(
                        "User created successfully. Name: " + request.getName()
                        + ", Surname: " + request.getSurname()
                        + ", Email: " + request.getEmail())
                .build();
    }
}
