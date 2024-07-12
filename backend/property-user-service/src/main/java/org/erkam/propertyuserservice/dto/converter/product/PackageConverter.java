package org.erkam.propertyuserservice.dto.converter.product;

import org.erkam.propertyuserservice.dto.response.product.PackageGetResponse;
import org.erkam.propertyuserservice.model.Package;

import java.util.List;
import java.util.stream.Collectors;

public class PackageConverter {

    public static PackageGetResponse toPackageGetResponse(Package pkg) {
        return PackageGetResponse.builder()
                .title(pkg.getTitle())
                .type(pkg.getPackageType())
                .description(pkg.getDescription())
                .expirationDate(pkg.getExpirationDate())
                .build();
    }

    // Convert the packages to response objects
    public static List<PackageGetResponse> toPackageGetResponseList(List<Package> packages) {
        return packages.stream()
                .map(PackageConverter::toPackageGetResponse)
                .collect(Collectors.toList());
    }
}
