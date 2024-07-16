// components/PackageCard/PackageCard.tsx

import React from "react";
import styles from "./PackageCard.module.css";
import { FaStar, FaRocket, FaGem } from "react-icons/fa";

type Package = {
  title: string;
  type: string;
  description: string;
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

const PackageCard: React.FC<Package> = ({
  title,
  type,
  description,
  expirationDate,
}) => {
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
