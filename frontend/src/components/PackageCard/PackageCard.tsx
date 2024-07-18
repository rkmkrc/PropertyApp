// components/PackageCard/PackageCard.tsx

import React, { useState } from "react";
import styles from "./PackageCard.module.css";
import { FaStar, FaRocket, FaGem } from "react-icons/fa";
import Modal from "../Modal/Modal";
import AddListingForm from "../AddListingForm/AddListingForm";
import BuyPackageForm from "../BuyPackageForm/BuyPackageForm";

type Pkg = {
  title: string;
  type: string;
  description: string;
  isTemplate?: boolean;
  expirationDate: string;
};

const getIcon = (type: string) => {
  switch (type.toUpperCase()) {
    case "STANDARD":
      return <FaStar className={styles.icon} />;
    case "HYPE_ME":
      return <FaRocket className={styles.icon} />;
    case "PRO":
      return <FaGem className={styles.icon} />;
    default:
      return <FaStar className={styles.icon} />;
  }
};

const PackageCard: React.FC<Pkg> = ({
  title,
  type,
  description,
  isTemplate = false,
  expirationDate,
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleModalOpen = () => {
    setIsModalOpen(true);
  };

  const handleModalClose = () => {
    setIsModalOpen(false);
  };

  if (isTemplate) {
    const handleBuyPackage = async (packageName: string) => {
      console.log("buy package");
      handleModalClose();
    };

    return (
      <div
        className={`${styles.card} ${styles.templateCard}`}
        onClick={handleModalOpen}
      >
        <div className={styles.plusIcon}>+</div>
        <Modal isOpen={isModalOpen} onClose={handleModalClose}>
          <BuyPackageForm onClose={handleModalClose} />
        </Modal>
      </div>
    );
  }

  return (
    <div className={styles.card}>
      <div className={styles.details}>
        <div className={styles.titleContainer}>
          {getIcon(type)} {/* Get icon based on type */}
          <h2 className={styles.title}>{title}</h2>
        </div>
        <p className={styles.type}>{type}</p>
        <p className={styles.description}>{description}</p>
        <p className={styles.date}>
          Expiration Date: {new Date(expirationDate).toLocaleDateString()}
        </p>
      </div>
    </div>
  );
};

export default PackageCard;
