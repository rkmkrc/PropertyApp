package org.erkam.propertyapp.repository;

import org.erkam.propertyapp.model.Listing;
import org.erkam.propertyapp.model.enums.ListingStatus;
import org.erkam.propertyapp.model.enums.PropertyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {

    // This method checks for a duplicate listing, ignoring the id values.
    Optional<Listing> findByTitleAndPriceAndAreaAndDescriptionAndTypeAndStatus(String title, BigDecimal price, Integer area, String description, PropertyType type, ListingStatus status);
}
