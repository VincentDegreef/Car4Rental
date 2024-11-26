import React, { useState, useEffect } from "react";
import CarService from "@/services/CarService";
import Header from "@/components/Header";
import { useRouter } from "next/router";
import { Car } from "@/types";
import RentalService from "@/services/RentalService";
import { ConfirmationModal } from "@/components/ConfirmationModal";
import Helpbot from "@/components/helpbot/Helpbot";
import { useTranslation } from "next-i18next";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import AlertDialog from "@/components/AlertDialog";

export const getServerSideProps = async (context: any) => {
  const { locale } = context;

  return {
    props: {
      ...(await serverSideTranslations(locale ?? "en", ['common']))
    },
  };
};

const CarOverview: React.FC = () => {
  const { t } = useTranslation('common');
  const [cars, setCars] = useState<Car[]>([]);
  const [rentedCarIds, setRentedCars] = useState<number[]>([]);
  const [carToDeleteId, setCarToDeleteId] = useState<number>(0);
  const [carToDelete, setCarToDelete] = useState<Car>();
  const [userRole, setUserRole] = useState<string>("");
  const [moreDetails, setMoreDetails] = useState<boolean>(false);
  const [userEmail, setUserEmail] = useState<string>("");
  const [alertMessage, setAlertMessage] = useState<string>("");

  const router = useRouter();

  const findCarById = (id: number) => {
    setCarToDelete(cars.find(car => car.id == id) as Car);
  }

  const handleRental = (carId: number, email: string) => {
    if(userEmail !== email) {
      setAlertMessage(t("carOverview.notAuthorizedToRental"));
      return;
    }  
    sessionStorage.setItem("carId", String(carId));
    router.push("/car/add/rental");
  };

  const showPopup = (carId: number, email: string) => {
    if(userEmail !== email) {
      setAlertMessage(t("carOverview.notAuthorizedToDelete"));
      return;
    }
    setCarToDeleteId(carId);
    findCarById(carId);
    document.getElementById("modal")?.classList.remove("hidden");
  };

  const closePopup = () => {
    setCarToDeleteId(0);
    document.getElementById("modal")?.classList.add("hidden");
  };

  const handleDelete = () => {
    if (carToDeleteId != 0) {
      CarService.DeleteCar(carToDeleteId);
    } else {
      console.error("CarId is not valid.");
    }
    setCars(cars.splice(cars.findIndex(car => car.id == carToDeleteId)-1, 1));
    closePopup();
  };

  const handleMoreDetails = () => {
    setMoreDetails(!moreDetails);
  };

  useEffect(() => {
    if(sessionStorage.getItem("loggedInUserDetails") != null) {
      const userDetails = JSON.parse(
        sessionStorage.getItem("loggedInUserDetails") || ""
      );
      setUserRole(userDetails.role.name);
      setUserEmail(userDetails.email);
    }
    CarService.GetCars()
      .then((fetchedCars) => {
        if (Array.isArray(fetchedCars)) {
          setCars(fetchedCars);
        } else {
          console.error("Received data is not an array:", fetchedCars);
        }
      })
      .catch((error) => {
        console.error(error);
      });

    RentalService.GetRentals().then((fetchedRentals) => {
      if (Array.isArray(fetchedRentals)) {
        setRentedCars(fetchedRentals.map((rental) => rental.car.id));
      } else {
        console.error("Received data is not an array:", fetchedRentals);
      }
    });
  }, []);

  return (
    <>
      <Header />
      <main>
        <div>
          <div>
            <h1 className="text-2xl font-bold text-center my-8">{t("carOverview.title")}</h1>
            {cars.length > 0 ? (
              <div className="overflow-x-auto w-fit mx-auto">
                <table className="table-auto w-full text-left whitespace-no-wrap">
                  <thead>
                    <tr className="text-xs font-semibold tracking-wide text-left text-gray-500 uppercase border-b dark:border-gray-700 bg-gray-50">
                      <th className="px-4 py-3">{t("carOverview.brand")}</th>
                      <th className="px-4 py-3">{t("carOverview.model")}</th>
                      <th className="px-4 py-3">{t("carOverview.type")}</th>
                      <th className="px-4 py-3">{t("carOverview.licensePlate")}</th>
                      {moreDetails && (<th className="px-4 py-3">{t("carOverview.seats")}</th>)}
                      {moreDetails && (<th className="px-4 py-3">{t("carOverview.childSeats")}</th>)}
                      {moreDetails && (<th className="px-4 py-3">{t("carOverview.foldableSeats")}</th>)}
                      {moreDetails && (<th className="px-4 py-3">{t("carOverview.towBar")}</th>)}
                      <th className="px-4 py-3">{t("carOverview.actions")}</th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y dark:divide-gray-700 dark:bg-gray-800">
                    {cars.map((car) => (
                      <tr key={car.id} className="text-gray-700 dark:text-gray-400">
                        <td className="px-4 py-3">{car.brand}</td>
                        <td className="px-4 py-3">{car.model}</td>
                        <td className="px-4 py-3">{car.type}</td>
                        <td className="px-4 py-3">{car.licensePlate}</td>
                        {moreDetails && (<td className="px-4 py-3">{car.numberOfSeats}</td>)}
                        {moreDetails && (<td className="px-4 py-3">{car.numberOfChildSeats}</td>)}
                        {moreDetails && (<td className="px-4 py-3">{car.foldableRearSeats ? t("carOverview.yes") : t("carOverview.no")}</td>)}
                        {moreDetails && (<td className="px-4 py-3">{car.towbar ? t("carOverview.yes") : t("carOverview.no")}</td>)}
                        <td className="px-4 py-3 flex gap-4">
                          {userRole !== "RENTER" && (<button
                            onClick={() => handleRental(car.id!, car.user?.email as string)}
                            className="text-white bg-blue-600 hover:bg-blue-700 font-medium rounded-lg text-sm px-4 py-2 text-center"
                          >
                            {t("carOverview.addRental")}
                          </button>)}
                          {userRole !== "RENTER" && (
                            <div>
                              {!rentedCarIds.includes(car.id!) ? (
                                <button
                                  onClick={() => showPopup(car.id!, car.user?.email as string)}
                                  className="text-white bg-red-600 hover:bg-red-700 font-medium rounded-lg text-sm px-4 py-2 text-center"
                                >
                                  {t("carOverview.delete")}
                                </button>
                              ) : (
                                <button
                                  disabled
                                  className="text-gray-500 bg-gray-400 font-medium rounded-lg text-sm px-4 py-2 text-center"
                                >
                                  {t("carOverview.delete")}
                                </button>
                              )}
                          </div>)}
                          <button onClick={handleMoreDetails} className="text-white bg-emerald-600 hover:bg-emerald-700 font-medium rounded-lg text-sm px-4 py-2 text-center flex">
                            {t("carOverview.details")}
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            ) : (
              <p className="text-center mt-4">{t("carOverview.noCarsFound")}</p>
            )}
          </div>
          <ConfirmationModal
            title={t("carOverview.confirmationTitle")}
            message={`${t("carOverview.carDetails")} ${carToDelete?.brand} ${carToDelete?.model} ${t("carOverview.withLicensePlate")}: ${carToDelete?.licensePlate}`}
            onConfirm={handleDelete}
            onCancel={closePopup}
          />
        </div>
        <div>
          <Helpbot></Helpbot>
        </div>
        <AlertDialog content={alertMessage} onClose={()=>setAlertMessage("")}></AlertDialog>
      </main>
    </>
  );
};

export default CarOverview;
