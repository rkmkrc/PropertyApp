"use client";

import React, { useState } from "react";
import Image from "next/image";
import { useRouter } from "next/navigation";
import styles from "./ListingCard.module.css";
import Modal from "@/components/Modal/Modal";
import AddListingForm from "@/components/AddListingForm/AddListingForm";
import { FaTrashAlt, FaPencilAlt } from "react-icons/fa";
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
  onAdd?: (listing: Listing) => void;
  onDelete?: (id: number) => void;
  onUpdate?: (listing: Listing) => void;
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
  onAdd,
  onDelete,
  onUpdate,
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isHovering, setIsHovering] = useState(false);
  const router = useRouter();

  const handleModalOpen = () => {
    setIsModalOpen(true);
    setIsHovering(false); // Reset hover state when modal is opened
  };
  const handleModalClose = () => {
    setIsModalOpen(false);
    setIsHovering(false); // Reset hover state when modal is closed
  };

  const handleDelete = async (event: React.MouseEvent) => {
    event.stopPropagation();
    try {
      const response = await fetch(`/api/listings/${id}`, {
        method: "DELETE",
      });

      const result = await response.json();
      if (result.success) {
        showToast(result.message, "success");
        onDelete && onDelete(id);
      } else {
        showToast(result.message, "error");
      }
    } catch (error) {
      showToast("An error occurred during deletion", "error");
    }
  };

  const handleEdit = (event: React.MouseEvent) => {
    event.stopPropagation();
    setIsModalOpen(true);
  };

  const handleUpdate = (updatedListing: Listing) => {
    onUpdate && onUpdate(updatedListing);
    handleModalClose();
  };

  const handleCardClick = () => {
    router.push(`/listings/${id}`);
  };

  const formattedPrice = formatPrice(price);

  if (isTemplate) {
    const handleAddListing = async (newListing: Listing) => {
      onAdd && onAdd(newListing);
      handleModalClose();
    };

    return (
      <div
        className={`${styles.card} ${styles.templateCard}`}
        onClick={handleModalOpen}
      >
        <div className={styles.plusIcon}>+</div>
        <Modal isOpen={isModalOpen} onClose={handleModalClose}>
          <AddListingForm onClose={handleModalClose} onAdd={handleAddListing} />
        </Modal>
      </div>
    );
  }

  return (
    <div
      className={styles.card}
      onMouseEnter={() => setIsHovering(true)}
      onMouseLeave={() => setIsHovering(false)}
      onClick={handleCardClick}
    >
      {isHovering && !isTemplate && (
        <>
          <div className={styles.deleteButton} onClick={handleDelete}>
            <FaTrashAlt />
          </div>
          <div className={styles.editButton} onClick={handleEdit}>
            <FaPencilAlt />
          </div>
        </>
      )}
      <div className={styles.imageWrapper}>
        <Image
          src={`/${type}.png`}
          alt={`${title} image`}
          width={300}
          height={200}
          className={styles.image}
        />
      </div>
      <div className={styles.details}>
        <h2 className={styles.title}>{title}</h2>
        <p className={styles.info}>
          <span>Type: {type}</span> | <span>Area: {area} sq mt</span>
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
      <Modal isOpen={isModalOpen} onClose={handleModalClose}>
        <AddListingForm
          onClose={handleModalClose}
          onAdd={handleUpdate}
          listing={{
            id,
            title,
            description,
            type,
            status,
            price,
            area,
            publishedDate,
          }}
        />
      </Modal>
    </div>
  );
};

export default ListingCard;
