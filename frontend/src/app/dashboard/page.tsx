import UserProfile from "@/components/UserProfile/UserProfile";
import { redirect } from "next/navigation";
import styles from "./DashboardPage.module.css";
import ListingCard from "@/components/ListingCard/ListingCard";
import PackageCard from "@/components/PackageCard/PackageCard";
import { cookies } from "next/headers";

const DashboardPage = async () => {
  const cookieStore = cookies();
  const token = cookieStore.get("token")?.value;

  if (!token) {
    redirect("/login"); // Redirect to login if no token is found
    return null;
  }

  const fetchUser = fetch(`http://localhost:8080/api/v1/users/user`, {
    cache: "no-store",
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  const fetchListings = fetch(`http://localhost:8080/api/v1/users/listings`, {
    cache: "no-store",
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  const fetchPackages = fetch(`http://localhost:8080/api/v1/users/packages`, {
    cache: "no-store",
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  const [userResponse, listingsResponse, packagesResponse] = await Promise.all([
    fetchUser,
    fetchListings,
    fetchPackages,
  ]);

  let user = null;
  let listings = null;
  let packages = null;

  if (userResponse.ok) {
    const userData = await userResponse.json();
    user = userData.data;
  }

  if (listingsResponse.ok) {
    const listingsData = await listingsResponse.json();
    listings = listingsData.data;
  }

  if (packagesResponse.ok) {
    const packagesData = await packagesResponse.json();
    packages = packagesData.data;
  }

  return (
    <div className={styles.dashboardContainer}>
      {user ? <UserProfile {...user} /> : <p>User not found</p>}
      <h2>Portfolio</h2>
      {listings ? (
        <div className={styles.gridContainer}>
          {listings.map((listing: any) => (
            <ListingCard key={listing.id} {...listing} />
          ))}
        </div>
      ) : (
        <p>Listings not found</p>
      )}
      <h2>Packages</h2>
      {packages ? (
        <div className={styles.packageGridContainer}>
          {packages.map((pkg: any) => (
            <PackageCard key={pkg.title} {...pkg} />
          ))}
        </div>
      ) : (
        <p>Packages not found</p>
      )}
    </div>
  );
};

export default DashboardPage;
