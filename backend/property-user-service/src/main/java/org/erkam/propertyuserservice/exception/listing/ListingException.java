package org.erkam.propertyuserservice.exception.listing;

import org.erkam.propertyuserservice.model.enums.PackageType;

public class ListingException extends RuntimeException {
    public ListingException(String message) {
        super(message);
    }

    public static class InvalidTypeOfStatusException extends ListingException {
        public InvalidTypeOfStatusException(String message, String type) { super(message + " : " + type); }
    }
}