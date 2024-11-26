import React, { useEffect, useState } from "react";
import { Car, Rent, Rental, User } from "@/types";
import { useRouter } from "next/router";
import RentService from "@/services/RentService";
import { ConfirmationModalOk } from "@/components/ConfirmationModal";
import { useTranslation } from "next-i18next";

const CheckOutForm: React.FC = () => {
  const { t } = useTranslation("common");
  const [seller, setSeller] = useState<User>({
    id: 0,
    username: "",
    password: "",
    email: "",
    phoneNumber: "",
  });

  const [car, setCar] = useState<Car>({
    brand: "",
    model: "",
    type: "",
    numberOfSeats: 0,
    numberOfChildSeats: 0,
    foldableRearSeats: false,
    towbar: false,
    licensePlate: "",
    user: seller,
  });

  const [rental, setRental] = useState<Rental>({
    startDate: "",
    startTime: "",
    endDate: "",
    endTime: "",
    price: 0,
    street: "",
    number: 0,
    postal: 0,
    city: "",
    email: "",
    phoneNumber: "",
    car: car,
  });

  const [rent, setRent] = useState<Rent>({
    phoneNumber: "",
    email: "",
    identification: "",
    birthDate: "",
    drivingLicenceNumber: "",
    rental: rental,
    startDate: "",
    startMillage: 0,
    startFuelQuantity: 0,
    endDate: "",
    returnMillage: 0,
    returnFuelQuantity: 0,
    totalPrice: 0,
  });

  const [totalPrice, setTotalPrice] = useState(0);
  const [errors, setErrors] = useState<{ [key: string]: string }>({});

  const router = useRouter();
  const { rentId } = router.query;

  const handleBackToRentOverview = () => {
    document.getElementById("modalOk")?.classList.add("hidden");
    router.push("/rent/overview");
  };

  const showPopup = (totalPrice: number) => {
    document.getElementById("modalOk")?.classList.remove("hidden");
  };

  const validateForm = () => {
    let formIsValid = true;
    let errors: { [key: string]: string } = {};

    if (!rent.endDate) {
      formIsValid = false;
      errors["endDate"] = "End date is required";
    }

    if (isNaN(rent.returnMillage) || rent.returnMillage < 0) {
      formIsValid = false;
      errors["returnMillage"] = "Return millage must be a positive number";
    }

    if (isNaN(rent.returnFuelQuantity) || rent.returnFuelQuantity < 0) {
      formIsValid = false;
      errors["returnFuelQuantity"] = "Return fuel quantity must be a positive number";
    }

    if (new Date(rent.startDate) > new Date(rent.endDate)) {
      formIsValid = false;
      errors["endDate"] = "End date must be later than start date";
    }

    if (new Date(rent.endDate) > new Date(rental.endDate)) {
      formIsValid = false;
      errors["endDate"] = "Rent end date cannot be after rental end date";
    }

    if (rent.startMillage > rent.returnMillage) {
      formIsValid = false;
      errors["returnMillage"] = "Return millage must be higher than the start millage.";
    }

    setErrors(errors);
    return formIsValid;
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value, type } = e.target;
    const parsedValue = type === "number" ? parseFloat(value) : value;

    setRent((prevState) => ({
      ...prevState,
      [name]: parsedValue,
    }));
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!validateForm()) {
      return;
    }

    const response = await RentService.CheckOut(Number(rentId), rent);
    const data = await response.json();

    if (response && response.status === 200) {
      showPopup(data);
      setTotalPrice(data);
    }
  };

  useEffect(() => {
    const fetchRent = async () => {
      try {
        const rent = await RentService.GetRentById(Number(rentId));
        setCar(rent.rental.car);
        setRent(rent);
        setRental(rent.rental);
      } catch (error) {
        console.error("Failed to fetch rent data:", error);
      }
    };

    if (rentId) {
      fetchRent();
    }
  }, [rentId]);

  return (
    <form onSubmit={handleSubmit} className="max-w-lg mx-auto p-4 shadow-md rounded-lg">
      <div>
        <table className="w-full border-collapse border border-gray-200">
          <tbody>
            <tr className="bg-gray-100">
              <th className="border border-gray-200 px-4 py-2">{t("checkOut.car")}</th>
              <th className="border border-gray-200 px-4 py-2">{t("checkOut.licensePlate")}</th>
            </tr>
            <tr className="border border-gray-200">
              <td className="border border-gray-200 px-4 py-2 text-center">
                {car.brand} {car.model}
              </td>
              <td className="border border-gray-200 px-4 py-2 text-center">
                {car.licensePlate}
              </td>
            </tr>
          </tbody>
        </table>

        <table className="w-full border-collapse border border-gray-200 my-3">
          <tbody>
            <tr className="bg-gray-100">
              <th className="border border-gray-200 px-4 py-2">{t("checkOut.startDate")}</th>
              <th className="border border-gray-200 px-4 py-2">{t("checkOut.startMillage")}</th>
            </tr>
            <tr className="border border-gray-200">
              <td className="border border-gray-200 px-4 py-2 text-center">
                {rent.startDate}
              </td>
              <td className="border border-gray-200 px-4 py-2 text-center">
                {rent.startMillage}
              </td>
            </tr>
          </tbody>
        </table>

        <table className="w-full border-collapse border border-gray-200 my-3">
          <tbody>
            <tr className="bg-gray-100">
              <th className="border border-gray-200 px-4 py-2">{t("checkOut.startFuelQuantity")}</th>
            </tr>
            <tr className="border border-gray-200">
              <td className="border border-gray-200 px-4 py-2 text-center">
                {rent.startFuelQuantity}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div className="mb-4">
        <label htmlFor="endDate">{t("checkOut.endDate")}</label>
        <input
          type="date"
          id="endDate"
          name="endDate"
          value={rent.endDate}
          onChange={handleChange}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
        />
        {errors.endDate && (
          <p className="text-red-500 text-xs italic">{errors.endDate}</p>
        )}
      </div>
      <div className="mb-4">
        <label htmlFor="returnMillage">{t("checkOut.returnMillage")}</label>
        <input
          type="number"
          id="returnMillage"
          name="returnMillage"
          value={rent.returnMillage}
          onChange={handleChange}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
        />
        {errors.returnMillage && (
          <p className="text-red-500 text-xs italic">{errors.returnMillage}</p>
        )}
      </div>
      <div className="mb-4">
        <label htmlFor="returnFuelQuantity">{t("checkOut.returnFuelQuantity")}</label>
        <input
          type="number"
          id="returnFuelQuantity"
          name="returnFuelQuantity"
          value={rent.returnFuelQuantity}
          onChange={handleChange}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
        />
        {errors.returnFuelQuantity && (
          <p className="text-red-500 text-xs italic">{errors.returnFuelQuantity}</p>
        )}
      </div>

      <div className="flex justify-center space-x-4">
        <button
          type="submit"
          className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
        >
          {t("checkOut.checkOut")}
        </button>
        <button
          type="button"
          onClick={handleBackToRentOverview}
          className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
        >
          {t("checkOut.back")}
        </button>
        <ConfirmationModalOk
          title="Total cost"
          message={`Your total cost is: €${totalPrice}. Detailed overview will be sent via e-mail.`}
          onOk={handleBackToRentOverview}
        />
      </div>
    </form>
  );
};

export default CheckOutForm;