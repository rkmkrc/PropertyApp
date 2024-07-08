package org.erkam.propertyuserservice.dto.response.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserGetResponse {
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
}
