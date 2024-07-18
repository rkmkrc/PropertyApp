"use client";

import React, { useState } from "react";
import styles from "./BuyPackageForm.module.css";
import { FaStar, FaRocket, FaGem, FaFireAlt } from "react-icons/fa";

type BuyPackageFormProps = {
  onClose: () => void;
};

const packages = [
  {
    name: "STANDARD",
    icon: <FaStar />,
    description: "You will be able to publish 10 listings with this package",
    quota: 10,
    expirationDuration: "30 days",
    price: 29.99,
  },
  {
    name: "HYPE ME",
    icon: <FaRocket />,
    description: "You will be able to publish 40 listings with this package",
    quota: 40,
    expirationDuration: "60 days",
    price: 99.99,
  },
  {
    name: "PRO",
    icon: <FaGem />,
    description: "You will be able to publish 25 listings with this package",
    quota: 25,
    expirationDuration: "90 days",
    price: 49.99,
  },
  {
    name: "SHOW ME AT FIRST PAGE",
    icon: <FaFireAlt />,
    description: "Your listings will be shown on the first page",
    quota: 100,
    expirationDuration: "30 days",
    price: 149.99,
  },
];

const BuyPackageForm: React.FC<BuyPackageFormProps> = ({ onClose }) => {
  const [selectedPackage, setSelectedPackage] = useState(packages[0]);

  const handlePackageSelection = (pkg: (typeof packages)[0]) => {
    setSelectedPackage(pkg);
  };

  const handleSubmit = (e: React.MouseEvent) => {
    e.preventDefault();
    // buy
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
        onClick={handleSubmit}
        className={styles.submitButton}
      >
        Purchase
      </button>
    </div>
  );
};

export default BuyPackageForm;
