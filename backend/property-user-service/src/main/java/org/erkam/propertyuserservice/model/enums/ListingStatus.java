package org.erkam.propertyuserservice.model.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.erkam.propertyuserservice.utils.ListingStatusDeserializer;

@JsonDeserialize(using = ListingStatusDeserializer.class)
public enum ListingStatus {
    ACTIVE,
    PASSIVE,
    IN_REVIEW
}
