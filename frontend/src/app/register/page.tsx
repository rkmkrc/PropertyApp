"use client";

import React from "react";
import { ToastContainer } from "react-toastify";
import { showToast } from "@/lib/toast";
import "react-toastify/dist/ReactToastify.css";
import styles from "./register.module.css";
import Link from "next/link";
import { useRouter } from "next/navigation";

const RegisterPage: React.FC = () => {
  const router = useRouter();

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const form = event.currentTarget;
    const formData = new FormData(form);

    try {
      const response = await fetch("/api/register", {
        method: "POST",
        body: formData,
      });

      const resData = await response.json();

      if (resData.success) {
        showToast(resData.message, "success");
        router.push("/dashboard");
      } else {
        showToast(resData.message, "error");
      }
    } catch (error) {
      showToast("An error occurred during registration", "error");
    }
  };

  return (
    <>
      <ToastContainer />
      <div className={styles.container}>
        <form className={styles.form} onSubmit={handleSubmit}>
          <h1 className={styles.title}>Register</h1>
          <input
            type="text"
            name="name"
            placeholder="Name"
            className={styles.input}
            required
          />
          <input
            type="text"
            name="surname"
            placeholder="Surname"
            className={styles.input}
            required
          />
          <input
            type="email"
            name="email"
            placeholder="Email"
            className={styles.input}
            required
          />
          <input
            type="password"
            name="password"
            placeholder="Password"
            className={styles.input}
            required
          />
          <button type="submit" className={styles.button}>
            Register
          </button>
          <p className={styles.accountText}>
            Already have an account?{" "}
            <Link href="/login" className={styles.loginLink}>
              Login here
            </Link>
          </p>
        </form>
      </div>
    </>
  );
};

export default RegisterPage;
