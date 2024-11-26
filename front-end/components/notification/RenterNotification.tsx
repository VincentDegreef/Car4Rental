import UserService from "@/services/UserService";
import { useState } from "react";
import useSWR, { mutate } from "swr";
import useInterval from "use-interval";
import { Notification } from "@/types";
import { useTranslation } from "next-i18next";

const RenterOverviewNotification: React.FC = () => {
    const { t } = useTranslation("common");
    const [received, setReceived] = useState<Notification[]>([]);
    const [sent, setSent] = useState<Notification[]>([]);
    const [isEmty, setIsEmty] = useState<boolean>(true);
    const [showAllReceived, setShowAllReceived] = useState<boolean>(false);
    const [showAllSent, setShowAllSent] = useState<boolean>(false);

    const getUserNotifications = async () => {
        if(sessionStorage.getItem('loggedInUserDetails') === null) {
            return;
        }
        const userDetails = JSON.parse(sessionStorage.getItem('loggedInUserDetails') ?? "");
        const userEmail = userDetails.email;
        const response = await UserService.getUserNotifications(userEmail);
        if(response.ok) {
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
            if(notification.tag === 'small') {
               rec.push(notification);
            } else if(notification.tag === 'big'){
                send.push(notification);
            }
        });
        setReceived(rec);
        setSent(send);
    };

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

    const {data,isLoading,error} = useSWR('notifications', getUserNotifications )

    useInterval(() => {
        mutate('notifications', getUserNotifications())
    }, 2000)

    return (
        <>
        {isEmty && <h1 className="text-2xl font-bold text-center mt-8">{t("notifications.noNotifications")}</h1>}
        {!isEmty && 
            <section>
                <h1 className="text-4xl font-bold text-center mt-8">{t("notifications.noti")}</h1>
                <button onClick={clearNotifications} className="text-center bg-blue-500 text-black p-3 m-5 rounded hover:bg-blue-200 ">{t("notifications.clearNotifications")}</button>

                <div className="grid grid-cols-2 m-8">
                    <div className="flex justify-center">
                        <div>
                            <h1 className="text-2xl font-bold mb-4">{t("notifications.received")}</h1>
                            <ul>
                                {received.slice(0, showAllReceived ? received.length : 3).map((notification: Notification, index:number) => (
                                    <div key={index} className={`border border-gray-300 rounded-md p-2 mb-2 w-80 text-black ${notification.confirmed ? 'bg-green-500' : 'bg-red-500'}`}>
                                        <li className="p-2 font-bold text-center text-black">{t("notifications.sendAt")} {notification.receivedAt}</li>
                                        <li className="p-2 text-center text-lg">{notification.message}</li>
                                    </div>
                                ))}
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
                                    <div key={index} className="border border-gray-300 rounded-md p-2 mb-2 w-80 bg-black text-white">
                                        <li className="p-2 font-bold text-center text-blue-400">{t("notifications.sendAt")} {notification.receivedAt}</li>
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
        </section>}
    </>
    );
};

export default RenterOverviewNotification;
