package org.erkam.propertyservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.erkam.propertyservice.model.enums.PackageType;

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
    @Column(name = "title")
    private String title;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PackageType packageType;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "description")
    private String description;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "duration")
    private Duration duration;
}
