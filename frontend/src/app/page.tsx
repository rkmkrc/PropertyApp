import styles from "./page.module.css";
import ListingCard from "@/components/ListingCard/ListingCard";

const HomePage = async () => {
  try {
    const listingResponse = await fetch(
      `http://localhost:8080/api/v1/listings/active`,
      {
        cache: "no-store",
        method: "GET",
      }
    );

    if (!listingResponse.ok) {
      throw new Error(`HTTP error! status: ${listingResponse.status}`);
    }

    const listingData = await listingResponse.json();

    if (!listingData.success) {
      throw new Error(listingData.message || "Failed to fetch listings");
    }

    const listings = listingData.data;

    return (
      <div className={styles.homeContainer}>
        <h2>All Listings</h2>
        <div className={styles.gridContainer}>
          {listings.map((listing: any) => (
            <ListingCard isEditable={false} key={listing.id} {...listing} />
          ))}
        </div>
      </div>
    );
  } catch (error: any) {
    return (
      <div className={styles.error}>
        Failed to load listing data: {error.message}
      </div>
    );
  }
};

export default HomePage;
