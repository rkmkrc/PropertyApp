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
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

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

    private static final Map<PackageType, Supplier<Package>> packageMap = new EnumMap<>(PackageType.class);
    private static final Map<PackageType, Integer> quotaMap = new EnumMap<>(PackageType.class);

    static {
        packageMap.put(PackageType.STANDARD, () -> createPackage(
                "Standard Package",
                PackageType.STANDARD,
                BigDecimal.valueOf(29.99),
                "You will be able to publish 10 listings with this package",
                10,
                30));
        packageMap.put(PackageType.PRO, () -> createPackage(
                "Pro Package",
                PackageType.PRO,
                BigDecimal.valueOf(49.99),
                "You will be able to publish 25 listings with this package",
                25,
                30));
        packageMap.put(PackageType.HYPE_ME, () -> createPackage(
                "Hype Me Package",
                PackageType.HYPE_ME,
                BigDecimal.valueOf(99.99),
                "You will be able to publish 40 listings with this package",
                40,
                30));
        packageMap.put(PackageType.SHOW_ME_AT_FIRST_PAGE, () -> createPackage(
                "Show Me at First Page Package",
                PackageType.SHOW_ME_AT_FIRST_PAGE,
                BigDecimal.valueOf(149.99),
                "You will be able to publish 100 listings with this package and your listings will be shown on the first page",
                100,
                30));

        quotaMap.put(PackageType.STANDARD, 10);
        quotaMap.put(PackageType.PRO, 25);
        quotaMap.put(PackageType.HYPE_ME, 40);
        quotaMap.put(PackageType.SHOW_ME_AT_FIRST_PAGE, 100);
    }

    private static Package createPackage(String title, PackageType type, BigDecimal price, String description, int quota, int expirationDays) {
        Package newPackage = new Package();
        newPackage.setTitle(title);
        newPackage.setPackageType(type);
        newPackage.setPrice(price);
        newPackage.setDescription(description);
        newPackage.setQuota(quota);
        newPackage.setExpirationDate(LocalDate.now().plusDays(expirationDays));
        return newPackage;
    }

    // Returns the package instance of request
    public static Package of(BuyPackageRequest request) {
        Supplier<Package> packageSupplier = packageMap.get(request.getType());
        if (packageSupplier != null) {
            return packageSupplier.get();
        }
        throw new PackageException.InvalidTypeOfPackageException(PackageExceptionMessage.INVALID_TYPE_OF_PACKAGE, request.getType());
    }

    public static Integer getQuotaOfType(PackageType type) {
        Integer quota = quotaMap.get(type);
        if (quota != null) {
            return quota;
        }
        throw new PackageException.InvalidTypeOfPackageException(PackageExceptionMessage.INVALID_TYPE_OF_PACKAGE, type);
    }
}
