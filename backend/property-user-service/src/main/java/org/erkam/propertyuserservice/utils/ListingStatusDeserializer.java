package org.erkam.propertyuserservice.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.erkam.propertyuserservice.exception.listing.ListingException;
import org.erkam.propertyuserservice.exception.listing.ListingExceptionMessage;
import org.erkam.propertyuserservice.model.enums.ListingStatus;

import java.io.IOException;

public class ListingStatusDeserializer extends JsonDeserializer<ListingStatus> {
    @Override
    public ListingStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String value = p.getText().toUpperCase();

        switch (value) {
            case "ACTIVE":
                return ListingStatus.ACTIVE;
            case "PASSIVE":
                return ListingStatus.PASSIVE;
            case "IN_REVIEW":
                return ListingStatus.IN_REVIEW;
            default:
                throw new ListingException.InvalidTypeOfStatusException(ListingExceptionMessage.INVALID_TYPE_OF_STATUS, value);
        }
    }
}