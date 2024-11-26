import  Header from "@/components/Header";
import Link from "next/link";
import { useEffect, useState } from "react";
import Helpbot from "@/components/helpbot/Helpbot";
import { useTranslation } from "next-i18next";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";

export const getServerSideProps = async (context: any) => {
  const {locale} = context;

  return {
    props: {
      ...(await serverSideTranslations(locale ?? "en", ['common']))
    },
  };
};

const Home: React.FC = () => {
  const {t} = useTranslation();

  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [loggedInUser, setLoggedInUser] = useState<string | null>(null);

  useEffect(() => {
    if (sessionStorage.getItem("loggedInUserToken") === null) {
      setIsLoggedIn(false);
      setLoggedInUser(null);
      return;
    }
    const userDetails = JSON.parse(
      sessionStorage.getItem("loggedInUserDetails") || ""
    );
    setLoggedInUser(userDetails.username);
    setIsLoggedIn(true);
  }, []);



  return (
    <div className="min-h-screen">
      <Header />
      <main>
        <div className="text-white bg-cover bg-center h-full bg-[url('https://images.pexels.com/photos/6147/cars-red-car-parking.jpg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1')]">
            <div className="backdrop-brightness-[30%] w-full h-full py-32 flex items-center justify-center">
            <div className="mx-auto p-4 max-w-screen-xl text-center">
                <h1 className="text-3xl font-bold mb-4">{t("home.welcome")}</h1>
                <p className="text-lg mb-12 max-w-[600px] mx-auto">
                    {t("home.description")}
                </p>
                {isLoggedIn && (
                  <p className="text-2xl font-bold">{t("home.welcomeUser")} {loggedInUser}</p>
                )}
                {!isLoggedIn && (
                  <Link href="/user/login" className="bg-white text-black py-4 px-8 rounded transition hover:bg-zinc-800 hover:text-white">{t("home.cta")}</Link>
                )}
            </div>
            </div>
        </div>
        {isLoggedIn && (
      <div className="mx-auto p-4 max-w-screen-xl grid grid-cols-5 gap-8 mt-8">
        <div className="col-span-3">
        <h2 className="text-2xl font-bold mb-4">{t("home.checkoutFollowingPages")}</h2>
          <ul className="text-lg mb-4 grid grid-cols-4 gap-4">
            <Link href="/car/overview" className="w-full bg-black text-white py-8 rounded flex justify-center items-center transition hover:bg-zinc-800">{t("home.carOverview")}</Link>
            <Link href="/rental/overview" className="w-full bg-black text-white py-8 rounded flex justify-center items-center transition hover:bg-zinc-800">{t("home.rentalOverview")}</Link>
            <Link href="/rent/overview" className="w-full bg-black text-white py-8 rounded flex justify-center items-center transition hover:bg-zinc-800">{t("home.rentOverview")}</Link>
          </ul>
        </div>
      </div>)}
      <div>
        <Helpbot></Helpbot>
      </div>
    </main>
    </div>
  );
}

export default Home;