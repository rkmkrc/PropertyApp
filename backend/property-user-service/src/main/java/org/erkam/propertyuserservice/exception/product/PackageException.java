package org.erkam.propertyuserservice.exception.product;

import org.erkam.propertyuserservice.model.enums.PackageType;

public class PackageException extends RuntimeException {
    public PackageException(String message) {
        super(message);
    }

    public static class InvalidTypeOfPackageException extends PackageException {
        public InvalidTypeOfPackageException(String message, PackageType type) { super(message + " : " + type.name()); }
        public InvalidTypeOfPackageException(String message, String type) { super(message + " : " + type); }
    }
}
