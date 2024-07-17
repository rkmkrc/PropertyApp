import styles from "./page.module.css";
import ListingCard from "@/components/ListingCard/ListingCard";

const HomePage: React.FC = async () => {
  const listingResponse = await fetch(
    `http://localhost:8080/api/v1/listings/active`,
    {
      cache: "no-store",
      method: "GET",
    }
  );

  const listingData = await listingResponse.json();

  if (!listingData.success) {
    return <div>Failed to load listing data: {listingData.message}</div>;
  }

  const listings = listingData.data;

  return (
    <div className={styles.homeContainer}>
      <h2>All Listings</h2>
      <div className={styles.gridContainer}>
        {listings.map((listing: any) => (
          <ListingCard key={listing.id} {...listing} />
        ))}
      </div>
    </div>
  );
};

export default HomePage;
