package org.erkam.propertyuserservice.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.erkam.propertyuserservice.model.enums.PackageType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuyPackageRequest {
    private PackageType type;
}
