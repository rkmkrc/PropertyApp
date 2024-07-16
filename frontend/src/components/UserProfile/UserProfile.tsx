import React from "react";
import Image from "next/image";
import styles from "./UserProfile.module.css";

type User = {
  name: string;
  surname: string;
  email: string;
  phoneNumber: string;
};

const UserProfile: React.FC<User> = ({ name, surname, email, phoneNumber }) => {
  return (
    <div className={styles.container}>
      <div className={styles.profilePicture}>
        <Image
          src="/pp.jpeg"
          alt={`${name}'s profile`}
          width={80}
          height={80}
        />
      </div>
      <div className={styles.profileDetails}>
        <div className={styles.header}>
          <h2>{`${name} ${surname}`}</h2>
          <button className={styles.editButton}>Edit Profile</button>
          <button className={styles.publicViewButton}>Public View</button>
        </div>
        <div className={styles.email}>
          Email: {email}
          <text className={styles.phone}>Phone: {phoneNumber}</text>
        </div>
      </div>
    </div>
  );
};

export default UserProfile;
