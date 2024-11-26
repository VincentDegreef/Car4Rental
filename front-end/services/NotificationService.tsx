const GetNotifications = async () => {
    try{
        const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
        const res = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/notifications`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            }
        });

        if (!res.ok){
            throw new Error("Failed to fetch notifications")
        }
        return res.json()
    } catch(error){
        console.error("Error loading cars:", error)
    }
}

const MakeNotification = async (rentId: number, renterEmail: String) => {
        const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
        const response = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/notifications/makeRentRequestNotification/${renterEmail}/${rentId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            },
        });
        return response;
};

const CancelRentNotification = async (notificationId: number) => {
        const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
        const res = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/notifications/makeCancelNotification/${notificationId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            },
        });

        return res;
}

const ConfirmationRentNotification = async (notificationId: number) => {
        const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
        const res = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/notifications/makeConfirmNotification/${notificationId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            },
        });

        return res;
}

const GetNotificationById = async (notificationId: number) => {
    try{
        const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
        const res = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/notifications/${notificationId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            }
        });

        if (!res.ok){
            throw new Error("Failed to fetch notifications")
        }
        return res.json();
    } catch(error){
        console.error("Error loading cars:", error)
    }
}

const GetNotificationByRentId = async (rentId: number) => {
        const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
        const res = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/notifications/getNotificationByRentId/${rentId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            }
        });

        return res;
}

const RentCancelNotification = async (rentId: number, renterEmail: string) => {
        const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
        const res = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/notifications/rentCancelNotification/${rentId}/${renterEmail}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            },
        });

        return res;
} 



const NotificationService = {
    GetNotifications,
    MakeNotification,
    CancelRentNotification,
    ConfirmationRentNotification,
    GetNotificationById,
    GetNotificationByRentId,
    RentCancelNotification
}

export default NotificationService;