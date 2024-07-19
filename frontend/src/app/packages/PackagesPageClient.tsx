"use client";

import React from "react";
import PackageCard from "@/components/PackageCard/PackageCard";
import styles from "./packages.module.css";

type PackagesPageClientProps = {
  packages: any[];
};

const PackagesPageClient: React.FC<PackagesPageClientProps> = ({
  packages,
}) => {
  return (
    <div className={styles.packagesPageContainer}>
      <h1>My Packages</h1>
      {packages.length > 0 ? (
        <div className={styles.gridContainer}>
          {packages.map((pkg: any) => (
            <PackageCard key={pkg.title} {...pkg} />
          ))}
          <PackageCard
            isTemplate={true}
            title={"Buy a package"}
            description={""}
            type={""}
            expirationDate={""}
          />
        </div>
      ) : (
        <p>Packages not found</p>
      )}
    </div>
  );
};

export default PackagesPageClient;
