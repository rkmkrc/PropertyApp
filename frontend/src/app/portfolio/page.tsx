import React from "react";
import { cookies } from "next/headers";
import { redirect } from "next/navigation";
import PortfolioPageClient from "./PortfolioPageClient";

const PortfolioPage = async () => {
  const cookieStore = cookies();
  const token = cookieStore.get("token")?.value;

  if (!token) {
    redirect("/login"); // Redirect to login if no token is found
    return null;
  }

  const fetchListings = fetch(`http://localhost:8080/api/v1/users/listings`, {
    cache: "no-store",
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  const listingsResponse = await fetchListings;
  let listings = [];

  if (listingsResponse.ok) {
    const listingsData = await listingsResponse.json();
    listings = listingsData.data;
  }

  return <PortfolioPageClient listings={listings} />;
};

export default PortfolioPage;
