"use client";

import Link from "next/link";
import styles from "./Navbar.module.css";
import LogoutButton from "../LogoutButton/LogoutButton";
import { useAuth } from "@/context/AuthContext";

const Navbar = () => {
  const { isAuthenticated } = useAuth();

  return (
    <nav className={styles.navbar}>
      <div className={styles.navbarContainer}>
        <Link href="/" className={styles.logo}>
          MyApp
        </Link>
        <ul className={styles.navLinks}>
          {isAuthenticated ? (
            <>
              <li>
                <Link href="/dashboard">Dashboard</Link>
              </li>
              <li>
                <Link href="/settings">Settings</Link>
              </li>
              <li>
                <LogoutButton />
              </li>
            </>
          ) : (
            <>
              <li>
                <Link href="/login">Sign In</Link>
              </li>
              <li>
                <Link href="/register">Register</Link>
              </li>
            </>
          )}
        </ul>
      </div>
    </nav>
  );
};

export default Navbar;
