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
    <div className={styles.listingDetailsContainer}>
      {images.length > 0 && <Carousel images={images} title={listing.title} />}
      <div className={styles.detailsContent}>
        <h1>{listing.title}</h1>
        <p>{listing.description}</p>
        <p>Type: {listing.type}</p>
        <p>Price: {listing.price} TL</p>
        <p>Status: {listing.status}</p>
        <p>Area: {listing.area} sq mt</p>
        <p>
          Published on: {new Date(listing.publishedDate).toLocaleDateString()}
        </p>
      </div>
    </div>
  );
};

export default ListingDetailsPage;
