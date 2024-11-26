import React, { useState } from "react";
import UserService from "@/services/UserService";
import useSWR, { mutate } from "swr";
import useInterval from "use-interval";
import { Notification } from "@/types";
import NotificationService from "@/services/NotificationService";
import RentService from "@/services/RentService";
import { useTranslation } from "next-i18next";

const OwnerOverviewNotification: React.FC = () => {
    const { t } = useTranslation("common");
    const [received, setReceived] = useState<Notification[]>([]);
    const [sent, setSent] = useState<Notification[]>([]);
    const [confirmedOrCancelled, setConfirmedOrCancelled] = useState<number[]>([]);
    const [isEmty, setIsEmty] = useState<boolean>(true);
    const [showAllReceived, setShowAllReceived] = useState<boolean>(false);
    const [showAllSent, setShowAllSent] = useState<boolean>(false);

    const getUserNotifications = async () => {
        if (sessionStorage.getItem('loggedInUserDetails') === null) {
            return;
        }
        const userDetails = JSON.parse(sessionStorage.getItem('loggedInUserDetails') ?? "");
        const userEmail = userDetails.email;
        const response = await UserService.getUserNotifications(userEmail);
        if (response.ok) {
            const notifications = await response.json();
            if(notifications.length <= 0){
                setIsEmty(true);
                return;
            }
            splitNotifications(notifications);
            setIsEmty(false);
        }
    };

    const splitNotifications = (notifications: Notification[]) => {
        const rec: Notification[] = [];
        const send: Notification[] = [];
        notifications.forEach(notification => {
            if (notification.tag === 'big') {
                rec.push(notification);
            } else if (notification.tag === 'small') {
                send.push(notification);
            }
        });
        setReceived(rec);
        setSent(send);
    };

    const { data, isLoading, error } = useSWR('notifications', getUserNotifications)

    useInterval(() => {
        mutate('notifications', getUserNotifications())
    }, 20000)

    const onConfirm = async (notificationId: number) => {
        setConfirmedOrCancelled([...confirmedOrCancelled, notificationId]);
        NotificationService.ConfirmationRentNotification(notificationId)
    }

    const onCancel = async (notificationId: number) => {
        setConfirmedOrCancelled([...confirmedOrCancelled, notificationId]);
        const response = await NotificationService.GetNotificationById(notificationId);
        const idRent = await response.rentId;
        RentService.CancelRent(idRent);
        NotificationService.CancelRentNotification(notificationId);
    }

    const toggleShowAllReceived = () => {
        setShowAllReceived(!showAllReceived);
    };

    const toggleShowAllSent = () => {
        setShowAllSent(!showAllSent);
    };

    const clearNotifications = async () => {
        const userDetails = JSON.parse(sessionStorage.getItem('loggedInUserDetails') ?? "");
        const userEmail = userDetails.email;
        await UserService.clearUserNotifications(userEmail);
        setReceived([]);
        setSent([]);
        setIsEmty(true);
    };

    return (
        <>
            {isEmty && <div className="flex justify-center"><h1 className="text-2xl font-bold mb-4">{t("notifications.noNotifications")}</h1></div>}
            {!isEmty && 
            <section>
                <div className="flex justify-center">
                    <h1 className="text-4xl font-bold mt-4">{t("notifications.noti")}</h1>
                </div>
                <button onClick={clearNotifications} className="text-center bg-blue-500 text-black p-3 m-5 rounded hover:bg-blue-200 ">{t("notifications.clearNotifications")}</button>
                <div className="grid grid-cols-2 m-8">
                <div className="flex justify-center">
                    <div>
                        <h1 className="text-2xl font-bold mb-4">{t("notifications.received")}</h1>
                        <ul>
                            {received.slice(0, showAllReceived ? received.length : 3).map(notification => {
                                const isConfirmedOrCancelled = confirmedOrCancelled.includes(notification.id ?? 0);
                                return (
                                    <div key={notification.id} className="border border-gray-300 rounded-md p-2 mb-2 w-80 bg-black text-white text-center">
                                        <li className="p-2 font-bold text-center text-blue-400"> {t("notifications.sendAt")} {notification.receivedAt}</li>
                                        <li className="p-2 text-center text-lg">{notification.message}</li>
                                        {!isConfirmedOrCancelled && notification.seen === false && (
                                            <>
                                                <button onClick={() => onConfirm(notification.id ?? 0)} className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline mx-4 my-2">Confirm</button>
                                                <button onClick={() => onCancel(notification.id ?? 0)} className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline mx-4 my-2">Cancel</button>
                                            </>
                                        )}
                                    </div>
                                    );
                                })}
                        </ul>
                        {received.length > 3 && (
                            <button onClick={toggleShowAllReceived} className="block mx-auto mt-4 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 focus:outline-none">
                                {showAllReceived ? 'Show Less' : 'View More'}
                            </button>
                        )}
                    </div>
                </div>
                <div className="flex justify-center">
                    <div>
                        <h1 className="text-2xl font-bold mb-4">{t("notifications.sent")}</h1>
                        <ul>
                            {sent.slice(0, showAllSent ? sent.length : 3).map((notification: Notification, index:number) => (
                                <div key={index} className={`border border-gray-300 rounded-md p-2 mb-2 w-80 text-black ${notification.confirmed ? 'bg-green-500' : 'bg-red-500'}`}>
                                    <li className="p-2 font-bold text-center">{t("notifications.sendAt")} {notification.receivedAt}</li>
                                    <li className="p-2 text-center text-lg">{notification.message}</li>
                                </div>
                            ))}
                        </ul>
                        {sent.length > 3 && (
                            <button onClick={toggleShowAllSent} className="block mx-auto mt-4 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 focus:outline-none">
                                {showAllSent ? 'Show Less' : 'View More'}
                            </button>
                        )}
                    </div>
                </div>
            </div>
            </section>
            }
        </>
    );

};

export default OwnerOverviewNotification;
