// components/AddListingForm/AddListingForm.tsx
"use client";

import React from "react";
import { showToast } from "@/lib/toast";
import "react-toastify/dist/ReactToastify.css";
import styles from "./AddListingForm.module.css";

type AddListingFormProps = {
  onClose: () => void;
};

const AddListingForm: React.FC<AddListingFormProps> = ({ onClose }) => {
  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const form = event.currentTarget;
    const formData = new FormData(form);

    const listingData = {
      title: formData.get("title"),
      description: formData.get("description"),
      type: formData.get("type"),
      price: Number(formData.get("price")),
      area: Number(formData.get("area")),
    };

    try {
      const response = await fetch("/api/listings", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(listingData),
      });

      const result = await response.json();
      if (result.success) {
        showToast(result.message, "success");
        onClose(); // Close the modal after successful submission
      } else {
        showToast(result.message, "error");
      }
    } catch (error) {
      showToast("An error occurred during listing creation", "error");
    }
  };

  return (
    <form onSubmit={handleSubmit} className={styles.form}>
      <h2 className={styles.title}>Create New Listing</h2>
      <div className={styles.field}>
        <label className={styles.label}>Title</label>
        <input type="text" name="title" required className={styles.input} />
      </div>
      <div className={styles.field}>
        <label className={styles.label}>Description</label>
        <textarea name="description" required className={styles.textarea} />
      </div>
      <div className={styles.field}>
        <label className={styles.label}>Type</label>
        <input type="text" name="type" required className={styles.input} />
      </div>
      <div className={styles.field}>
        <label className={styles.label}>Price</label>
        <input type="number" name="price" required className={styles.input} />
      </div>
      <div className={styles.field}>
        <label className={styles.label}>Area</label>
        <input type="number" name="area" required className={styles.input} />
      </div>
      <button type="submit" className={styles.submitButton}>
        Submit
      </button>
    </form>
  );
};

export default AddListingForm;
