"use client";

// components/ListingCard/ListingCard.tsx

import React, { useState } from "react";
import Image from "next/image";
import styles from "./ListingCard.module.css";
import Modal from "@/components/Modal/Modal";
import AddListingForm from "@/components/AddListingForm/AddListingForm";
import { FaTrashAlt } from "react-icons/fa";
import { showToast } from "@/lib/toast";

type Listing = {
  id: number;
  title: string;
  description: string;
  type: string;
  price: number;
  status: string;
  area: number;
  publishedDate: string;
  isTemplate?: boolean;
};

const formatPrice = (price: number) => {
  return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
};

const ListingCard: React.FC<Listing> = ({
  id,
  title,
  description,
  type,
  price,
  status,
  area,
  publishedDate,
  isTemplate = false,
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isHovering, setIsHovering] = useState(false);

  const handleModalOpen = () => setIsModalOpen(true);
  const handleModalClose = () => setIsModalOpen(false);

  const handleDelete = async () => {
    try {
      const response = await fetch(`/api/listings/${id}`, {
        method: "DELETE",
      });

      const result = await response.json();
      if (result.success) {
        // Refresh the page or update state to reflect the deletion
        showToast(result.message, "success");
        console.log("Listing deleted successfully");
      } else {
        showToast(result.message, "error");
        console.error("Failed to delete listing");
      }
    } catch (error) {
      console.error("An error occurred during deletion", error);
    }
  };

  if (isTemplate) {
    return (
      <div
        className={`${styles.card} ${styles.templateCard}`}
        onClick={handleModalOpen}
      >
        <div className={styles.plusIcon}>+</div>
        <Modal isOpen={isModalOpen} onClose={handleModalClose}>
          <AddListingForm onClose={handleModalClose} />
        </Modal>
      </div>
    );
  }

  const formattedPrice = formatPrice(price);
  return (
    <div
      className={styles.card}
      onMouseEnter={() => setIsHovering(true)}
      onMouseLeave={() => setIsHovering(false)}
    >
      {isHovering && !isTemplate && (
        <div className={styles.deleteButton} onClick={handleDelete}>
          <FaTrashAlt />
        </div>
      )}
      <div className={styles.imageWrapper}>
        <Image
          src="/land.jpg"
          alt={`${title} image`}
          width={300}
          height={200}
          className={styles.image}
        />
      </div>
      <div className={styles.details}>
        <h2 className={styles.title}>{title}</h2>
        <p className={styles.info}>
          <span>Type: {type}</span> | <span>Area: {area} sq ft</span>
        </p>
        <p className={styles.price}>
          <span>Price: {formattedPrice} TL</span>
        </p>
        <div className={styles.footer}>
          <div className={styles.statusWrapper}>
            <div
              className={`${styles.statusIndicator} ${
                status.toLowerCase() === "active"
                  ? styles.active
                  : styles.inactive
              }`}
            />
            <span className={styles.statusText}>{status}</span>
          </div>
          <p className={styles.date}>
            Published on: {new Date(publishedDate).toLocaleDateString()}
          </p>
        </div>
      </div>
    </div>
  );
};

export default ListingCard;
