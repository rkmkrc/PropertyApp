import Image from "next/image";
import styles from "./page.module.css";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "next/navigation"; // Ensure you import from "next/navigation" in App Router

export default function Home() {
  return (
    <>
      <ToastContainer />
      <div>
        <h1>Home</h1>
      </div>
    </>
  );
}
