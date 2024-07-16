// components/ListingCard/ListingCard.tsx

import React from "react";
import Image from "next/image";
import styles from "./ListingCard.module.css";

type Listing = {
  id: number;
  title: string;
  description: string;
  type: string;
  price: number;
  status: string;
  area: number;
  publishedDate: string;
};

const formatPrice = (price: number) => {
  return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
};

const ListingCard: React.FC<Listing> = ({
  title,
  description,
  type,
  price,
  status,
  area,
  publishedDate,
}) => {
  const formattedPrice = formatPrice(price);
  return (
    <div className={styles.card}>
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
          <span>
            <div
              className={`${styles.statusIndicator} ${
                status.toLowerCase() === "active"
                  ? styles.active
                  : styles.inactive
              }`}
            />
          </span>
          <span>{status}</span>
          <p className={styles.date}>
            Published on: {new Date(publishedDate).toLocaleDateString()}
          </p>
        </div>
      </div>
    </div>
  );
};

export default ListingCard;
