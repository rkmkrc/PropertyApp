"use client";

import { useRouter } from "next/navigation";
import styles from "./LogoutButton.module.css";
import { useAuth } from "@/context/AuthContext";

const LogoutButton: React.FC = () => {
  const router = useRouter();
  const { logout } = useAuth();

  const handleLogout = async () => {
    const response = await fetch("/api/logout", {
      method: "GET",
    });
    logout();
    if (response.ok) {
      router.push("/login"); // Redirect to login page after logout
    }
  };

  return (
    <span onClick={handleLogout} className={styles.navLink}>
      Logout
    </span>
  );
};

export default LogoutButton;
