"use client";

import React from "react";
import styles from "./FilterSection.module.css";

type FilterSectionProps = {
  handleTypeFilterChange: (type: string) => void;
  handlePriceFilterChange: (priceOrder: string) => void;
  handleDateFilterChange: (dateOrder: string) => void;
  handleStatusFilterChange: (status: string) => void;
  handleItemsPerPageChange: (e: React.ChangeEvent<HTMLSelectElement>) => void;
  filter: string;
  itemsPerPage: number;
};

const FilterSection: React.FC<FilterSectionProps> = ({
  handleTypeFilterChange,
  handlePriceFilterChange,
  handleDateFilterChange,
  handleStatusFilterChange,
  handleItemsPerPageChange,
  filter,
  itemsPerPage,
}) => {
  return (
    <div className={styles.filterContainer}>
      <div className={styles.dropdownFilterSection}>
        <span className={styles.filterText}>
          Type:
          <select
            className={styles.filterDropdown}
            onChange={(e) => handleTypeFilterChange(e.target.value)}
          >
            <option value="ALL">All Types</option>
            <option value="FLAT">Flat</option>
            <option value="STORE">Store</option>
            <option value="HOUSE">House</option>
            <option value="LAND">Land</option>
          </select>
        </span>
        <span className={styles.filterText}>
          Price:
          <select
            className={styles.filterDropdown}
            onChange={(e) => handlePriceFilterChange(e.target.value)}
          >
            <option value="ALL">All Prices</option>
            <option value="LOW_TO_HIGH">Low to High</option>
            <option value="HIGH_TO_LOW">High to Low</option>
          </select>
        </span>
        <span className={styles.filterText}>
          Date:
          <select
            className={styles.filterDropdown}
            onChange={(e) => handleDateFilterChange(e.target.value)}
          >
            <option value="ALL">All Dates</option>
            <option value="NEW_TO_OLD">New to Old</option>
            <option value="OLD_TO_NEW">Old to New</option>
          </select>
        </span>
        <span className={styles.filterText}>
          Listings per page:
          <select
            className={styles.filterDropdown}
            value={itemsPerPage}
            onChange={handleItemsPerPageChange}
          >
            <option value={5}>5</option>
            <option value={10}>10</option>
            <option value={15}>15</option>
            <option value={20}>20</option>
          </select>
        </span>
      </div>
      <div className={styles.statusFilterSection}>
        {["ALL", "ACTIVE", "PASSIVE"].map((status) => (
          <div
            key={status}
            className={`${styles.filterOption} ${
              filter === status ? styles.selected : ""
            }`}
            onClick={() => handleStatusFilterChange(status)}
          >
            {status}
          </div>
        ))}
      </div>
    </div>
  );
};

export default FilterSection;
