"use client";

import React, { useState, useEffect } from "react";
import Link from "next/link";
import styles from "./DashboardContent.module.css";
import ListingCard from "@/components/ListingCard/ListingCard";
import PackageCard from "@/components/PackageCard/PackageCard";
import FilterSection from "@/components/FilterSection/FilterSection";
import { HiOutlineExternalLink } from "react-icons/hi";

type DashboardContentProps = {
  listings: any[];
  packages: any[];
};

const DashboardContent: React.FC<DashboardContentProps> = ({
  listings = [],
  packages = [],
}) => {
  const [filter, setFilter] = useState("ALL");
  const [typeFilter, setTypeFilter] = useState("ALL");
  const [priceFilter, setPriceFilter] = useState("ALL");
  const [dateFilter, setDateFilter] = useState("ALL");
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage, setItemsPerPage] = useState(5);
  const [filteredListings, setFilteredListings] = useState<any[]>(listings);

  useEffect(() => {
    if (!listings) return;

    let updatedListings = [...listings];

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

  const handleAddListing = (newListing: any) => {
    setFilteredListings((prev) => [newListing, ...prev]);
  };

  const handleUpdateListing = (updatedListing: any) => {
    setFilteredListings((prev) =>
      prev.map((listing) =>
        listing.id === updatedListing.id ? updatedListing : listing
      )
    );
  };

  const handleDeleteListing = (id: number) => {
    setFilteredListings((prev) => prev.filter((listing) => listing.id !== id));
  };

  return (
    <div className={styles.dashboardContentContainer}>
      <h2>
        <Link href="/portfolio" className={styles.clickableHeader}>
          Portfolio <HiOutlineExternalLink className={styles.linkIcon} />
        </Link>
      </h2>
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
        filter={filter}
        handleItemsPerPageChange={handleItemsPerPageChange}
        itemsPerPage={itemsPerPage}
      />
      {filteredListings && filteredListings.length > 0 ? (
        <>
          <div className={styles.gridContainer}>
            {paginatedListings.map((listing: any) => (
              <ListingCard
                key={listing.id}
                {...listing}
                onAdd={handleAddListing}
                onDelete={handleDeleteListing}
                onUpdate={handleUpdateListing}
              />
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
              onAdd={handleAddListing}
              onDelete={handleDeleteListing}
              onUpdate={handleUpdateListing}
            />
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
      <h2>
        <Link href="/packages" className={styles.clickableHeader}>
          Packages <HiOutlineExternalLink className={styles.linkIcon} />
        </Link>
      </h2>
      {packages && packages.length > 0 ? (
        <div className={styles.packageGridContainer}>
          {packages.map((pkg: any) => (
            <PackageCard key={pkg.title} {...pkg} />
          ))}
          <PackageCard
            isTemplate={true}
            title={"Buy a package"}
            description={""}
            type={""}
            expirationDate={""}
          />
        </div>
      ) : (
        <p>Packages not found</p>
      )}
    </div>
  );
};

export default DashboardContent;
