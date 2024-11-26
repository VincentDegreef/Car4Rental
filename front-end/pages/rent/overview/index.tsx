import  Header  from "@/components/Header";
import RentService from "@/services/RentService";
import { useEffect } from "react";
import { useState } from "react";
import { Rent } from "@/types";
import { ConfirmationModal } from "@/components/ConfirmationModal";
import { useRouter } from "next/router";
import NotificationService from "@/services/NotificationService";
import UserService from "@/services/UserService";
import useInterval from "use-interval";
import useSWR, { mutate } from "swr";
import Helpbot from "@/components/helpbot/Helpbot";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import { useTranslation } from "next-i18next";

export const getServerSideProps = async (context: any) => {
  const {locale} = context;

  return {
    props: {
      ...(await serverSideTranslations(locale ?? "en", ['common']))
    },
  };
};

const Overview: React.FC = () => {
  const { t } = useTranslation();

  const [rents, setRents] = useState<Rent[]>([]);
  const [rentToDeleteId, setRentToDeleteId] = useState<number>(0);
  const [rentToDelete, setRentToDelete] = useState<Rent>();
  const [email, setEmail] = useState<string>("");
  const router = useRouter();

  const [errors, setErrors] = useState<string>("");

  const findRentById = (id: number) => {
    setRentToDelete(rents.find((rent) => rent.id == id) as Rent);
  };

  const showPopup = (id: number) => {
    setRentToDeleteId(id);
    findRentById(id);
    document.getElementById("modal")?.classList.remove("hidden");
  };

  const closePopup = () => {
    document.getElementById("modal")?.classList.add("hidden");
    router.push("/rent/overview");
  };

  const cancelRentNotification = async (
    rentId: number,
    renterEmail: string
  ) => {
    await NotificationService.RentCancelNotification(rentId, renterEmail);
  };

  const cancelRent = async () => {
    if (rentToDeleteId != 0) {
      if (sessionStorage.getItem("loggedInUserDetails") === null) {
        return;
      }
      const userDetails = JSON.parse(
        sessionStorage.getItem("loggedInUserDetails") ?? ""
      );
      const userEmail = userDetails.email;
      await cancelRentNotification(rentToDeleteId, userEmail);
      
      RentService.CancelRent(rentToDeleteId);
      
    } else {
      console.error("RentId is not valid.");
    }
    setRents(
      rents.splice(rents.findIndex((rent) => rent.id == rentToDeleteId) - 1, 1)
    );
    closePopup();
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
  };

  const handleSubmit = async () => {
    const rents = await RentService.GetRentsByEmail(email);
    if (email === "") {
      setErrors("You need to choose one or more values to get search results");
      setRents(await RentService.GetRents());
    } else if (rents.length === 0) {
      setErrors("No rents found with the email provided");
      setRents([]);
    } else {
      setErrors("");
      setRents(rents);
    }
  };

  const getUserRents = async () => {
    if (sessionStorage.getItem("loggedInUserDetails") === null) {
      return;
    }
    const userDetails = JSON.parse(
      sessionStorage.getItem("loggedInUserDetails") ?? ""
    );
    const userEmail = userDetails.email;
    const response = await UserService.getUserRentsList(userEmail);
    if (response.ok) {
      const rents = await response.json();
      setRents(rents);
    }
  };
  const { data, isLoading, error } = useSWR("rents", getUserRents);

  useInterval(() => {
    mutate("rents", getUserRents());
  }, 20000);

  const handleCheckOut = (id: number) => {
    router.push(`/rent/checkOut/${id}`);
  };

  const handleComplaint = (id: number) => {
    router.push(`/rent/complaint/${id}`);
  };

    return (
      <>
        <Header />
        <main>
          <div>
            <div>
              <h1 className="text-2xl font-bold text-center my-8">{t("rent.rentOverview")}</h1>
              <div className="flex flex-wrap justify-center max-w-screen-xl mx-auto gap-4 mb-8">
                <div className="flex items-start flex-col">
                  <label htmlFor="email" className="mr-2">{t("rental.email")}</label>
                  <input
                    name='email'
                    type="text"
                    onChange={handleChange}
                    id="email"
                    placeholder={t("rental.emailPlaceholder")}
                    className="px-4 py-2 border border-gray-300 rounded-lg"
                  />
                </div>

                    <button
                onClick={handleSubmit}
                className="bg-blue-500 hover:bg-blue-700 text-white font-bold h-fit px-4 py-2 self-end rounded block"
              >
                      {t("rental.search")}
                    </button>
                        </div>
                  {error && (
              <p className="text-center text-red-500 border-2 border-red-500 rounded-lg p-4 mb-6 w-fit mx-auto">
                {error}
              </p>
            )}
                  {rents.length > 0 ? (
                    <div className="overflow-x-auto w-fit mx-auto">
                      <table className="table-auto w-full text-left whitespace-no-wrap">
                        <thead>
                          <tr className="text-xs font-semibold tracking-wide text-left text-gray-500 uppercase border-b dark:border-gray-700 bg-gray-50">
                            <th className="px-4 py-3">{t("rental.car")}</th>
                            <th className="px-4 py-3">{t("rent.start")}</th>
                            <th className="px-4 py-3">{t("rent.end")}</th>
                            <th className="px-4 py-3">{t("rent.emailOwner")}</th>
                            <th className="px-4 py-3">{t("rent.emailRenter")}</th>
                            <th className="px-4 py-3 text-center" colSpan={3}>
                            {t("rental.actions")}
                      </th>
                          </tr>
                        </thead>
                        <tbody className="bg-white divide-y dark:divide-gray-700 dark:bg-gray-800">
                          {rents.map((rent) => (
                            <tr
                        key={rent.id}
                        className="text-gray-700 dark:text-gray-400 "
                      >
                              <td className="px-4 py-3">
                          {rent.rental.car.brand} {rent.rental.car.model}{" "}
                          {rent.rental.car.licensePlate}
                        </td>
                              <td className="px-4 py-3">{rent.startDate}</td>
                              <td className="px-4 py-3">{rent.endDate}</td>
                              <td className="px-4 py-3">{rent.rental.email}</td>
                              <td className="px-4 py-3">{rent.email}</td>
                        {!rent.endDate && (
                          <>
                                      <td className="px-4 py-3">
                                        <button
                                          onClick={() => showPopup(rent?.id as number)}
                                    className="text-white bg-red-600 hover:bg-red-700 font-medium rounded-lg text-sm px-4 py-2 text-center"
                                  >
                                          {t("rental.cancel")}
                                        </button>
                                      </td>
                                <td className="px-4 py-3">
                                  <button
                                    onClick={() => handleCheckOut(rent?.id as number)}
                                    className="text-white bg-green-600 hover:bg-green-700 font-medium rounded-lg text-sm px-4 py-2 text-center"
                                  >
                                    {t("rent.checkout")}
                                  </button>
                                </td>
                                <td className="px-4 py-3">
                                  <button onClick={() => handleComplaint(rent?.id as number)}
                                  className="text-white bg-blue-600 hover:bg-blue-700 font-medium rounded-lg text-sm px-4 py-2 text-center">
                                    {t("rent.complaint")}
                                  </button>
                                </td>
                          </>
                        )}
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    </div>
                  ) : (
                    <div className="text-center">{t("rent.noRents")}</div>
                  )}
          </div>
              <ConfirmationModal
                title={t("rent.cancelRent")}
                message={`${t("rent.rentAreyouSure")} ${rentToDelete?.rental.car.brand} ${rentToDelete?.rental.car.model} ${t("rent.withLicensePlate")} ${rentToDelete?.rental.car.licensePlate} ${t("rental.from")} ${rentToDelete?.rental.startDate} ${t("rental.to")} ${rentToDelete?.rental.endDate}?`}
                onConfirm={() => cancelRent()}
                onCancel={() => closePopup()}
              />
            </div>
          <div>
            <Helpbot></Helpbot>
          </div>
        </main>
      </>
        
  );
};

export default Overview;
