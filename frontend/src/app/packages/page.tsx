import React from "react";
import { cookies } from "next/headers";
import { redirect } from "next/navigation";
import PackagesPageClient from "./PackagesPageClient";

const PackagesPage = async () => {
  const cookieStore = cookies();
  const token = cookieStore.get("token")?.value;

  if (!token) {
    redirect("/login"); // Redirect to login if no token is found
    return null;
  }

  const fetchPackages = fetch(`http://localhost:8080/api/v1/users/packages`, {
    cache: "no-store",
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  const packagesResponse = await fetchPackages;
  let packages = [];

  if (packagesResponse.ok) {
    const packagesData = await packagesResponse.json();
    packages = packagesData.data;
  }

  return <PackagesPageClient packages={packages} />;
};

export default PackagesPage;
