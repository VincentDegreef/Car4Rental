import { User, loginUser, registerRequestUser } from "@/types";


const registerUser = async(user: registerRequestUser) => {
    const response = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/rest/auth/register`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    });
    return response;
};

const login = async (user: loginUser) => {
    return await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/rest/auth/login`,{
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(user)
    
    })
};

const getUserNotifications = async (email: string) => {
    const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
    const response = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/users/usersNotfications/${email}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
    });
    return response;
};

const getUserRentsList = async (email: string) => {
    const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
    const response = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/users/usersRents/${email}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
    });
    return response;
};

const clearUserNotifications = async (email: string) => {
    const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
    const response = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/users/clearNotifications/${email}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
    });
    return response;
};


const getUserByEmail = async (email: string) => {
    const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
    return await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/users/email/${email}`,{
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        }
    })
}

const getUserById = async (id: number) => {
    const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
    return await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/users/${id}`,{
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        }
    })
}

const addBalance = async (id: number, amount: number) => {
    const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
    return await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/wallets/${id}/addBalance/${amount}`,{
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        }
    })
}

const subtractBalance = async (id: number, amount: number) => {
    const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
    return await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/wallets/${id}/subtractBalance/${amount}`,{
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        }
    })
}

const getBalance = async (username: string) => {
    const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
    const response = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/users/balance/${username}`,{
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        }
    });
    return response;
}  

const sendVerificationEmail = async (email: string) => {
    return await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/rest/auth/sendVerificationCode/${email}`,{
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    })
};

const verifyEmailCode = async (email: string, code: number) => {
    return await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/rest/auth/verifyCode/${email}/${code}`,{
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    })
};

const checkIsUserVerified = async (email: string) => {
    return await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/rest/auth/checkUserVerified/${email}`,{
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    })
};

const deleteAccount = async (email:string) => {
    const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
    return await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/users/delete/${email}`,{
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        }
    })
};

const getUsers = async () => {
    const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
    return await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/users`,{
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        }
    })
}

const banUser = async (id: number) => {
    const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
    return await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/users/ban/${id}`,{
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        }
    })
}

const unbanUser = async (id: number) => {
    const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
    return await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/users/unban/${id}`,{
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        }
    })
}

 const checkUserBanned = async (email: string) => {
    return await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/users/checkUserBanned/${email}`,{
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }

    })
}

const getUserCars = async (id:number) => {
    const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
    return await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/users/getUsersCars/${id}`,{
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        }
    })

}


const UserService = {
    registerUser,
    login,
    getUserByEmail,
    getUserById,
    addBalance,
    subtractBalance,
    getUserNotifications,
    getUserRentsList,
    clearUserNotifications,
    getBalance,
    sendVerificationEmail,
    verifyEmailCode,
    checkIsUserVerified,
    deleteAccount,
    getUsers,
    banUser,
    unbanUser,
    checkUserBanned,
    getUserCars
    
};

export default UserService;