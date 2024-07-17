// utils/auth.ts

import { cookies } from "next/headers";

export function isAuthenticated(): boolean {
  const token = cookies().get("token");
  console.log(cookies())
  return !!token;
}
