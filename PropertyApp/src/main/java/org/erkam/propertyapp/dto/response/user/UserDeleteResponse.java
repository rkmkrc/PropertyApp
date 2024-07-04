package org.erkam.propertyapp.dto.response.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.erkam.propertyapp.model.User;

@Getter
@Setter
@Builder
public class UserDeleteResponse {
    private String responseMessage;
    public static UserDeleteResponse of(User user) {
        return UserDeleteResponse.builder()
                .responseMessage(
                        "User deleted successfully. Name: " + user.getName()
                                + ", Surname: " + user.getSurname()
                                + ", Email: " + user.getEmail())
                .build();
    }
}
