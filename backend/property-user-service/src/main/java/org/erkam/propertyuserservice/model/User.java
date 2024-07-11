package org.erkam.propertyuserservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.erkam.propertyuserservice.dto.request.user.BuyPackageRequest;
import org.erkam.propertyuserservice.model.enums.PackageType;
import org.erkam.propertyuserservice.model.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    @Column(name = "total_days_to_expiration_of_packages")
    private Integer totalDaysToExpirationOfPackages = 0;
    @Column(name = "publishing_quota")
    private Integer publishingQuota = 0;

    // Add new package to user.
    public void assignPackage(BuyPackageRequest request) {
        if (packages == null) {
            packages = new ArrayList<>();
        }
        this.packages.add(Package.of(request));
    }

    public void updateUserAfterBuyingPackage(BuyPackageRequest request) {
        updateTotalDaysToExpirationOfPackages();
        updatePublishingQuota(request.getType());
    }

    // Update the publishing quota of user
    private void updatePublishingQuota(PackageType type) {
        if (this.publishingQuota == null) {
            this.publishingQuota = 0;
        }
        this.publishingQuota += Package.getQuotaOfType(type);
    }

    // Updates user according to products.
    private void updateTotalDaysToExpirationOfPackages() {
        LocalDate currentDate = LocalDate.now();
        int totalDaysToExpiration = packages.stream()
                .mapToInt(pkg -> (int) ChronoUnit.DAYS.between(currentDate, pkg.getExpirationDate()))
                .sum();
        this.totalDaysToExpirationOfPackages = totalDaysToExpiration;
    }

    // Updates user according to products.
    public void reducePublishingQuotaByOne() {
        this.publishingQuota -= 1;
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
