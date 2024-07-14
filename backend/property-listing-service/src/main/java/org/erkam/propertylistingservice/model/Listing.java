package org.erkam.propertylistingservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.erkam.propertylistingservice.dto.request.listing.ListingUpdateRequest;
import org.erkam.propertylistingservice.model.enums.ListingStatus;
import org.erkam.propertylistingservice.model.enums.PropertyType;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    @Column(name = "user_id")
    private Long userId;
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
    private LocalDate publishedDate;

    public Listing updateByRequest(ListingUpdateRequest request) {
        this.setTitle(request.getTitle());
        this.setDescription(request.getDescription());
        this.setPrice(request.getPrice());
        this.setArea(request.getArea());
        this.setType(request.getType());
        return this;
    }
}
