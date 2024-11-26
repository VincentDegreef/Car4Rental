import { StatusMessage } from "@/types";
import Head from "next/head";
import { useState } from "react";

import userService from "@/services/UserService";
import { useRouter } from "next/router";
import { useTranslation } from "next-i18next";


const UserLoginForm: React.FC = () => {
    const {t} = useTranslation();

    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    
    const [errors, setErrors] = useState<{ [key: string]: string }>({});
    const [statusMessages, setStatusMessages] = useState<StatusMessage[]>([]);

    const [checkVerify, setCheckVerify] = useState<boolean>(true);

    const router = useRouter();




    const validateForm = () => {
        let formIsValid = true;
        let errors: { [key: string]: string } = {};

        if (email == "") {
            formIsValid = false;
            errors['email'] = "Email is required";
        }

        if (password == "") {
            formIsValid = false;
            errors['password'] = "Password is required";
        }

        setErrors(errors);
        return formIsValid;
    };

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setErrors({});
        setStatusMessages([]);
        
        if (!validateForm()) {
            return;
        }
        
        const checkUserBanned = await userService.checkUserBanned(email);
        if(checkUserBanned.ok) {
            const data = await checkUserBanned.json();
            if(data) {
                setStatusMessages([{message: "User is banned", type: "error"}]);
                return;
            }
        }

        const checkVerificationResponse = await userService.checkIsUserVerified(email);
        if(!checkVerificationResponse.ok) {
            setStatusMessages([{message: `Something went wrong or no account for: ${email}.`, type: "error"}]);
            return;
        }
        const isVerified = await checkVerificationResponse.json();
        if (isVerified === false) {
            setStatusMessages([{message: "User not verified", type: "error"}]);
            setCheckVerify(false);
            return;
        }else {
            const loginUser = {email:email, password:password};

            const response: Response = await userService.login(loginUser);
    
            if (response.status === 200) {
                const data = await response.json();
                sessionStorage.setItem("loggedInUserDetails", JSON.stringify(data));
                sessionStorage.setItem("loggedInUserToken", JSON.stringify(
                    {token: data.token, 
                    user: data.user
                    })
                );
                setStatusMessages([{message: "Login successful", type: "success"}]);
                router.push("/");
            } else {
                setStatusMessages([{message: "Login failed", type: "error"}]);
            }
        }   
    }

    return (
        <>
            {statusMessages  &&(
                <div className="text-center m-2 text-blue-500 font-bold">
                    <ul>
                        {statusMessages.map(({message, type}, index) => (
                            <li className="status" key={index}>
                                {message}
                            </li>
                        ))}
                    </ul>
                </div>
            )}
            <div>
                <form onSubmit={handleSubmit} className="max-w-lg mx-auto p-4 shadow-md rounded-lg">
                    <div className="mb-4">
                        <label htmlFor="emailInput">{t("login.email")}</label>
                        <input type="email" id="emailInput" name="email" 
                        value={email} onChange={(event => setEmail(event.target.value))} 
                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"/>
                    </div>
                    
                    <div className="mb-4">
                        <label htmlFor="passwordInput">{t("login.password")}</label>
                        <input type="password" id="passwordInput" name="password" 
                        value={password} onChange={(event => setPassword(event.target.value))}
                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" />
                    </div>       
                    
    
                    {errors && (
                        <div>
                            <ul>
                                {Object.entries(errors).map(([key, value], index) => (
                                    <li className="error" key={index}>
                                        {value}
                                    </li>
                                ))}
                            </ul>
                        </div>
                    
                    )}
                
                    <button type="submit" onClick={() => router.push("/user/login")}
                    className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline mx-4">
                        {t("login.login")}
                    </button>

                    <button type="button" onClick={() => router.push("/user/register")}
                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline mx-4">
                        {t("login.register")}
                    </button>

                    {!checkVerify && (
                        <button onClick={() => router.push("/verification")}
                        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline mx-4">
                           {t("login.verify")}
                        </button>
                    )}
                </form>
            </div>
        </>
    );
} 

export default UserLoginForm;