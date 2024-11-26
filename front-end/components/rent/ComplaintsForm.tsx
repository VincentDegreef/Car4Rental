import React, { useEffect, useState } from "react";
import { Car, Rent, Rental, User } from "@/types";
import { useRouter } from "next/router";
import RentService from "@/services/RentService";
import { useTranslation } from "next-i18next";


const ComplaintsForm: React.FC = () => {
  const { t } = useTranslation("common");
  const [complaintMessage, setComplaintMessage] = useState("");
  const [errors, setErrors] = useState<{ [key: string]: string }>({});
  const [car, setCar] = useState<{ brand: string; licensePlate: string }>({ brand: "", licensePlate: "" });

  const router = useRouter();
  const { rentId } = router.query;

  const handleBackToRentOverview = () => {
    router.push("/rent/overview");
  };

  const validateForm = () => {
    let formIsValid = true;
    let errors: { [key: string]: string } = {};

    if (!complaintMessage) {
      formIsValid = false;
      errors["complaintMessage"] = "Complaint message is required";
    }

    setErrors(errors);
    return formIsValid;
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setComplaintMessage(e.target.value);
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!validateForm()) {
      return;
    }

    const response = await RentService.AddComplaint(Number(rentId), complaintMessage);
    

    if (response) {
        handleBackToRentOverview();
    }
  };

  useEffect(() => {
    const fetchRentDetails = async () => {
      try {
        const rent = await RentService.GetRentById(Number(rentId));
        setCar({
          brand: rent.rental.car.brand,
          licensePlate: rent.rental.car.licensePlate,
        });
      } catch (error) {
        console.error("Failed to fetch rent details:", error);
      }
    };

    if (rentId) {
      fetchRentDetails();
    }
  }, [rentId]);


    return (
        <form onSubmit={handleSubmit} className="max-w-lg mx-auto p-4 shadow-md rounded-lg">
            <div>
            <table className="w-full border-collapse border border-gray-200">
            <tbody>
                <tr className="bg-gray-100">
                <th className="border border-gray-200 px-4 py-2">{t("complaint.car")}</th>
                <th className="border border-gray-200 px-4 py-2">{t("complaint.licensePlate")}</th>
                </tr>
                <tr className="border border-gray-200">
                <td className="border border-gray-200 px-4 py-2 text-center">
                    {car.brand}
                </td>
                <td className="border border-gray-200 px-4 py-2 text-center">
                    {car.licensePlate}
                </td>
                </tr>
            </tbody>
            </table>
            </div>
            <div className="mb-4">
            <label htmlFor="ComplaintMessage">{t("complaint.complaintMessage")}</label>
            <input
            type="text"
            id="complaintMessage"
            name="complaintMessage"
            onChange={handleChange}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            />
            {errors.complaintMessage && (
            <p className="text-red-500 text-xs italic">{errors.complaintMessage}</p>
            )}
        </div>

        <div className="flex justify-center space-x-4">
            <button
            type="submit"
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
            >
            {t("complaint.fileComplaint")}
            </button>
            <button
            type="button"
            onClick={handleBackToRentOverview}
            className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
            >
            {t("complaint.back")}
            </button>
        </div>
        </form>
    );
};

export default ComplaintsForm;