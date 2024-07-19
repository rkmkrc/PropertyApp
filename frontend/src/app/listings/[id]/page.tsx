import React from "react";
import styles from "./ListingDetails.module.css";
import Carousel from "@/components/Carousel/Carousel"; // Adjust the import path as necessary

const ListingDetailsPage = async ({ params }: { params: { id: number } }) => {
  const id = params.id;
  const fetchListing = fetch(`http://localhost:8080/api/v1/listings/${id}`, {
    method: "GET",
  });

  const listingResponse = await fetchListing;
  let listing = null;

  if (listingResponse.ok) {
    const listingData = await listingResponse.json();
    listing = listingData.data;
  }

  if (!listing) {
    return <p>Listing not found</p>;
  }

  const images = [
    "/land.png",
    "/house.png",
    "/flat.png",
    "/land.png",
    "/store.png",
  ];

  return (
    <>
      <h1 className={styles.title}>{listing.title}</h1>
      <div className={styles.listingDetailsContainer}>
        {images.length > 0 && (
          <Carousel images={images} title={listing.title} />
        )}
        <div className={styles.detailsContent}>
          <h2 className={styles.details}>Listing Details</h2>
          <div className={styles.detailItem}>
            <span className={styles.label}>Type:</span>
            <span className={styles.value}>{listing.type}</span>
          </div>
          <div className={styles.detailItem}>
            <span className={styles.label}>Price:</span>
            <span className={styles.value}>{listing.price} TL</span>
          </div>
          <div className={styles.detailItem}>
            <span className={styles.label}>Status:</span>
            <span className={styles.value}>{listing.status}</span>
          </div>
          <div className={styles.detailItem}>
            <span className={styles.label}>Area:</span>
            <span className={styles.value}>{listing.area} sq mt</span>
          </div>
          <div className={styles.detailItem}>
            <span className={styles.label}>Published on:</span>
            <span className={styles.value}>
              {new Date(listing.publishedDate).toLocaleDateString()}
            </span>
          </div>
          <div className={styles.detailItem}>
            <span className={styles.label}>Description:</span>
            <span className={styles.value}>{listing.description}</span>
          </div>
        </div>
      </div>
    </>
  );
};

export default ListingDetailsPage;
