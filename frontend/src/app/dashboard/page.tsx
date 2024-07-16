import UserProfile from "@/components/UserProfile/UserProfile";
import { cookies } from "next/headers";
import { redirect } from "next/navigation";

const DashboardPage: React.FC = async () => {
  const cookieStore = cookies();
  const token = cookieStore.get("token")?.value;

  if (!token) {
    redirect("/login"); // Redirect to login if no token is found
  }

  const response = await fetch(`http://localhost:8080/api/v1/users/user`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  const data = await response.json();
  console.log(data);
  if (!data.success) {
    return <div>Failed to load user data: {data.message}</div>;
  }

  const user = data.data;

  return (
    <div>
      <h1>Dashboard</h1>
      <UserProfile {...user} />
    </div>
  );
};

export default DashboardPage;
