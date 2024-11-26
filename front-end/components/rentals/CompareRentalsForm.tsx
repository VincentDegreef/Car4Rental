import RentalService from "@/services/RentalService";
import { Car, Rental, StatusMessage, User } from "@/types";
import { useEffect, useState } from "react";
import useSWR, { mutate } from "swr";
import useInterval from "use-interval";
import { FaArrowsAltH } from "react-icons/fa";
import { useTranslation } from "next-i18next";

const CompareRentalsForm: React.FC = () => {
    const { t } = useTranslation();
    const [fuel, setFuel] = useState("");
    const [distance, setDistance] = useState("");
    const [endDate, setEndDate] = useState("");
    const [totalPrice1, setTotalPrice1] = useState(0);
    const [totalPrice2, setTotalPrice2] = useState(0);
    const [duration1, setDuration1] = useState(0);
    const [statusMessages, setStatusMessages] = useState<StatusMessage[]>([]);
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

    const [rental1, setRental1] = useState<Rental>({
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

    const [rental2, setRental2] = useState<Rental>({
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

    const validateForm = () => {
        let formIsValid = true;
        let statusMessages: StatusMessage[] = [];
        if (fuel === "") {
            formIsValid = false;
            statusMessages.push({ message: "Fuel is required", type: "error" });
        }

        if (distance === "") {
            formIsValid = false;
            statusMessages.push({ message: "Distance is required", type: "error" });
        }

        if (endDate === "") {
            formIsValid = false;
            statusMessages.push({ message: "End date is required", type: "error" });
        }

        setStatusMessages(statusMessages);
        return formIsValid;
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setStatusMessages([]);
        if (!validateForm()) {
            return;
        }
        const id1 = rental1.id || 0; // Assign a default value of 0 if rental1.id is undefined
        const id2 = rental2.id || 0; // Assign a default value of 0 if rental2.id is undefined

        // number of days between start and given end date
        const startDate = new Date(rental1.startDate);
        const end = new Date(endDate);
        const timeDiff = Math.abs(end.getTime() - startDate.getTime());
        const diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));

        setDuration1(diffDays);

        const response = await RentalService.compareRentals(id1, id2, Number(distance), Number(fuel), endDate);
        if (response) {
            setTotalPrice1(response[0]);
            setTotalPrice2(response[1]);
            if (response[0] < response[1]) {
                statusMessages.push({ message: "Comparison successful, Rental 1 is cheaper", type: "success" });
            } else if (response[0] > response[1]) {
                statusMessages.push({ message: "Comparison successful, Rental 2 is cheaper", type: "success" });
            }
        } else {
            alert("Comparison failed");
        }
    };

    const handleCompareThis = async (id: number) => {
        const rental = await RentalService.GetRentalById(id);
        setRental2(rental);
    };

    const handleReset = () => {
        setRental2({
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
        setTotalPrice1(0);
        setTotalPrice2(0);
        setDuration1(0);
        setFuel("");
        setDistance("");
        setEndDate("");
        setStatusMessages([]);
    };

    const handleSwitchRentals = () => {
        if (rental2.id) {
            // Swap rentals
            const temp = rental1;
            setRental1(rental2);
            setRental2(temp);
    
            // Swap prices
            const tempPrice = totalPrice1;
            setTotalPrice1(totalPrice2);
            setTotalPrice2(tempPrice);
    
            // Set status message
            setStatusMessages([{ message: "Rentals switched successfully", type: "success" }]);
        } else {
            setStatusMessages([{ message: "Rental 2 must be set before switching", type: "error" }]);
        }
    };

    const fetchRental = async () => {
        const rentalId1 = sessionStorage.getItem("rentalId1");
        const rental = await RentalService.GetRentalById(Number(rentalId1));

        if (rental) {
            setRental1(rental);
        }
    };

    const fetchAllRentals = async () => {
        const rentals = await RentalService.GetRentals();
        return rentals;
    };

    useEffect(() => {
        fetchRental();
    }, []);

    const { data, isLoading, error } = useSWR('rentals', fetchAllRentals);

    useInterval(() => {
        mutate('rentals', fetchAllRentals());
    }, 20000);

    // if totalPrices are less than 0, bg color is red, if totalPrices are greater than 0, bg color is green, if they are equal, bg color is gray
    const rental1BgClass = totalPrice1 < totalPrice2 ? "bg-green-700" : totalPrice1 > 0 ? "bg-red-700" : "bg-gray-800";
    const rental2BgClass = totalPrice2 < totalPrice1 ? "bg-green-700" : totalPrice2 > 0 ? "bg-red-700" : "bg-gray-800";


    return (
        <div className="flex flex-col justify-evenly mt-15 gap-20">
            
            <div className="flex flex-row justify-center items-center gap-10">
                <div className="flex flex-row gap-2 items-center">
                    <div className={`max-w-sm shadow-md rounded-lg overflow-hidden ${rental1BgClass}`}>
                        <div className="p-4">
                            <h2 className="text-2xl font-semibold text-center text-white">{t("compare.rental1")}</h2>
                            <div className="mt-4">
                                <p className="text-white"><span className="font-medium">{t("compare.car")}</span> {rental1.car.brand}</p>
                                <p className="text-white"><span className="font-medium">{t("compare.price")}</span> {rental1.price}</p>
                                <p className="text-white"><span className="font-medium">{t("compare.startDate")}</span> {rental1.startDate}</p>
                                <p className="text-white"><span className="font-medium">{t("compare.endDate")}</span> {rental1.endDate}</p>
                                <p className="text-white"><span className="font-medium">{t("compare.city")}</span> {rental1.city}</p>
                            </div>
                            <div className="mt-5 text-center">
                                <p className="text-white"><span className="font-medium">{t("compare.duration")}</span> {duration1} {t("compare.days")}</p>
                                <p className="text-white"><span className="font-medium">{t("compare.totalPrice")}</span> €{totalPrice1}</p>
                            </div>
                        </div>
                    </div>
                    <div>
                        <FaArrowsAltH size={40} />
                    </div>
                    <div className={`max-w-sm shadow-md rounded-lg overflow-hidden ${rental2BgClass}`}>
                        <div className="p-4">
                            <h2 className="text-2xl font-semibold text-center text-white">{t("compare.rental2")}</h2>
                            <div className="mt-4">
                                <p className="text-white"><span className="font-medium">{t("compare.car")}</span> {rental2.car.brand}</p>
                                <p className="text-white"><span className="font-medium">{t("compare.price")}</span> {rental2.price}</p>
                                <p className="text-white"><span className="font-medium">{t("compare.startDate")}</span> {rental2.startDate}</p>
                                <p className="text-white"><span className="font-medium">{t("compare.endDate")}</span> {rental2.endDate}</p>
                                <p className="text-white"><span className="font-medium">{t("compare.city")}</span> {rental2.city}</p>
                            </div>
                            <div className="mt-5 text-center">
                                <p className="text-white"><span className="font-medium">{t("compare.duration")}</span> {duration1} {t("compare.days")}</p>
                                <p className="text-white"><span className="font-medium">{t("compare.totalPrice")}</span> €{totalPrice2}</p>
                            </div>
                        </div>
                    </div>
                </div> 
                <div className="">
                    {statusMessages && (
                        <div className="flex justify-center text-xl text-blue-600 my-2">
                            <ul>
                                {statusMessages.map(({ message, type }, index) => (
                                    <li className="status" key={index}>
                                        {message}
                                    </li>
                                ))}
                            </ul>
                        </div>
                    )}
                    <form onSubmit={handleSubmit} className="max-w-xl mx-auto p-4 shadow-md rounded-lg">
                        <div className="mb-4">
                            <label htmlFor="fuel">{t("compare.fuelAmount")} (L)</label>
                            <input type="number" name="fuel" id="fuel" className="w-full border rounded-lg p-2" value={fuel} onChange={(event => setFuel(event.target.value))} />
                        </div>
                        <div className="mb-4">
                            <label htmlFor="distance">{t("compare.distance")} (Miles)</label>
                            <input type="number" name="distance" id="distance" className="w-full border rounded-lg p-2" value={distance} onChange={(event => setDistance(event.target.value))} />
                        </div>
                        <div className="mb-4">
                            <label htmlFor="endDate">{t("compare.endDate2")}</label>
                            <input type="date" name="endDate" id="endDate" className="w-full border rounded-lg p-2" value={endDate} onChange={(event => setEndDate(event.target.value))} />
                        </div>
                        <div className="flex flex-row gap-2 justify-center">
                            <button className="mt-2 text-white bg-blue-600 hover:bg-blue-700 font-medium rounded-lg text-sm px-4 py-2 text-center" type="submit">
                                {t("compare.compare")}
                            </button>
                            <button onClick={handleReset} className="mt-2 text-white bg-green-600 hover:bg-green-700 font-medium rounded-lg text-sm px-4 py-2 text-center" type="button">
                                {t("compare.reset")}
                            </button>
                        </div>
                    </form>
                </div>    
            </div>
            <div className="overflow-x-auto max-w-screen-xl mx-auto">
                <table className="table-auto w-full text-left whitespace-no-wrap">
                    <thead>
                        <tr className="text-xs font-semibold tracking-wide text-left text-gray-500 uppercase border-b dark:border-gray-700 bg-gray-50">
                            <th className="px-4 py-3">{t("rental.car")}</th>
                            <th className="px-4 py-3">{t("rental.price")}</th>
                            <th className="px-4 py-3">{t("rental.startDate2")}</th>
                            <th className="px-4 py-3">{t("rental.endDate2")}</th>
                            <th className="px-4 py-3">{t("rental.city2")}</th>
                            <th className="px-4 py-3">{t("rental.actions")}</th>
                        </tr>
                    </thead>
                    <tbody className="bg-white divide-y dark:divide-gray-700 dark:bg-gray-800">
                        {data?.map((rental: Rental) => (
                            <tr key={rental.id} className="text-gray-700 dark:text-gray-400">
                                <td className="px-4 py-3">{rental.car.brand}</td>
                                <td className="px-4 py-3">{rental.price}</td>
                                <td className="px-4 py-3">{rental.startDate}</td>
                                <td className="px-4 py-3">{rental.endDate}</td>
                                <td className="px-4 py-3">{rental.city}</td>
                                <td className="px-4 py-3">
                                    {rental1.id !== rental.id && (<button onClick={() => handleCompareThis(rental.id!)} className="text-white bg-blue-600 hover:bg-blue-700 font-medium rounded-lg text-sm px-4 py-2 text-center">
                                        {t("compare.compareThis")}
                                    </button>)}
                                    {rental1.id === rental.id && (<button disabled className="text-white bg-gray-500 font-medium rounded-lg text-sm px-4 py-2 text-center">
                                        {t("compare.compareThis")}
                                    </button>)}
                                    {rental1.id !== rental.id && rental2.id && (<button onClick={handleSwitchRentals} className="m-2 text-white bg-emerald-600 hover:bg-emerald-700 font-medium rounded-lg text-sm px-4 py-2 text-center">
                                        {t("compare.switch")}
                                    </button>)}
                                    {rental1.id === rental.id &&  rental2.id && (<button disabled className="m-2 text-white bg-gray-500 font-medium rounded-lg text-sm px-4 py-2 text-center">
                                        {t("compare.compareThis")}
                                    </button>)}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default CompareRentalsForm;