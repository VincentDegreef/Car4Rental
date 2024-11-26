import React, { useEffect } from "react";

import { useRouter } from "next/router";
import { useState } from "react";
import Image from "next/image";
import UserService from "@/services/UserService";
import { get } from "http";
import useInterval from "use-interval";
import useSWR, { mutate } from "swr";
import { GrLogout } from "react-icons/gr";
import Link from "next/link";
import Language from "./Language";
import { useTranslation } from "next-i18next";



const Header: React.FC = () => {
  const {t} = useTranslation();

  const router = useRouter();
  const [isCarDropdownOpen, setIsCarDropdownOpen] = useState(false);
  const [isProfileDropdownOpen, setIsProfileDropdownOpen] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [balance, setBalance] = useState<number>(0);
  const [loggedInRole, setLoggedInRole] = useState("");
  const [userName, setUserName] = useState("");

  const handleCarDropdownHover = (isOpen: boolean) => {
    setIsCarDropdownOpen(isOpen);
  };

  const handleProfileDropdownHover = (isOpen: boolean) => {
    setIsProfileDropdownOpen(isOpen);
  };

  const getUserBalance = async () => {
    if (sessionStorage.getItem("loggedInUserToken") === null) {
      setIsLoggedIn(false);
      return;
    }
    const userDetails = JSON.parse(
      sessionStorage.getItem("loggedInUserDetails") || ""
    );
    

    const username = userDetails.username;
    const balanceResponse = await UserService.getBalance(username);
    const balanceData = await balanceResponse.json();
    setBalance(balanceData.balance);
    return balanceData.balance; // Return the balance for SWR
  };

  const { data, isLoading, error } = useSWR("getbalance", getUserBalance);

  useInterval(() => {
    mutate("getbalance", getUserBalance());
  }, 20000);

  useEffect(() => {
    if (sessionStorage.getItem("loggedInUserToken") === null) {
      setIsLoggedIn(false);
      return;
    }
    const userDetails = JSON.parse(
      sessionStorage.getItem("loggedInUserDetails") || ""
    );
    setUserName(userDetails.username);
    setLoggedInRole(userDetails.role.name);
    setIsLoggedIn(true);
  }, [loggedInRole]); // Add loggedInRole as a dependency

  const handleClick = () => {
    sessionStorage.removeItem("loggedInUserToken");
    setIsLoggedIn(false);
    setLoggedInRole("");
    router.push("/");
  };

  return (
    <header className="bg-black text-white text-center mx-auto z-10 w-full">
      <div className="max-w-screen-xl mx-auto p-4">
        <div className="flex justify-between items-center">
          <Image
            src="/car4rent.webp"
            alt="Car4Rent"
            width={200}
            height={100}
            className="cursor-pointer border-b-2 border-black transition hover:border-white w-60"
            onClick={() => router.push("/")}
          />
          <div>
            <div className="flex justify-center items-center gap-12">
              {isLoggedIn && (
                <div
                  className="relative"
                  onMouseEnter={() => handleCarDropdownHover(true)}
                  onMouseLeave={() => handleCarDropdownHover(false)}
                >
                  <button
                    className="border-b-2 border-black transition text-lg"
                    onClick={() => router.push("/car/overview")}
                  >
                    {isLoggedIn ? t("header.cars") : t("header.cars") + "&#9662;"}
                  </button>
                  {loggedInRole !== "RENTER" && (
                    <div
                      className={`absolute dropdown z-10 right-0 py-2 bg-white rounded-lg shadow-md w-max ${
                        isCarDropdownOpen ? "" : "hidden"
                      }`}
                    >
                      <button
                        className="block px-4 py-2 text-gray-800 hover:bg-gray-200"
                        onClick={() => router.push("/car/add")}
                      >
                        {t("header.addCar")}
                      </button>
                    </div>
                  )}
                </div>
              )}
              {isLoggedIn && (
                <div className="relative">
                  <button
                    className="border-b-2 border-black transition text-lg"
                    onClick={() => router.push("/rental/overview")}
                  >
                    {t("header.rentals")}
                  </button>
                </div>
              )}
              {isLoggedIn && (
                <div className="relative">
                  <button
                    className="border-b-2 border-black transition text-lg"
                    onClick={() => router.push("/rent/overview")}
                  >
                    {t("header.rents")}
                  </button>
                </div>
              )}
              {!isLoggedIn && (
                <div>
                  <button
                    className="border-b-2 border-black transition text-lg"
                    onClick={() => router.push("/user/register")}
                  >
                    {t("header.register")}
                  </button>
                </div>
              )}
              {!isLoggedIn && (
                <div>
                  <button
                    className="border-b-2 border-black transition text-lg"
                    onClick={() => router.push("/user/login")}
                  >
                    {t("header.login")}
                  </button>
                </div>
              )}
              {isLoggedIn && <div><p>{}</p></div>}
              {isLoggedIn && (
                <div
                  className="relative"
                  onMouseEnter={() => handleProfileDropdownHover(true)}
                  onMouseLeave={() => handleProfileDropdownHover(false)}
                >
                  <button className="border-b-2 border-black transition text-lg flex items-center gap-4 bg-stone-800 p-3 rounded-full">
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="30"
                      height="30"
                      viewBox="0 0 24 24"
                      strokeWidth="1.5"
                      stroke="#ffffff"
                      fill="none"
                      strokeLinecap="round"
                      strokeLinejoin="round"
                    >
                      <path stroke="none" d="M0 0h24v24H0z" fill="none" />
                      <path d="M12 12m-9 0a9 9 0 1 0 18 0a9 9 0 1 0 -18 0" />
                      <path d="M12 10m-3 0a3 3 0 1 0 6 0a3 3 0 1 0 -6 0" />
                      <path d="M6.168 18.849a4 4 0 0 1 3.832 -2.849h4a4 4 0 0 1 3.834 2.855" />
                    </svg>
                    €{balance}
                  </button>
                  <div
                    className={`absolute dropdown z-10 right-0 py-2 bg-white rounded-lg shadow-md ${
                      isProfileDropdownOpen ? "" : "hidden"
                    }`}
                  >
                    <button
                      className="block px-4 py-2 text-gray-800 hover:bg-gray-200"
                      onClick={() =>
                        router.push(
                          `/user/${
                            JSON.parse(
                              sessionStorage.getItem("loggedInUserDetails") ||
                                ""
                            ).id
                          }`
                        )
                      }
                    >
                      {t("header.viewProfile")}
                    </button>
                    <button
                      className="block px-4 py-2 text-gray-800 hover:bg-gray-200"
                      onClick={() => router.push("/notifications")}
                    >
                      {t("header.notifications")}
                    </button>
                    {loggedInRole === "ADMIN" && (
                      <button
                        className="block px-4 py-2 text-gray-800 hover:bg-gray-200 whitespace-nowrap"
                        onClick={() => router.push("/adminpanel")}
                      >
                        {t("header.adminPanel")}
                      </button>
                    )}
                  </div>
                </div>
              )}
              {isLoggedIn &&(<div className="flex flex-row gap-2 justify-center items-center cursor-pointer">
                <GrLogout onClick={handleClick} size={20} style={{ cursor: 'pointer'}} />
                <p className="text-lg">{t("header.logout")}</p>
              </div>)}
              <Language/>
            </div>
          </div>
        </div>
      </div>
    </header>
  );
};


export default Header;
