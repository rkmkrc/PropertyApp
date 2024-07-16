import React from "react";
import styles from "./register.module.css";

const RegisterPage: React.FC = () => {
  return (
    <div className={styles.container}>
      <form method="POST" action="/api/register" className={styles.form}>
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
      </form>
    </div>
  );
};

export default RegisterPage;
