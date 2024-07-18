// src/app/dashboard/page.tsx

import UserProfile from "@/components/UserProfile/UserProfile";
import styles from "./DashboardPage.module.css";
import { cookies } from "next/headers";
import { redirect } from "next/navigation";
import DashboardContent from "@/components/DashboardContent/DashboardContent";

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
      <DashboardContent listings={listings} packages={packages} />
    </div>
  );
};

export default DashboardPage;
