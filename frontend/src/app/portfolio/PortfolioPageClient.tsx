"use client";

import React, { useState, useEffect } from "react";
import ListingCard from "@/components/ListingCard/ListingCard";
import FilterSection from "@/components/FilterSection/FilterSection";
import styles from "./portfolio.module.css";

type PortfolioPageClientProps = {
  listings: any[];
};

const PortfolioPageClient: React.FC<PortfolioPageClientProps> = ({
  listings,
}) => {
  const [filter, setFilter] = useState("ALL");
  const [typeFilter, setTypeFilter] = useState("ALL");
  const [priceFilter, setPriceFilter] = useState("ALL");
  const [dateFilter, setDateFilter] = useState("ALL");
  const [filteredListings, setFilteredListings] = useState<any[]>(listings);

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
    <div className={styles.portfolioPageContainer}>
      <h1>My Portfolio</h1>
      <FilterSection
        handleTypeFilterChange={setTypeFilter}
        handlePriceFilterChange={setPriceFilter}
        handleDateFilterChange={setDateFilter}
        handleStatusFilterChange={setFilter}
        filter={filter}
      />
      {filteredListings.length > 0 ? (
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
          />
        </div>
      ) : (
        <p>Listings not found</p>
      )}
    </div>
  );
};

export default PortfolioPageClient;
