package org.erkam.propertyapp.exception.listing;

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
}

