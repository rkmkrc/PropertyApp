package org.erkam.propertyuserservice.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.erkam.propertyuserservice.exception.product.PackageException;
import org.erkam.propertyuserservice.exception.product.PackageExceptionMessage;
import org.erkam.propertyuserservice.model.enums.PackageType;

import java.io.IOException;
import java.util.Arrays;

public class PackageTypeDeserializer extends JsonDeserializer<PackageType> {
    @Override
    public PackageType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String value = p.getText().toUpperCase();
        return Arrays.stream(PackageType.values())
                .filter(type -> type.name().equals(value))
                .findFirst()
                .orElseThrow(() -> new PackageException.InvalidTypeOfPackageException(PackageExceptionMessage.INVALID_TYPE_OF_PACKAGE, value));
    }
}
