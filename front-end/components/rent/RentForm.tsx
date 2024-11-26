import React, { useEffect, useState } from "react";
import { useRouter } from "next/router";
import RentalService from "@/services/RentalService";
import RentService from "@/services/RentService";
import NotificationService from "@/services/NotificationService";
import { Car, Rental, RentRequest, User } from "@/types";
import { useTranslation } from "next-i18next";

const RentForm: React.FC = () => {
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

  const [rent, setRent] = useState<RentRequest>({
    phoneNumber: "",
    email: "",
    identification: "",
    birthDate: "",
    drivingLicenceNumber: "",
    startDate: "",
    startMillage: 0,
    startFuelQuantity: 0,
  });

  const [rentalId, setRentalId] = useState<number | null>(null);
  const [errors, setErrors] = useState<{ [key: string]: string }>({});

  const router = useRouter();

  const handleBackToHome = () => {
    router.push("/rent/overview");
  };

  const validateForm = () => {
    let formIsValid = true;
    const errors: { [key: string]: string } = {};

    if (!rent.phoneNumber) {
      formIsValid = false;
      errors["phoneNumber"] = "Phone number is required";
    }

    const emailRegex = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i;
    if (!emailRegex.test(rent.email)) {
      formIsValid = false;
      errors["email"] = "Invalid email address";
    }

    const identificationRegex = /^\d{2}\.\d{2}\.\d{2}-\d{3}\.\d{2}$/;
    if (!identificationRegex.test(rent.identification)) {
      formIsValid = false;
      errors["identification"] = "Identification number is invalid";
    }

    if (!rent.birthDate) {
      formIsValid = false;
      errors["birthDate"] = "Birth date is required";
    } else if (new Date(rent.birthDate) > new Date()) {
      formIsValid = false;
      errors["birthDate"] = "Birth date must be in the past";
    }

    if (!rent.drivingLicenceNumber) {
      formIsValid = false;
      errors["drivingLicenceNumber"] = "Driving licence number is required";
    } else {
      const drivingLicenceRegex = /^[0-9]{10}$/;
      if (!drivingLicenceRegex.test(rent.drivingLicenceNumber)) {
        formIsValid = false;
        errors["drivingLicenceNumber"] =
          "Invalid driving licence number format";
      }
    }

    setErrors(errors);
    return formIsValid;
  };

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value, type } = e.target;
    const parsedValue = type === "number" ? parseFloat(value) : value;

    setRent((prevState) => ({
      ...prevState,
      [name]: parsedValue,
    }));
  };

  const makeNotification = async (rentId: number, renterEmail: string) => {
    await NotificationService.MakeNotification(rentId, renterEmail);
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!validateForm()) {
      return;
    }
    const rentalId = sessionStorage.getItem("rentalId");
    if (!rentalId) {
      console.error("RentalId is not set");
      return;
    }

    try {
      const rental = await RentalService.GetRentalById(parseInt(rentalId));
      const ownerCar = rental.car.user;

      if (!ownerCar) {
        console.error("Owner car is undefined");
        return;
      }

      const userData = JSON.parse(
        sessionStorage.getItem("loggedInUserDetails") ?? "{}"
      );
      if (!userData.id || !userData.email) {
        console.error("No logged in user details");
        return;
      }

      const buyerId = userData.id;
      const renterEmail = userData.email;

      const response = await RentService.MakeRent(parseInt(rentalId), rent, renterEmail);
      const newRent = await response.json();
      makeNotification(newRent.id, renterEmail);

      handleBackToHome();
    } catch (error) {
      console.error("Failed to make rent:", error);
    }
  };

  useEffect(() => {
    const storedRentalId = sessionStorage.getItem("rentalId");
    if (storedRentalId) {
      setRentalId(parseInt(storedRentalId));
    }
  }, []);

  useEffect(() => {
    if (rentalId) {
      RentalService.GetRentalById(rentalId)
        .then((fetchedRental) => {
          setRental(fetchedRental);
          setCar(fetchedRental.car);
        })
        .catch((error) => {
          console.error(error);
        });
    }
  }, [rentalId]);

  return (
    <form
      onSubmit={handleSubmit}
      className="max-w-lg mx-auto p-4 shadow-md rounded-lg"
    >
      <div>
        <table className="w-full border-collapse border border-gray-200">
          <tbody>
            <tr className="bg-gray-100">
              <th className="border border-gray-200 px-4 py-2">{t("rentAdd.car")}</th>
              <th className="border border-gray-200 px-4 py-2">
              {t("rentAdd.licensePlate")}
              </th>
            </tr>
            <tr className="border border-gray-200">
              <td className="border border-gray-200 px-4 py-2 text-center">
                {rental.car.brand} {rental.car.model}
              </td>
              <td className="border border-gray-200 px-4 py-2 text-center">
                {rental.car.licensePlate}
              </td>
            </tr>
          </tbody>
        </table>

        <table className="w-full border-collapse border border-gray-200 my-3">
          <tbody>
            <tr className="bg-gray-100">
              <th className="border border-gray-200 px-4 py-2">{t("rentAdd.start-Date")}</th>
              <th className="border border-gray-200 px-4 py-2">{t("rentAdd.endDate")}</th>
            </tr>
            <tr className="border border-gray-200">
              <td className="border border-gray-200 px-4 py-2 text-center">
                {rental.startDate}
              </td>
              <td className="border border-gray-200 px-4 py-2 text-center">
                {rental.endDate}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div className="mb-4">
        <label htmlFor="phoneNumber">{t("rentAdd.phoneNumber")}</label>
        <input
          type="text"
          id="phoneNumber"
          name="phoneNumber"
          value={rent.phoneNumber}
          onChange={handleChange}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
        />
        {errors.phoneNumber && (
          <p className="text-red-500 text-xs italic">{errors.phoneNumber}</p>
        )}
      </div>
      <div className="mb-4">
        <label htmlFor="email">{t("rentAdd.email")}</label>
        <input
          type="text"
          id="email"
          name="email"
          value={rent.email}
          onChange={handleChange}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
        />
        {errors.email && (
          <p className="text-red-500 text-xs italic">{errors.email}</p>
        )}
      </div>
      <div className="mb-4">
        <label htmlFor="identification">{t("rentAdd.nationalRegister")}</label>
        <input
          type="text"
          id="identification"
          name="identification"
          value={rent.identification}
          onChange={handleChange}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
        />
        {errors.identification && (
          <p className="text-red-500 text-xs italic">{errors.identification}</p>
        )}
      </div>
      <div className="mb-4">
        <label htmlFor="birthDate">{t("rentAdd.dateOfBirth")}</label>
        <input
          type="date"
          id="birthDate"
          name="birthDate"
          value={rent.birthDate}
          onChange={handleChange}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
        />
        {errors.birthDate && (
          <p className="text-red-500 text-xs italic">{errors.birthDate}</p>
        )}
      </div>
      <div className="mb-4">
        <label htmlFor="drivingLicenceNumber">{t("rentAdd.driverLicenseNumber")}</label>
        <input
          type="text"
          id="drivingLicenceNumber"
          name="drivingLicenceNumber"
          value={rent.drivingLicenceNumber}
          onChange={handleChange}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
        />
        {errors.drivingLicenceNumber && (
          <p className="text-red-500 text-xs italic">
            {errors.drivingLicenceNumber}
          </p>
        )}
      </div>
      <div className="mb-4">
        <label htmlFor="startDate">{t("rentAdd.startDate")}</label>
        <input
          type="date"
          id="startDate"
          name="startDate"
          value={rent.startDate}
          onChange={handleChange}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
        />
      </div>
      <div className="mb-4">
        <label htmlFor="startMillage">{t("rentAdd.startMillage")}</label>
        <input
          type="number"
          id="startMillage"
          name="startMillage"
          value={rent.startMillage}
          onChange={handleChange}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
        />
      </div>
      <div className="mb-4">
        <label htmlFor="startFuelQuantity">{t("rentAdd.startFuelQuantity")}</label>
        <input
          type="number"
          id="startFuelQuantity"
          name="startFuelQuantity"
          value={rent.startFuelQuantity}
          onChange={handleChange}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
        />
      </div>

      <div className="flex justify-center space-x-4">
        <button
          type="submit"
          className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
        >
         {t("rentAdd.rent")}
        </button>
        <button
          type="button"
          onClick={handleBackToHome}
          className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
        >
          {t("rentAdd.cancel")}
        </button>
      </div>
    </form>
  );
};

export default RentForm;