package org.erkam.propertyuserservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.erkam.propertyuserservice.dto.request.user.BuyPackageRequest;
import org.erkam.propertyuserservice.exception.user.UserExceptionMessage;
import org.erkam.propertyuserservice.model.enums.PackageType;
import org.erkam.propertyuserservice.model.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Package> packages;
    @Builder.Default
    @Column(name = "total_days_to_expiration_of_packages")
    private Integer totalDaysToExpirationOfPackages = 0;
    @Builder.Default
    @Column(name = "publishing_quota")
    private Integer publishingQuota = 0;

    // Add new package to user.
    public void assignPackage(BuyPackageRequest request) {
        if (packages == null) {
            packages = new ArrayList<>();
        }
        this.packages.add(Package.of(request));
    }

    public void updateUserAfterPurchasingAPackage(BuyPackageRequest request) {
        updateTotalDaysAccordingToExpirationOfPackages();
        updatePublishingQuota(request.getType());
    }

    // Update the publishing quota of user
    private void updatePublishingQuota(PackageType type) {
        if (this.publishingQuota == null) {
            this.publishingQuota = 0;
        }
        this.publishingQuota += Package.getQuotaOfType(type);
    }

    // Updates user according to products. Exclude expired packages if any
    // Ensure non-negative total
    private void updateTotalDaysAccordingToExpirationOfPackages() {
        LocalDate currentDate = LocalDate.now();
        int totalDaysToExpiration = packages.stream()
                .filter(pkg -> !pkg.getExpirationDate().isBefore(currentDate))
                .mapToInt(pkg -> (int) ChronoUnit.DAYS.between(currentDate, pkg.getExpirationDate()))
                .sum();
        this.totalDaysToExpirationOfPackages = Math.max(totalDaysToExpiration, 0);
    }

    // Updates user according to products.
    public void reducePublishingQuotaByOne() {
        this.publishingQuota -= 1;
    }

    // Checks the user is eligible to add listing
    public boolean isUserEligibleToPublishListing() {
        return publishingQuota > 0 && totalDaysToExpirationOfPackages > 0;
    }

    // Gets the eligibility error messages
    public String getEligibilityErrorMessages() {
        StringBuilder errorMessages = new StringBuilder();

        if (publishingQuota <= 0) {
            errorMessages.append(UserExceptionMessage.USER_HAS_NOT_ANY_QUOTA_TO_PUBLISH_A_LISTING).append(". ");
        }

        if (totalDaysToExpirationOfPackages <= 0) {
            errorMessages.append(UserExceptionMessage.USER_HAS_NOT_ANY_PACKAGES_TO_PUBLISH_A_LISTING).append(". ");
        }

        return errorMessages.toString().trim();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() { return true; }

}
