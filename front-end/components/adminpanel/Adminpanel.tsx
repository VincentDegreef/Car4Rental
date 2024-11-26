

import React, { useEffect, useState } from "react";
import UserService from "@/services/UserService";
import { Car, Rent, Rental, StatusMessage, User } from "@/types";
import { get } from "http";
import useSWR, { mutate } from "swr";
import useInterval from "use-interval";
import CarService from "@/services/CarService";
import { useTranslation } from "next-i18next";

const AdminUsersList: React.FC = () => {
    const { t } = useTranslation();

    const [users, setUsers] = useState<User[]>([]);
    const [errors, setErrors] = useState<string | null>(null);
    const [statusMessages, setStatusMessages] = useState<StatusMessage[]>([]);
    const [loggedInUser, setLoggedInUser] = useState<User | null>(null);
    const[showDetails, setShowDetails] = useState<boolean>(false);
    const [selectedUserId, setSelectedUserId] = useState<number | null>(null);
    const [cars, setCars] = useState<Car[]>([]);
    const [rents, setRents] = useState<Rent[]>([]);
    const [showRentalDetails, setShowRentalDetails] = useState<boolean>(false);
    const [rentals, setRentals] = useState<Rental[]>([]);

    const getUsers = async () => {
        try {
            const response = await UserService.getUsers();
            if (response.ok) {
                const users = await response.json();
                setUsers(users);
            } else {
                throw new Error('Failed to get users');
            }
        } catch (error) {
            setErrors('Failed to get users. Please try again later.');
            console.error(error);
        }
    };

    

    useEffect(() => {
        if (sessionStorage.getItem("loggedInUserToken") === null) {
            return;
        }
        const userDetails = JSON.parse(
            sessionStorage.getItem("loggedInUserDetails") || ""
        );

        setLoggedInUser(userDetails);
    }, []);
    //use swr hook to fetch data
    const { data, isLoading, error } = useSWR('users', getUsers)

    useInterval(() => {
        mutate('users', getUsers())
    }, 5000)

    const handleShowDetails = async (id:number, email: string) => {
        const response = await UserService.getUserCars(id);
        if(response.ok){
            const cars = await response.json();
            setCars(cars);
        }

        const response2 = await UserService.getUserRentsList(email);
        if(response2.ok){
            const rents = await response2.json();
            setRents(rents);
        }
        setSelectedUserId(id);
        setShowDetails(!showDetails);
    }

    const handleShowRentalDetails = async (id:number) => {
        const response = await CarService.getRentalsByCarId(id)
        if(response.ok){
            const rentals = await response.json();
            setRentals(rentals);
        }
        setShowRentalDetails(!showRentalDetails);
    }
    const banUser = async (id: number) => {
        try {
            const response = await UserService.banUser(id);
            if (response.ok) {
                getUsers();
                setStatusMessages([{ message: "User has been banned", type: "success" }]);
            } else {
                throw new Error('Failed to ban user');
            }
        } catch (error) {
            setErrors('Failed to ban user. Please try again later.');
            console.error(error);
        }
    }

    const unbanUser = async (id: number) => {
        try {
            const response = await UserService.unbanUser(id);
            if (response.ok) {
                getUsers();
                setStatusMessages([{ message: "User has been unbanned", type: "success" }]);
            } else {
                throw new Error('Failed to unban user');
            }
        } catch (error) {
            setErrors('Failed to unban user. Please try again later.');
            console.error(error);
        }
    }

    return (
        <div className="bg-gray-100 min-h-screen p-4">
            {statusMessages.length > 0 && (
                <div className="text-center m-2 text-blue-500 font-bold">
                    <ul>
                        {statusMessages.map(({ message, type }, index) => (
                            <li key={index} className="status">
                                {message}
                            </li>
                        ))}
                    </ul>
                </div>
            )}
            {error ? (
                <p className="text-red-500">{t("adminPanel.error")} {error}</p>
            ) : (
                <table className="min-w-full bg-gray-300 border border-gray-400 shadow-lg mt-4">
                    <thead className="bg-gray-200">
                        <tr>
                            <th className="py-2 px-4 border">{t("adminPanel.username")}</th>
                            <th className="py-2 px-4 border">{t("adminPanel.email")}</th>
                            <th className="py-2 px-4 border">{t("adminPanel.phone")}</th>
                            <th className="py-2 px-4 border">{t("adminPanel.role")}</th>
                            <th className="py-2 px-4 border">{t("adminPanel.actions")}</th>
                            <th className="py-2 px-4 border">{t("adminPanel.details")}</th>
                        </tr>
                    </thead>
                    <tbody className="bg-gray-100 text-black">
                        {users.map((user: User) => (
                            <React.Fragment key={user.id}>
                            <tr className="text-center border-b border-gray-400">
                                <td className="py-2 px-4 border">{user.username}</td>
                                <td className="py-2 px-4 border">{user.email}</td>
                                <td className="py-2 px-4 border">{user.phoneNumber}</td>
                                <td className="py-2 px-4 border">{user.role && user.role.name}</td>
                                {loggedInUser && loggedInUser.id === user.id && (
                                    <td className="py-2 px-4 border">{t("adminPanel.banSelf")}</td>
                                )}
                                {loggedInUser && loggedInUser.id !== user.id && (
                                    <td className="py-2 px-4 border">
                                        <button className="bg-red-500 hover:bg-red-700 text-white font-bold py-1 px-2 rounded mx-1" onClick={() => banUser(user.id!)}>{t("adminPanel.ban")}</button>
                                        <button className="bg-green-500 hover:bg-green-700 text-white font-bold py-1 px-2 rounded mx-1" onClick={() => unbanUser(user.id!)}>{t("adminPanel.unban")}</button>
                                    </td>
                                )}
                                <td className="py-2 px-4 border">
                                    <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-2 rounded mx-1" onClick={() => handleShowDetails(user.id!, user.email)}>{t("adminPanel.showDetails")}</button>
                                </td>
                            </tr>
                            {showDetails && selectedUserId === user.id && (
                                <tr>
                                    <td colSpan={6} className="py-2 px-4 border">
                                        <p className="text-center font-bold">{t("adminPanel.userCars")}</p>
                                        {cars.length === 0 && <p className="text-center text-red-500 font-bold">No cars found</p>}
                                        { cars.length> 0 &&(<table className="min-w-full bg-gray-300 border border-gray-400 shadow-lg">
                                            <thead className="bg-gray-200">
                                                <tr>
                                                    <th className="py-2 px-4 border">{t("carOverview.brand")}</th>
                                                    <th className="py-2 px-4 border">{t("carOverview.type")}</th>
                                                    <th className="py-2 px-4 border">{t("carOverview.licensePlate")}</th>
                                                    <th className="py-2 px-4 border">{t("adminPanel.actions")}</th>
                                                </tr>
                                            </thead>
                                            <tbody className="bg-gray-100 text-black">
                                                <tr>
                                                    {cars.map((car: Car) => (
                                                        <React.Fragment key={car.id}>
                                                            <td className="py-2 px-4 border">{car.brand}</td>
                                                            <td className="py-2 px-4 border">{car.type}</td>
                                                            <td className="py-2 px-4 border">{car.licensePlate}</td>
                                                            <td className="py-2 px-4 border">
                                                                <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-2 rounded mx-1" onClick={() => handleShowRentalDetails(car.id!)}>{t("adminPanel.showRentals")}</button>
                                                            </td>
                                                        </React.Fragment>
                                                    ))}
                                                </tr>
                                                <tr>
                                                    <td colSpan={6} className="py-2 px-4 border">
                                                        {showRentalDetails && rentals.length === 0 && <p className="text-center text-red-500 font-bold">{t("adminPanel.noRentals")}</p>}
                                                        {showRentalDetails && rentals.length > 0 && (<table className="min-w-full bg-gray-300 border border-gray-400 shadow-lg">
                                                            <thead className="bg-gray-200">
                                                                <tr>
                                                                    <th className="py-2 px-4 border">{t("rentalForm.startDate")}</th>
                                                                    <th className="py-2 px-4 border">{t("rentalForm.endDate")}</th>
                                                                    <th className="py-2 px-4 border">{t("adminPanel.owner")}</th>
                                                                    <th className="py-2 px-4 border">{t("adminPanel.price")}</th>
                                                                    <th className="py-2 px-4 border">{t("rentalForm.city")}</th>

                                                                </tr>
                                                            </thead>
                                                            <tbody className="bg-gray-100 text-black">
                                                                {rentals.map((rental: Rental) => (
                                                                    <React.Fragment key={rental.id}>
                                                                        <tr>
                                                                            <td className="py-2 px-4 border">{rental.startDate}</td>
                                                                            <td className="py-2 px-4 border">{rental.endDate}</td>
                                                                            <td className="py-2 px-4 border">{rental.email}</td>
                                                                            <td className="py-2 px-4 border">{rental.price}</td>
                                                                            <td className="py-2 px-4 border">{rental.city}</td>
                                                                        </tr>
                                                                    </React.Fragment>
                                                                ))}
                                                            </tbody>
                                                        </table>)}
                                                    </td>
                                                    
                                                </tr>
                                            </tbody>
                                        </table>)}
                                    </td>
                                </tr>
                            )}
                            {showDetails && selectedUserId === user.id && (
                                <tr>
                                    
                                    <td colSpan={6} className="py-2 px-4 border">
                                        <p className="text-center font-bold">{t("adminPanel.userRents")}</p>
                                        {rents.length === 0 && <p className="text-center text-red-500 font-bold">{t("adminPanel.noRents")}</p>}
                                        {rents.length>0 &&(<table className="min-w-full bg-gray-300 border border-gray-400 shadow-lg">
                                            <thead className="bg-gray-200">
                                                <tr>
                                                    <th className="py-2 px-4 border">{t("rentalForm.car")}</th>
                                                    <th className="py-2 px-4 border">{t("rentalForm.startDate")}</th>
                                                    <th className="py-2 px-4 border">{t("rentalForm.endDate")}</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {rents.map((rent: Rent) => (
                                                    <React.Fragment key={rent.id}>
                                                        <tr>
                                                            <td className="py-2 px-4 border">{rent.rental.car.brand}</td>
                                                            <td className="py-2 px-4 border">{rent.startDate}</td>
                                                            <td className="py-2 px-4 border">{rent.endDate}</td>
                                                        </tr>
                                                    </React.Fragment>
                                                ))}
                                            </tbody>
                                        </table>)}

                                    </td>
                                </tr>
                            )}
                        </React.Fragment>))}

                    </tbody>
                </table>
            )}
        </div>
    );
}

export default AdminUsersList;