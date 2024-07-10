package org.erkam.propertyuserservice.exception.user;

import org.erkam.propertyuserservice.client.listing.dto.response.ListingSaveResponse;
import org.erkam.propertyuserservice.dto.response.GenericResponse;

public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }

    public static class UserAlreadyExistException extends UserException {
        public UserAlreadyExistException(String message, String email) {
            super(message + " email: " + email);
        }
    }

    public static class NoDataOnDatabaseException extends UserException {
        public NoDataOnDatabaseException(String message) { super(message); }
    }

    public static class UserNotFoundException extends UserException {
        public UserNotFoundException(String message, Long id) { super(message + " with id: " + id); }
        public UserNotFoundException(String message, String email) { super(message + " with email: " + email); }

    }

    public static class ListingCouldNotCreatedException extends UserException {
        public ListingCouldNotCreatedException(String message) { super(message); }
    }

    public static class UserIsNotAuthenticatedException extends UserException {
        public UserIsNotAuthenticatedException(String message) {
            super(message);
        }
    }
}

