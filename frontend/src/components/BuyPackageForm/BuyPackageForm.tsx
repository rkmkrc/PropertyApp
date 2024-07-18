"use client";

import React, { useState } from "react";
import styles from "./BuyPackageForm.module.css";
import { FaStar, FaRocket, FaGem } from "react-icons/fa";

type BuyPackageFormProps = {
  onClose: () => void;
};

const packages = [
  {
    name: "STANDARD",
    icon: <FaStar />,
    description: "Basic package with standard features.",
    quota: 10,
    expirationDuration: "30 days",
  },
  {
    name: "HYPE_ME",
    icon: <FaRocket />,
    description: "Boost your visibility with this package.",
    quota: 20,
    expirationDuration: "60 days",
  },
  {
    name: "PRO",
    icon: <FaGem />,
    description: "Professional package with premium features.",
    quota: 50,
    expirationDuration: "90 days",
  },
  {
    name: "SHOW ME AT FIRST PAGE",
    icon: <FaStar />,
    description: "Get top visibility with this package.",
    quota: 100,
    expirationDuration: "120 days",
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
            <div className={styles.packageQuota}>Quota: {pkg.quota}</div>
            <div className={styles.packageExpiration}>
              Expiration: {pkg.expirationDuration}
            </div>
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
