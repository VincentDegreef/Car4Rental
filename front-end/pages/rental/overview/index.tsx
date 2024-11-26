import React, { useState, useEffect } from "react";
import CarService from "@/services/CarService";
import Header  from "@/components/Header";
import RentalService from "@/services/RentalService";
import RentService from "@/services/RentService";
import { Rental, RentalSearch } from '@/types';
import { ConfirmationModal } from '@/components/ConfirmationModal';
import { useRouter } from 'next/router';
import Helpbot from '@/components/helpbot/Helpbot';
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
  const [rentals, setRentals] = useState<Rental[]>([]);
  const [rentalToDeleteId, setRentalToDeleteId] = useState<number>(0);
  const [rentalToDelete, setRentalToDelete] = useState<Rental>();
  const [userRole, setUserRole] = useState<string>("");
  const [showFilters, setShowFilters] = useState<boolean>(false);
  const [userEmail, setUserEmail] = useState<string>("");

  const [rentsIds, setRentsIds] = useState<number[]>([]);

  const router = useRouter();

  const [search, setSearch] = useState<RentalSearch>({
    email: "",
    brand: "",
    city: "",
    startDate: "",
    endDate: "",
    priceUnder: 0,
  });

  const [error, setError] = useState<string>("");

  const findRentalById = (id: number) => {
    setRentalToDelete(rentals.find((rental) => rental.id == id) as Rental);
  };

  const showPopup = (id: number) => {
    setRentalToDeleteId(id);
    findRentalById(id);
    document.getElementById("modal")?.classList.remove("hidden");
  };

  const closePopup = () => {
    document.getElementById("modal")?.classList.add("hidden");
  };
  const handleRent = (id: number, rentalEmail: string) => {
    if(rentalEmail === userEmail){
      setError("You can't rent your own car.");
      return;
    }
    sessionStorage.setItem("rentalId", String(id));
    router.push("/rent/add");
  };

  const cancelRental = () => {
    if (rentalToDeleteId != 0) {
      RentalService.CancelRental(rentalToDeleteId);
    } else {
      console.error("RentalId is not valid.");
    }
    setRentals(
      rentals.splice(
        rentals.findIndex((rental) => rental.id == rentalToDeleteId) - 1,
        1
      )
    );
    closePopup();
  };

  const handleShowFilters = () => {
    setShowFilters(!showFilters);
  };

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const target = e.target;
    const name = target.name;
    let value: string | number | Date;

    value = target.value;

    setSearch((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = async () => {
    let rentalsByEmail =
      search.email != ""
        ? await RentalService.getRentalsByEmail(search.email)
        : [];
    let rentalsByBrand =
      search.brand != ""
        ? await RentalService.getRentalsByBrand(search.brand)
        : [];
    let rentalsByCity =
      search.city != ""
        ? await RentalService.getRentalsByCity(search.city)
        : [];
    let rentalsByStartDate =
      search.startDate != ""
        ? await RentalService.getRentalsByStartDate(search.startDate)
        : [];
    let rentalsByEndDate =
      search.endDate != ""
        ? await RentalService.getRentalsByEndDate(search.endDate)
        : [];
    let rentalsByPriceUnder: Rental[] =
      search.priceUnder != 0
        ? await RentalService.GetRentalsUnderPrice(search.priceUnder)
        : [];
    let rentalsToDisplay: Rental[] = [];

    if (
      search.email == "" &&
      search.brand == "" &&
      search.city == "" &&
      search.startDate == "" &&
      search.endDate == "" &&
      search.priceUnder == 0
    ) {
      getRentals();
      setError("You need to choose one or more values to get search results.");
      return;
    }

    let fields = Object.values(search).filter((value) => value !== "").length;

    if (rentalsByEmail.length > 0 && rentalsByBrand.length > 0) {
      rentalsToDisplay = rentalsByEmail.filter((rental) =>
        rentalsByBrand.some((rental2) => rental.id === rental2.id)
      );
    } else if (rentalsByEmail.length > 0) {
      rentalsToDisplay = rentalsByEmail;
    } else if (rentalsByBrand.length > 0) {
      rentalsToDisplay = rentalsByBrand;
    }

    if (rentalsByCity.length > 0 && rentalsToDisplay.length > 0) {
      rentalsToDisplay = rentalsToDisplay.filter((rental) =>
        rentalsByCity.some((rental2) => rental.id === rental2.id)
      );
    } else if (rentalsByCity.length > 0) {
      rentalsToDisplay = rentalsByCity;
    }

    if (rentalsByStartDate.length > 0 && rentalsToDisplay.length > 0) {
      rentalsToDisplay = rentalsToDisplay.filter((rental) =>
        rentalsByStartDate.some((rental2) => rental.id === rental2.id)
      );
    } else if (rentalsByStartDate.length > 0) {
      rentalsToDisplay = rentalsByStartDate;
    }

    if (rentalsByEndDate.length > 0 && rentalsToDisplay.length > 0) {
      rentalsToDisplay = rentalsToDisplay.filter((rental) =>
        rentalsByEndDate.some((rental2) => rental.id === rental2.id)
      );
    } else if (rentalsByEndDate.length > 0) {
      rentalsToDisplay = rentalsByEndDate;
    }

    if (rentalsByPriceUnder.length > 0 && rentalsToDisplay.length > 0) {
      rentalsToDisplay = rentalsToDisplay.filter((rental) =>
        rentalsByPriceUnder.some((rental2) => rental.id === rental2.id)
      );
    } else if (rentalsByPriceUnder.length > 0) {
      rentalsToDisplay = rentalsByPriceUnder;
    }

    if (rentalsToDisplay.length == 0 && fields > 1) {
      setError("No rentals found with the given search criteria.");
      return;
    }

    if (search.email != "" && rentalsByEmail.length == 0) {
      setError("There are no rentals with this email address found as owner.");
      setRentals([]);
      return;
    } else if (search.brand != "" && rentalsByBrand.length == 0) {
      setError("There are no rentals with this brand found.");
      setRentals([]);
      return;
    } else if (search.city != "" && rentalsByCity.length == 0) {
      setError("There are no rentals in this city found.");
      setRentals([]);
      return;
    } else if (search.startDate != "" && rentalsByStartDate.length == 0) {
      setError("There are no rentals with this start date found.");
      setRentals([]);
      return;
    } else if (search.endDate != "" && rentalsByEndDate.length == 0) {
      setError("There are no rentals with this end date found.");
      setRentals([]);
      return;
    } else if (search.priceUnder != 0 && rentalsByPriceUnder.length == 0) {
      setError("There are no rentals with a price under this value found.");
      setRentals([]);
      return;
    }

    setError("");
    setRentals(rentalsToDisplay);
  };

  const handleCompare = (rentalId1:number) => {
    sessionStorage.setItem("rentalId1", String(rentalId1));
    router.push("/rental/compare");
  };

  const getRentals = async () => {
    RentalService.GetRentals()
      .then((fetchedRentals) => {
        if (Array.isArray(fetchedRentals)) {
          setRentals(fetchedRentals);
        } else {
          console.error("Received data is not an array:", fetchedRentals);
        }
      })
      .catch((error) => {
        console.error(error);
      });
    RentService.GetRents().then((fetchedRents) => {
      if (Array.isArray(fetchedRents)) {
        const filteredRents = fetchedRents.filter(
          (rent) => rent.endDate === null
        );

        setRentsIds(filteredRents.map((rent) => rent.rental.id!));
      } else {
        console.error("Received data is not an array:", fetchedRents);
      }
    });
  };

  useEffect(() => {
    if (sessionStorage.getItem("loggedInUserDetails") != null) {
      const userDetails = JSON.parse(
        sessionStorage.getItem("loggedInUserDetails") || ""
      );
      setUserEmail(userDetails.email);
      setUserRole(userDetails.role.name);
    }
    getRentals();
  }, []);

  return (
    <>
      <Header />
      <main>
      <div>
        <div>
          <h1 className="text-2xl font-bold text-center my-8">{t("rental.rentalOverview")}</h1>
          <div className="flex justify-center">
            <button
              onClick={handleShowFilters}
              className="m-2 bg-blue-500 hover:bg-blue-700 text-white font-bold h-fit px-4 py-2 rounded-lg block"
            >
              {showFilters ? "Hide filters" : "Show filters"}
            </button>
          </div>
          {showFilters && (<div className="flex flex-row justify-between max-w-screen-xl mx-auto gap-4 mb-8">
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
              <div className="flex items-start flex-col">
                <label htmlFor="brand" className="mr-2">{t("rental.brand")}</label>
                <input
                  name='brand'
                  type="text"
                  onChange={handleChange}
                  id="brand"
                  placeholder={t("rental.brandPlaceholder")}
                  className="px-4 py-2 border border-gray-300 rounded-lg"
                />
              </div>
              <div className="flex items-start flex-col">
                <label htmlFor="city" className="mr-2">{t("rental.city")}</label>
                <input
                  name='city'
                  type="text"
                  onChange={handleChange}
                  id="city"
                  placeholder={t("rental.cityPlaceholder")}
                  className="px-4 py-2 border border-gray-300 rounded-lg"
                />
              </div>
              <div className="flex items-start flex-col">
                <label htmlFor="startDate" className="mr-2">{t("rental.startDate")}</label>
                <input
                  name='startDate'
                  type="date"
                  onChange={handleChange}
                  id="startDate"
                  placeholder={t("rental.startDatePlaceholder")}
                  className="px-4 py-2 border border-gray-300 rounded-lg"
                />
              </div>
              <div className="flex items-start flex-col">
                <label htmlFor="endDate" className="mr-2">{t("rental.endDate")}</label>
                <input
                  name='endDate'
                  type="date"
                  onChange={handleChange}
                  id="endDate"
                  placeholder={t("rental.endDatePlaceholder")}
                  className="px-4 py-2 border border-gray-300 rounded-lg"
                />
              </div>
              <div className="flex items-start flex-col">
                <label htmlFor="priceUnder" className="mr-2">{t("rental.priceUnder")}</label>
                <input
                  name='priceUnder'
                  type="number"
                  onChange={handleChange}
                  id="priceUnder"
                  placeholder={t("rental.priceUnderPlaceholder")}
                  className="px-4 py-2 border border-gray-300 rounded-lg"
                />
              </div>

                  <button
            onClick={handleSubmit}
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold h-fit px-4 py-2 self-end rounded block"
          >
                    {t("rental.search")}
                  </button>
                </div>)}
                {error && (
          <p className="text-center text-red-500 border-2 border-red-500 rounded-lg p-4 mb-6 w-fit mx-auto">
            {error}
          </p>
        )}
                {rentals.length > 0 ? (
                  <div className="overflow-x-auto max-w-screen-xl mx-auto">
                    <table className="table-auto w-full text-left whitespace-no-wrap">
                      <thead>
                        <tr className="text-xs font-semibold tracking-wide text-left text-gray-500 uppercase border-b dark:border-gray-700 bg-gray-50">
                          <th className="px-4 py-3">{t("rental.price")}</th>
                          <th className="px-4 py-3">{t("rental.car")}</th>
                          <th className="px-4 py-3">{t("rental.startDate2")}</th>
                          <th className="px-4 py-3">{t("rental.endDate2")}</th>
                          <th className="px-4 py-3">{t("rental.city2")}</th>
                          <th className="px-4 py-3">{t("rental.email2")}</th>
                          <th className="px-4 py-3">{t("rental.actions")}</th>
                        </tr>
                      </thead>
                      <tbody className="bg-white divide-y dark:divide-gray-700 dark:bg-gray-800">
                        {rentals.map((rental) => (
                          <tr
                    key={rental.id}
                    className="text-gray-700 dark:text-gray-400"
                  >
                            <td className="px-4 py-3">€{rental.price} /{t("rental.day")}</td>
                            <td className="px-4 py-3">{`${rental.car.brand} ${rental.car.model} ${rental.car.licensePlate}`}</td>
                            <td className="px-4 py-3">{rental.startDate}</td>
                            <td className="px-4 py-3">{rental.endDate}</td>
                            <td className="px-4 py-3">{rental.city}</td>
                            <td className="px-4 py-3">{rental.email}</td>
                            <td className="px-4 py-3 flex flex-row">

                            
                              {!rentsIds.includes(rental.id ? rental.id : 0) ? (
                                <button onClick={() => handleRent(rental.id!, rental.email)} className="mr-2 text-white bg-green-600 hover:bg-green-700 font-medium rounded-lg text-sm px-4 py-2 text-center">
                                  {t("rental.rent")}
                                </button>
                              ) : (<button disabled className="mr-2 text-white bg-gray-400 font-medium rounded-lg text-sm px-4 py-2 text-center">
                                  {t("rental.rent")}
                                </button>
                              )}
                            

                              {userRole !== "RENTER" && (
                                <div>
                                  {!rentsIds.includes(rental.id ? rental.id : 0) ? (
                                    <button onClick={() => showPopup(rental?.id as number)} className=" mr-2 text-white bg-red-600 hover:bg-red-700 font-medium rounded-lg text-sm px-4 py-2 text-center">
                                      {t("rental.cancel")}
                                    </button>
                                  ) : (<button disabled className="mr-2 text-white bg-gray-400 font-medium rounded-lg text-sm px-4 py-2 text-center">
                                      {t("rental.cancel")}
                                    </button>
                                  )}
                                </div>)}
                                <div>
                                  <button onClick={()=> handleCompare(rental?.id as number) } className="text-white bg-blue-600 hover:bg-blue-700 font-medium rounded-lg text-sm px-4 py-2 text-center">
                                  {t("rental.compare")}
                                  </button>
                                </div>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                ) : (
                  <p className="text-center mt-4">{t("rental.noRentals")}</p>
                )}
              </div>
              <ConfirmationModal
                title={t("rental.areYouSure")}
                message={`${t("rental.rentalFor")} ${rentalToDelete?.car.brand} ${rentalToDelete?.car.model}, ${rentalToDelete?.car.licensePlate} ${t("rental.from")} ${rentalToDelete?.startDate} ${t("rental.to")} ${rentalToDelete?.endDate}`}
                onConfirm={() => cancelRental()}
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
