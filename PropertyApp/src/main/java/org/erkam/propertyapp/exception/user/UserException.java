package org.erkam.propertyapp.exception.user;


public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }

    public static class UserAlreadyExistException extends UserException {
        public UserAlreadyExistException(String message, String email) {
            super(message + " Email: " + email);
        }
    }
}

