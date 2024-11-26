import React, { useEffect, useState } from "react";
import { useRouter } from "next/router";
import CarService from "@/services/CarService";
import { Car, Rental } from "@/types";
import { useTranslation } from "next-i18next";

const RentalForm: React.FC = () => {
  const { t } = useTranslation('common');

  const [car, setCar] = useState<Car>({
    brand: "",
    model: "",
    type: "",
    numberOfSeats: 0,
    numberOfChildSeats: 0,
    foldableRearSeats: false,
    towbar: false,
    licensePlate: "",
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

  const [carId, setCarId] = useState(0);

  const [errors, setErrors] = useState<{ [key: string]: string }>({});

  const router = useRouter();

  const handleBackToHome = () => {
    router.push("/rental/overview");
  };

  const validateForm = () => {
    let formIsValid = true;
    let errors: { [key: string]: string } = {};

    if (rental.startDate == "") {
      formIsValid = false;
      errors["startDate"] = t("rentalForm.errors.startDateRequired");
    } else if (rental.startDate < new Date().toISOString().split("T")[0]) {
      formIsValid = false;
      errors["startDate"] = t("rentalForm.errors.startDateFuture");
    } else if (rental.startDate >= rental.endDate) {
      formIsValid = false;
      errors["startDate"] = t("rentalForm.errors.startDateBeforeEndDate");
    }

    if (rental.endDate == "") {
      formIsValid = false;
      errors["endDate"] = t("rentalForm.errors.endDateRequired");
    } else if (rental.endDate < new Date().toISOString().split("T")[0]) {
      formIsValid = false;
      errors["endDate"] = t("rentalForm.errors.endDateFuture");
    } else if (rental.endDate <= rental.startDate) {
      formIsValid = false;
      errors["endDate"] = t("rentalForm.errors.endDateAfterStartDate");
    }

    if (rental.city === "") {
      formIsValid = false;
      errors["city"] = t("rentalForm.errors.cityRequired");
    }

    const emailRegex = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i;
    if (!emailRegex.test(rental.email)) {
      formIsValid = false;
      errors["email"] = t("rentalForm.errors.invalidEmail");
    }

    if (rental.phoneNumber.length < 1) {
      formIsValid = false;
      errors["phoneNumber"] = t("rentalForm.errors.invalidPhoneNumber");
    }

    if (rental.price < 0) {
      formIsValid = false;
      errors["price"] = t("rentalForm.errors.priceNegative");
    } else if (rental.price === 0) {
      formIsValid = false;
      errors["price"] = t("rentalForm.errors.priceZero");
    } else if (rental.price > 1000) {
      formIsValid = false;
      errors["price"] = t("rentalForm.errors.priceTooHigh");
    }

    setErrors(errors);
    return formIsValid;
  };

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const target = e.target;
    const name = target.name;
    let value: string | number | Date;

    value = target.value;

    setRental((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!validateForm()) {
      return;
    }

    let added = await CarService.MakeRental(carId, rental);
    if (added) {
      handleBackToHome();
    }
  };

  const getCarIdFromSessionStorage = async () => {
    const storedCarId = await sessionStorage.getItem("carId");
    setCarId(storedCarId ? parseInt(storedCarId) : 0);
  };

  useEffect(() => {
    getCarIdFromSessionStorage();
    CarService.GetCarById(carId)
      .then((fetchedCar) => {
        setCar(fetchedCar);
      })
      .catch((error) => {
        console.error(error);
      });
  }, [carId]);

  return (
    <form
      onSubmit={handleSubmit}
      className="max-w-lg mx-auto p-4 shadow-md rounded-lg"
    >
      <div>
        <table className="w-full border-collapse border border-gray-200">
          <tbody>
            <tr className="bg-gray-100">
              <th className="border border-gray-200 px-4 py-2">{t("rentalForm.car")}</th>
              <th className="border border-gray-200 px-4 py-2">
                {t("rentalForm.licensePlate")}
              </th>
            </tr>
            <tr className="border border-gray-200">
              <td className="border border-gray-200 px-4 py-2 text-center">
                {car?.brand} {car?.model}
              </td>
              <td className="border border-gray-200 px-4 py-2 text-center">
                {car?.licensePlate}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div className="mb-4">
        <label htmlFor="startDate">{t("rentalForm.startDate")}*</label>
        <input
          type="date"
          id="startDate"
          name="startDate"
          onChange={handleChange}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
        />
        {errors.startDate && (
          <p className="text-red-500 text-xs italic">{errors.startDate}</p>
        )}
      </div>
      <div className="mb-4">
        <label htmlFor="startTime">{t("rentalForm.startTime")}</label>
        <input
          type="time"
          id="startTime"
          name="startTime"
          onChange={handleChange}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
        />
      </div>
      <div className="mb-4">
        <label htmlFor="endDate">{t("rentalForm.endDate")}*</label>
        <input
          type="date"
          id="endDate"
          name="endDate"
          onChange={handleChange}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
        />
        {errors.endDate && (
          <p className="text-red-500 text-xs italic">{errors.endDate}</p>
        )}
      </div>
      <div className="mb-4">
        <label htmlFor="endTime">{t("rentalForm.endTime")}</label>
        <input
          type="time"
          id="endTime"
          name="endTime"
          onChange={handleChange}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
        />
      </div>
      <div className="mb-4">
        <label htmlFor="price">{t("rentalForm.pricePerDay")}*</label>
        <input
          type="number"
          id="price"
          name="price"
          onChange={handleChange}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
        />
        {errors.price && (
          <p className="text-red-500 text-xs italic">{errors.price}</p>
        )}
      </div>
      <div className="mb-4">
        <label htmlFor="street">{t("rentalForm.street")}</label>
        <input
          type="text"
          id="street"
          name="street"
          onChange={handleChange}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
        />
        {errors.street && (
          <p className="text-red-500 text-xs italic">{errors.street}</p>
        )}
      </div>
      <div className="mb-4">
        <label htmlFor="number">{t("rentalForm.number")}</label>
        <input
          type="number"
          id="number"
          name="number"
          onChange={handleChange}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
        />
        {errors.number && (
          <p className="text-red-500 text-xs italic">{errors.number}</p>
        )}
      </div>
      <div className="mb-4">
        <label htmlFor="postal">{t("rentalForm.postalCode")}</label>
        <input
          type="number"
          id="postal"
          name="postal"
          onChange={handleChange}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
        />
        {errors.postal && (
          <p className="text-red-500 text-xs italic">{errors.postal}</p>
        )}
        </div>
        <div className="mb-4">
          <label htmlFor="city">{t("rentalForm.city")}*</label>
          <input
            type="text"
            id="city"
            name="city"
            onChange={handleChange}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
          />
          {errors.city && (
            <p className="text-red-500 text-xs italic">{errors.city}</p>
          )}
        </div>
        <div className="mb-4">
          <label htmlFor="email">{t("rentalForm.email")}*</label>
          <input
            type="email"
            id="email"
            name="email"
            onChange={handleChange}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
          />
          {errors.email && (
            <p className="text-red-500 text-xs italic">{errors.email}</p>
          )}
        </div>
        <div className="mb-4">
          <label htmlFor="phoneNumber">{t("rentalForm.phoneNumber")}*</label>
          <input
            type="tel"
            id="phoneNumber"
            name="phoneNumber"
            onChange={handleChange}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
          />
          {errors.phoneNumber && (
            <p className="text-red-500 text-xs italic">{errors.phoneNumber}</p>
          )}
        </div>
        <div className="flex justify-center space-x-4">
          <button
            type="submit"
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
          >
            {t("rentalForm.addRental")}
          </button>
          <button
            type="button"
            onClick={handleBackToHome}
            className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
          >
            {t("rentalForm.cancel")}
          </button>
        </div>
      </form>
    );
  };
  
  export default RentalForm;
  
