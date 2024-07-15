import React from "react";
import styles from "./login.module.css";

const LoginPage: React.FC = () => {
  return (
    <div className={styles.container}>
      <form method="POST" action="/api/login" className={styles.form}>
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
  );
};

export default LoginPage;
