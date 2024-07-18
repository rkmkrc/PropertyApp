"use client";

// components/AddListingForm/AddListingForm.tsx

import React, { useState, useEffect } from "react";
import { showToast } from "@/lib/toast";
import "react-toastify/dist/ReactToastify.css";
import styles from "./AddListingForm.module.css";

type AddListingFormProps = {
  onClose: () => void;
  onAdd: (listing: Listing) => void;
  listing?: Listing;
};

const AddListingForm: React.FC<AddListingFormProps> = ({
  onClose,
  onAdd,
  listing,
}) => {
  const [selectedType, setSelectedType] = useState(listing?.type || "");
  const [selectedStatus, setSelectedStatus] = useState(listing?.status || "");
  const [typeError, setTypeError] = useState(false);
  const [statusError, setStatusError] = useState(false);
  const [formData, setFormData] = useState({
    title: listing?.title || "",
    description: listing?.description || "",
    price: listing?.price || 0,
    area: listing?.area || 0,
  });

  useEffect(() => {
    if (listing) {
      setSelectedType(listing.type);
      setSelectedStatus(listing.status);
      setFormData({
        title: listing.title,
        description: listing.description,
        price: listing.price,
        area: listing.area,
      });
    }
  }, [listing]);

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    if (!selectedType) {
      setTypeError(true);
      showToast("Please select a type", "error");
      return;
    }

    if (listing && !selectedStatus) {
      setStatusError(true);
      showToast("Please select a status", "error");
      return;
    }

    const listingData = {
      ...formData,
      type: selectedType,
      status: selectedStatus,
    };

    try {
      const response = await fetch(
        `/api/listings${listing ? `/${listing.id}` : ""}`,
        {
          method: listing ? "PUT" : "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(listingData),
        }
      );

      const result = await response.json();
      if (result.success) {
        showToast(
          `Listing ${listing ? "updated" : "created"} successfully!`,
          "success"
        );
        onAdd(result.data); // Pass the new or updated listing data back
        onClose(); // Close the modal after adding
      } else {
        showToast(result.message, "error");
      }
    } catch (error) {
      showToast(
        `An error occurred during listing ${listing ? "update" : "creation"}`,
        "error"
      );
    }
  };

  const handleTypeSelection = (type: string) => {
    setSelectedType(type);
    setTypeError(false);
  };

  const handleStatusSelection = (status: string) => {
    setSelectedStatus(status);
    setStatusError(false);
  };

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  return (
    <form onSubmit={handleSubmit} className={styles.form}>
      <h2 className={styles.title}>
        {listing ? "Edit Listing" : "Create New Listing"}
      </h2>
      <div className={styles.field}>
        <label className={styles.label}>Title</label>
        <input
          type="text"
          name="title"
          value={formData.title}
          onChange={handleChange}
          required
          className={styles.input}
        />
      </div>
      <div className={styles.field}>
        <label className={styles.label}>Description</label>
        <textarea
          name="description"
          value={formData.description}
          onChange={handleChange}
          required
          className={styles.textarea}
        />
      </div>
      <div className={styles.field}>
        <label className={styles.label}>Type</label>
        <div className={styles.typeSelector}>
          {["FLAT", "STORE", "HOUSE", "LAND"].map((type) => (
            <div
              key={type}
              className={`${styles.typeOption} ${
                selectedType === type ? styles.selected : ""
              }`}
              onClick={() => handleTypeSelection(type)}
            >
              {type}
            </div>
          ))}
        </div>
      </div>
      {listing && (
        <div className={styles.field}>
          <label className={styles.label}>Status</label>
          <div className={styles.typeSelector}>
            {["ACTIVE", "PASSIVE"].map((status) => (
              <div
                key={status}
                className={`${styles.typeOption} ${
                  selectedStatus === status ? styles.selected : ""
                }`}
                onClick={() => handleStatusSelection(status)}
              >
                {status}
              </div>
            ))}
          </div>
        </div>
      )}
      <div className={styles.field}>
        <label className={styles.label}>Price</label>
        <input
          type="number"
          name="price"
          value={formData.price}
          onChange={handleChange}
          required
          className={styles.input}
        />
      </div>
      <div className={styles.field}>
        <label className={styles.label}>Area</label>
        <input
          type="number"
          name="area"
          value={formData.area}
          onChange={handleChange}
          required
          className={styles.input}
        />
      </div>
      <button type="submit" className={styles.submitButton}>
        Submit
      </button>
    </form>
  );
};

export default AddListingForm;
