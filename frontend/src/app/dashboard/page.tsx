import { GetServerSidePropsContext } from "next";
import { parseCookies } from "nookies";

export async function getServerSideProps(context: GetServerSidePropsContext) {
  const { req } = context;
  const cookies = parseCookies({ req });
  const token = cookies.token || "";

  if (!token) {
    return {
      redirect: {
        destination: "/login",
        permanent: false,
      },
    };
  }

  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/verify-token`,
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );

  if (!response.ok) {
    return {
      redirect: {
        destination: "/login",
        permanent: false,
      },
    };
  }

  return {
    props: {}, // pass any necessary data to the page component
  };
}

const Dashboard = () => {
  return <div>Welcome to the Dashboard!</div>;
};

export default Dashboard;
