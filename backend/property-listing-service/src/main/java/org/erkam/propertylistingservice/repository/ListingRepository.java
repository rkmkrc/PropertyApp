package org.erkam.propertylistingservice.repository;

import org.erkam.propertylistingservice.model.Listing;
import org.erkam.propertylistingservice.model.enums.ListingStatus;
import org.erkam.propertylistingservice.model.enums.PropertyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {

    // This method checks for a duplicate listing, ignoring the id values.
    Optional<Listing> findByTitleAndPriceAndAreaAndDescriptionAndType(String title, BigDecimal price, Integer area, String description, PropertyType type);

    List<Listing> findListingsByUserId(Long userId);

    List<Listing> findListingsByUserIdAndStatus(Long userId, ListingStatus listingStatus);

    Optional<Listing> findByIdAndUserId(Long id, Long userId);

    List<Listing> findByStatus(ListingStatus listingStatus);
}
