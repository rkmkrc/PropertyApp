package org.erkam.propertyservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.erkam.propertyservice.model.enums.ListingStatus;
import org.erkam.propertyservice.model.enums.PropertyType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "listings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PropertyType type ;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "area")
    private Integer area;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ListingStatus status;
    @Column(name = "published_date")
    private LocalDateTime publishedDate;
}
