import UserProfile from "@/components/UserProfile/UserProfile";
import { cookies } from "next/headers";
import { redirect } from "next/navigation";
import styles from "./DashboardPage.module.css";
import ListingCard from "@/components/ListingCard/ListingCard";
import PackageCard from "@/components/PackageCard/PackageCard";

const DashboardPage: React.FC = async () => {
  const cookieStore = cookies();
  const token = cookieStore.get("token")?.value;

  if (!token) {
    redirect("/login"); // Redirect to login if no token is found
  }

  const userResponse = await fetch(`http://localhost:8080/api/v1/users/user`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  const listingsResponse = await fetch(
    `http://localhost:8080/api/v1/users/listings`,
    {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );

  const packagesResponse = await fetch(
    `http://localhost:8080/api/v1/users/packages`,
    {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );

  const userData = await userResponse.json();
  const listingsData = await listingsResponse.json();
  const packagesData = await packagesResponse.json();

  console.log(userData);
  console.log(listingsData);
  console.log(packagesData);

  if (!userData.success) {
    return <div>Failed to load user data: {userData.message}</div>;
  }
  if (!listingsData.success) {
    return <div>Failed to load listings data: {listingsData.message}</div>;
  }
  if (!packagesData.success) {
    return <div>Failed to load packages data: {packagesData.message}</div>;
  }

  const user = userData.data;
  const listings = listingsData.data;
  const packages = packagesData.data;

  return (
    <div>
      <h1>Dashboard</h1>
      <div className={styles.dashboardContainer}>
        <UserProfile {...user} />
        <h2>Portfolio</h2>
        <div className={styles.gridContainer}>
          {listings.map((listing: any) => (
            <ListingCard key={listing.id} {...listing} />
          ))}
        </div>
        <h2>Packages</h2>
        <div className={styles.packageGridContainer}>
          {packages.map((pkg: any) => (
            <PackageCard key={pkg.title} {...pkg} />
          ))}
        </div>
      </div>
    </div>
  );
};

export default DashboardPage;
