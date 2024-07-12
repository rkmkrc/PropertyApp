package org.erkam.propertyuserservice.model.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.erkam.propertyuserservice.utils.PackageTypeDeserializer;

@JsonDeserialize(using = PackageTypeDeserializer.class)
public enum PackageType {
    STANDARD,
    HYPE_ME,
    SHOW_ME_AT_FIRST_PAGE,
    PRO,
}
