package org.erkam.propertyapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.erkam.propertyapp.model.enums.PackageType;

import java.math.BigDecimal;
import java.time.Duration;

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
    private String title;
    private PackageType packageType;
    private BigDecimal price;
    private String description;
    private Integer quantity;
    private Duration duration;
}
