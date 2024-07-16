package org.erkam.propertyuserservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.erkam.propertyuserservice.constants.PropertyAppConstants;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
public class GenericResponse<T> {
    private boolean success;
    private String message;
    private HttpStatus httpStatus;
    private T data;

    public static GenericResponse<ExceptionResponse> failed(String message) {
        return GenericResponse.<ExceptionResponse>builder()
                .success(false)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(message)
                .build();
    }

    public static <T> GenericResponse<T> success(T data) {
        return GenericResponse.<T>builder()
                .success(true)
                .message(PropertyAppConstants.SUCCESS)
                .httpStatus(HttpStatus.OK)
                .data(data)
                .build();
    }
}