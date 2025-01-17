package org.erkam.propertylistingservice.exception.listing;

public class ListingException extends RuntimeException {
    public ListingException(String message) {
        super(message);
    }

    public static class ListingAlreadyExistException extends ListingException {
        public ListingAlreadyExistException(String message) { super(message); }
    }

    public static class NoDataOnDatabaseException extends ListingException {
        public NoDataOnDatabaseException(String message) { super(message); }
    }

    public static class ListingNotFoundException extends ListingException {
        public ListingNotFoundException(String message, Long id) { super(message + " with id: " + id); }
    }

    public static class DuplicateListingException extends ListingException {
        public DuplicateListingException(String message, String title) { super(message + " with title: " + title); }
    }

    public static class NoActiveListingsFoundForThisUserException extends ListingException {
        public NoActiveListingsFoundForThisUserException(String message) { super(message); }
    }

    public static class NoPassiveListingsFoundForThisUserException extends ListingException {
        public NoPassiveListingsFoundForThisUserException(String message) { super(message); }
    }

    public static class ListingNotFoundOrYouDontHavePermissionException extends ListingException {
        public ListingNotFoundOrYouDontHavePermissionException(String message, Long id) { super(message);}
    }
}

