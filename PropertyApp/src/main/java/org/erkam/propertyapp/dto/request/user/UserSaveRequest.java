package org.erkam.propertyapp.dto.request.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSaveRequest {
    private String name;
    private String surname;
    private String email;
    private String password;
}
