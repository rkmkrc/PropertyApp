package org.erkam.propertyapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.erkam.propertyapp.model.enums.ListingStatus;
import org.erkam.propertyapp.model.enums.PropertyType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "listings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private PropertyType type ;
    private BigDecimal price;
    private Integer area;
    private ListingStatus status;
    private LocalDateTime publishedDate;
}
