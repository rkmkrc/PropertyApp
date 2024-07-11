package org.erkam.propertyuserservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.erkam.propertyuserservice.dto.request.user.BuyPackageRequest;
import org.erkam.propertyuserservice.exception.product.PackageException;
import org.erkam.propertyuserservice.exception.product.PackageExceptionMessage;
import org.erkam.propertyuserservice.model.enums.PackageType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "packages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PackageType packageType;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "description")
    private String description;
    @Column(name = "quota")
    private Integer quota;
    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    public static Package standartPackage() {
        Package standartPackage = new Package();
        standartPackage.title = "Standard Package";
        standartPackage.packageType = PackageType.STANDARD;
        standartPackage.price = BigDecimal.valueOf(29.99);
        standartPackage.description = "You will be able to publish 10 listing with this package";
        standartPackage.quota = 10;
        standartPackage.expirationDate = LocalDate.now().plusDays(30);
        return standartPackage;
    }

    public static Package proPackage() {
        Package proPackage = new Package();
        proPackage.title = "Pro Package";
        proPackage.packageType = PackageType.PRO;
        proPackage.price = BigDecimal.valueOf(49.99);
        proPackage.description = "You will be able to publish 25 listing with this package";
        proPackage.quota = 25;
        proPackage.expirationDate = LocalDate.now().plusDays(30);
        return proPackage;
    }

    public static Package hypeMePackage() {
        Package hypeMePackage = new Package();
        hypeMePackage.title = "Hype Me Package";
        hypeMePackage.packageType = PackageType.HYPE_ME;
        hypeMePackage.price = BigDecimal.valueOf(99.99);
        hypeMePackage.description = "You will be able to publish 40 listing with this package";
        hypeMePackage.quota = 40;
        hypeMePackage.expirationDate = LocalDate.now().plusDays(30);
        return hypeMePackage;
    }

    public static Package showMeAtFirstPagePackage() {
        Package showMeAtFirstPagePackage = new Package();
        showMeAtFirstPagePackage.title = "Show Me at First Page Package";
        showMeAtFirstPagePackage.packageType = PackageType.SHOW_ME_AT_FIRST_PAGE;
        showMeAtFirstPagePackage.price = BigDecimal.valueOf(149.99);
        showMeAtFirstPagePackage.description = "You will be able to publish 100 listing with this package and your listings will be showing in the first page.";
        showMeAtFirstPagePackage.quota = 100;
        showMeAtFirstPagePackage.expirationDate = LocalDate.now().plusDays(30);
        return showMeAtFirstPagePackage;
    }

    // Returns the package instance of request
    public static Package of(BuyPackageRequest request) {
        PackageType type = request.getType();
        if (type == PackageType.STANDARD) {
            return standartPackage();
        } else if (type == PackageType.PRO) {
            return proPackage();
        } else if (type == PackageType.SHOW_ME_AT_FIRST_PAGE) {
            return showMeAtFirstPagePackage();
        } else if (type == PackageType.HYPE_ME) {
            return hypeMePackage();
        }
        throw new PackageException.InvalidPackageTypeException(PackageExceptionMessage.INVALID_TYPE_OF_PACKAGE, request.getType());
    }

    public static Integer getQuotaOfType(PackageType type) {
        switch (type) {
            case STANDARD:
                return 10;
            case PRO:
                return 25;
            case HYPE_ME:
                return 40;
            case SHOW_ME_AT_FIRST_PAGE:
                return 100;
            default:
                throw new PackageException.InvalidPackageTypeException(PackageExceptionMessage.INVALID_TYPE_OF_PACKAGE, type);
        }
    }
}
