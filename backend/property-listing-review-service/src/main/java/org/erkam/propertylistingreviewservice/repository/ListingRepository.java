package org.erkam.propertylistingreviewservice.repository;

import org.erkam.propertylistingreviewservice.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {
}
