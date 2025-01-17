package org.erkam.propertyuserservice.exception.user;

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

    public static class PaymentFailedException extends UserException {
        public PaymentFailedException(String message) {
            super(message);
        }
    }

    public static class UserIsNotAuthenticatedException extends UserException {
        public UserIsNotAuthenticatedException(String message) {
            super(message);
        }
    }

    public static class UserIsNotEligibleToAddListing extends UserException {
        public UserIsNotEligibleToAddListing(String message) {super(message);}
    }

    public static class UserHasNotAnyPackagesException extends UserException {
        public UserHasNotAnyPackagesException(String message, String email) {super(message + " with email: " + email);}
    }

    public static class UserHasNotAnyListingsException extends UserException {
        public UserHasNotAnyListingsException(String message) {super(message);}
    }

    public static class UserHasNotAnyActiveListingsException extends UserException {
        public UserHasNotAnyActiveListingsException(String message) {super(message);}
    }

    public static class UserHasNotAnyPassiveListingsException extends UserException {
        public UserHasNotAnyPassiveListingsException(String message) {super(message);}
    }

    public static class ListingCouldNotDeletedException extends UserException {
        public ListingCouldNotDeletedException(String message) {super(message);}
    }

    public static class ListingStatusCouldNotUpdatedException extends UserException {
        public ListingStatusCouldNotUpdatedException(String message) {super(message);}
    }

    public static class ListingCouldNotUpdatedException extends UserException {
        public ListingCouldNotUpdatedException(String message) {super(message);}
    }
}

