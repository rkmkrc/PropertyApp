"use client";

import React from "react";
import { ToastContainer } from "react-toastify";
import { showToast } from "@/lib/toast";
import "react-toastify/dist/ReactToastify.css";
import styles from "./login.module.css";

const LoginPage: React.FC = () => {
  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const form = event.currentTarget;
    const formData = new FormData(form);

    try {
      const response = await fetch("/api/login", {
        method: "POST",
        body: formData,
      });

      const resData = await response.json();

      if (resData.success) {
        showToast(resData.message, "success");
      } else {
        showToast(resData.message, "error");
      }
    } catch (error) {
      showToast("An error occurred during login", "error");
    }
  };

  return (
    <>
      <ToastContainer />
      <div className={styles.container}>
        <form className={styles.form} onSubmit={handleSubmit}>
          <h1 className={styles.title}>Login</h1>
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
            Login
          </button>
        </form>
      </div>
    </>
  );
};

export default LoginPage;
