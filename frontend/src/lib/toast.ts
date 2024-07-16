import { toast, ToastOptions } from "react-toastify";

export const showToast = (
  message: string,
  type: "success" | "error",
  autoClose: number = 2500
) => {
  const commonOptions: ToastOptions = {
    position: "top-center",
    autoClose,
    hideProgressBar: false,
    closeOnClick: true,
    pauseOnHover: true,
    draggable: true,
    progress: undefined,
    theme: "colored",
  };

  if (type === "success") {
    toast.success(message, commonOptions);
  } else if (type === "error") {
    toast.error(message, commonOptions);
  }
};
