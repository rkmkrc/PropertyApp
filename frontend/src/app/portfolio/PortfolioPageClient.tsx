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
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage, setItemsPerPage] = useState(5);

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
    setCurrentPage(1); // Reset to the first page when filters change
  }, [filter, typeFilter, priceFilter, dateFilter, listings]);

  const paginatedListings = filteredListings.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage
  );

  const handlePageChange = (newPage: number) => {
    setCurrentPage(newPage);
  };

  const handleItemsPerPageChange = (
    e: React.ChangeEvent<HTMLSelectElement>
  ) => {
    setItemsPerPage(Number(e.target.value));
    setCurrentPage(1); // Reset to first page when items per page changes
  };

  return (
    <div className={styles.portfolioPageContainer}>
      <h1>My Portfolio</h1>
      <FilterSection
        handleTypeFilterChange={(type) => {
          setTypeFilter(type);
          setCurrentPage(1); // Reset to the first page when filters change
        }}
        handlePriceFilterChange={(price) => {
          setPriceFilter(price);
          setCurrentPage(1); // Reset to the first page when filters change
        }}
        handleDateFilterChange={(date) => {
          setDateFilter(date);
          setCurrentPage(1); // Reset to the first page when filters change
        }}
        handleStatusFilterChange={(status) => {
          setFilter(status);
          setCurrentPage(1); // Reset to the first page when filters change
        }}
        handleItemsPerPageChange={handleItemsPerPageChange}
        filter={filter}
        itemsPerPage={itemsPerPage}
      />

      {paginatedListings.length > 0 ? (
        <>
          <div className={styles.gridContainer}>
            {paginatedListings.map((listing: any) => (
              <ListingCard key={listing.id} {...listing} />
            ))}
          </div>
          <div className={styles.pagination}>
            {Array.from(
              { length: Math.ceil(filteredListings.length / itemsPerPage) },
              (_, i) => (
                <button
                  key={i + 1}
                  className={
                    currentPage === i + 1
                      ? styles.activePageButton
                      : styles.pageButton
                  }
                  onClick={() => handlePageChange(i + 1)}
                >
                  {i + 1}
                </button>
              )
            )}
          </div>
        </>
      ) : (
        <p>Listings not found</p>
      )}
    </div>
  );
};

export default PortfolioPageClient;
