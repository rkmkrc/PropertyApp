"use client";

import React, { useState, useEffect } from "react";
import styles from "./DashboardContent.module.css";
import ListingCard from "@/components/ListingCard/ListingCard";
import PackageCard from "@/components/PackageCard/PackageCard";
import FilterSection from "@/components/FilterSection/FilterSection";

type DashboardContentProps = {
  listings: any[];
  packages: any[];
};

const DashboardContent: React.FC<DashboardContentProps> = ({
  listings,
  packages,
}) => {
  const [filter, setFilter] = useState("ALL");
  const [typeFilter, setTypeFilter] = useState("ALL");
  const [priceFilter, setPriceFilter] = useState("ALL");
  const [dateFilter, setDateFilter] = useState("ALL");
  const [filteredListings, setFilteredListings] = useState(listings);

  useEffect(() => {
    let updatedListings = listings;

    if (filter !== "ALL") {
      updatedListings = updatedListings.filter(
        (listing) => listing.status === filter
      );
    }

    if (typeFilter !== "ALL") {
      updatedListings = updatedListings.filter(
        (listing) => listing.type === typeFilter
      );
    }

    if (priceFilter !== "ALL") {
      if (priceFilter === "LOW_TO_HIGH") {
        updatedListings = updatedListings.sort((a, b) => a.price - b.price);
      } else if (priceFilter === "HIGH_TO_LOW") {
        updatedListings = updatedListings.sort((a, b) => b.price - a.price);
      }
    }

    if (dateFilter !== "ALL") {
      if (dateFilter === "NEW_TO_OLD") {
        updatedListings = updatedListings.sort(
          (a, b) =>
            new Date(b.publishedDate).getTime() -
            new Date(a.publishedDate).getTime()
        );
      } else if (dateFilter === "OLD_TO_NEW") {
        updatedListings = updatedListings.sort(
          (a, b) =>
            new Date(a.publishedDate).getTime() -
            new Date(b.publishedDate).getTime()
        );
      }
    }

    setFilteredListings(updatedListings);
  }, [filter, typeFilter, priceFilter, dateFilter, listings]);

  return (
    <div className={styles.dashboardContentContainer}>
      <h2>Portfolio</h2>
      <FilterSection
        handleTypeFilterChange={setTypeFilter}
        handlePriceFilterChange={setPriceFilter}
        handleDateFilterChange={setDateFilter}
        handleStatusFilterChange={setFilter}
        filter={filter}
      />
      {filteredListings ? (
        <div className={styles.gridContainer}>
          {filteredListings.map((listing: any) => (
            <ListingCard key={listing.id} {...listing} />
          ))}
          <ListingCard
            isTemplate={true}
            id={0}
            title={""}
            description={""}
            type={""}
            price={0}
            status={""}
            area={0}
            publishedDate={""}
          />{" "}
          {/* Template card for creating new listings */}
        </div>
      ) : (
        <p>Listings not found</p>
      )}
      <h2>Packages</h2>
      {packages ? (
        <div className={styles.packageGridContainer}>
          {packages.map((pkg: any) => (
            <PackageCard key={pkg.title} {...pkg} />
          ))}
        </div>
      ) : (
        <p>Packages not found</p>
      )}
    </div>
  );
};

export default DashboardContent;
