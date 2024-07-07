package org.erkam.propertyservice.exception.user;

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
    }
}

