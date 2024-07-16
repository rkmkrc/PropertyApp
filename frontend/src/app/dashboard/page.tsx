import { cookies } from "next/headers";
import { redirect } from "next/navigation";

async function fetchUserData(token: string) {
  const response = await fetch("http://localhost:3000/api/user", {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return response.json();
}

export default async function DashboardPage() {
  const cookieStore = cookies();
  const token = cookieStore.get("token")?.value;

  if (!token) {
    redirect("/login"); // Redirect to login if no token is found
  }

  const data = await fetchUserData(token);

  if (!data.success) {
    return <div>Failed to load user data: {data.message}</div>;
  }

  const user = data.user;

  return (
    <div>
      <h1>Welcome, {user.name}!</h1>
      <p>Email: {user.email}</p>
      {/* Render other user-specific content here */}
    </div>
  );
}
