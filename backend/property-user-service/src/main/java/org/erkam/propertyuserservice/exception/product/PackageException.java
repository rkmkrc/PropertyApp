package org.erkam.propertyuserservice.exception.product;

import org.erkam.propertyuserservice.model.enums.PackageType;

public class PackageException extends RuntimeException {
    public PackageException(String message) {
        super(message);
    }

    public static class InvalidPackageTypeException extends PackageException {
        public InvalidPackageTypeException(String message, PackageType type) { super(message + " : " + type.name()); }
    }
}
