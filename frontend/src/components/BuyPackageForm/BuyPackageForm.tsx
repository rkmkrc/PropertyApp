"use client";

import React, { useState } from "react";
import styles from "./BuyPackageForm.module.css";
import { FaStar, FaRocket, FaGem, FaFireAlt } from "react-icons/fa";
import { showToast } from "@/lib/toast";

type BuyPackageFormProps = {
  onClose: () => void;
  onAddPackage?: (newPackage: any) => void;
};

const packages = [
  {
    name: "Standard Package",
    code: "STANDARD",
    icon: <FaStar />,
    description: "You will be able to publish 10 listings with this package",
    quota: 10,
    expirationDuration: "30 days",
    price: 29.99,
  },
  {
    name: "Hype Me Package",
    code: "HYPE_ME",
    icon: <FaRocket />,
    description: "You will be able to publish 40 listings with this package",
    quota: 40,
    expirationDuration: "60 days",
    price: 99.99,
  },
  {
    name: "PRO Package",
    code: "PRO",
    icon: <FaGem />,
    description: "You will be able to publish 25 listings with this package",
    quota: 25,
    expirationDuration: "90 days",
    price: 49.99,
  },
  {
    name: "Show Me At First Page Package",
    code: "SHOW_ME_AT_FIRST_PAGE",
    icon: <FaFireAlt />,
    description: "Your listings will be shown on the first page",
    quota: 100,
    expirationDuration: "30 days",
    price: 149.99,
  },
];

const BuyPackageForm: React.FC<BuyPackageFormProps> = ({
  onClose,
  onAddPackage,
}) => {
  const [selectedPackage, setSelectedPackage] = useState(packages[0]);
  const [isPurchasing, setIsPurchasing] = useState(false); // Added state to prevent multiple submissions

  const handlePackageSelection = (pkg: (typeof packages)[0]) => {
    setSelectedPackage(pkg);
  };

  const handlePurchase = async (e: React.MouseEvent) => {
    e.preventDefault();
    if (isPurchasing) return; // Prevent multiple submissions
    setIsPurchasing(true); // Set purchasing state to true

    try {
      const response = await fetch(`/api/users/packages`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ type: selectedPackage.code }),
      });

      const result = await response.json();
      if (result.success) {
        showToast(`Package purchased successfully!`, "success");
        if (onAddPackage) {
          onAddPackage({
            title: selectedPackage.name,
            type: selectedPackage.code,
            description: selectedPackage.description,
            expirationDate: new Date(
              Date.now() + 30 * 24 * 60 * 60 * 1000
            ).toISOString(), // Example expiration date
          });
        }
        onClose(); // Close the modal after adding
      } else {
        showToast(result.message, "error");
      }
    } catch (error) {
      showToast(`An error occurred: ${error}`, "error");
    } finally {
      setIsPurchasing(false); // Reset purchasing state
    }
  };

  return (
    <div className={styles.formContainer}>
      <label className={styles.label}>Select Package</label>
      <div className={styles.typeSelector}>
        {packages.map((pkg) => (
          <div
            key={pkg.name}
            className={`${styles.typeOption} ${
              selectedPackage.name === pkg.name ? styles.selected : ""
            }`}
            onClick={() => handlePackageSelection(pkg)}
          >
            <div className={styles.icon}>{pkg.icon}</div>
            <div className={styles.packageName}>{pkg.name}</div>
            <div className={styles.packageDescription}>{pkg.description}</div>
            <div className={styles.packageQuota}>
              <span className={styles.boldText}>Quota:</span> {pkg.quota}
            </div>
            <div className={styles.packageExpiration}>
              <span className={styles.boldText}>Expiration:</span>{" "}
              {pkg.expirationDuration}
            </div>
            <div className={styles.price}>${pkg.price}</div>
          </div>
        ))}
      </div>
      <button
        type="button"
        onClick={handlePurchase}
        className={styles.submitButton}
        disabled={isPurchasing} // Disable button while purchasing
      >
        {isPurchasing ? "Purchasing..." : "Purchase"}
      </button>
    </div>
  );
};

export default BuyPackageForm;
