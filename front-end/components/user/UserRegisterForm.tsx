import { StatusMessage } from "@/types";
import { useState } from "react";

import userService from "@/services/UserService";
import { useRouter } from "next/router";
import { useTranslation } from "next-i18next";


const UserRegisterForm: React.FC = () => {
    const {t} = useTranslation();

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [email, setEmail] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [roleValue, setRoleValue] = useState('');
    
    const [errors, setErrors] = useState<{ [key: string]: string }>({});
    const [statusMessages, setStatusMessages] = useState<StatusMessage[]>([]);

    

    const router = useRouter();


    const validateForm = () => {
        let formIsValid = true;
        let errors: { [key: string]: string } = {};

        if (username == "") {
            formIsValid = false;
            errors['username'] = "Username is required";
        }

        if (password == "") {
            formIsValid = false;
            errors['password'] = "Password is required";
        }

        if (confirmPassword == "") {
            formIsValid = false;
            errors['confirmPassword'] = "Confirm password is required";
        } else if (password !== confirmPassword) {
            formIsValid = false;
            errors['confirmPassword'] = "Passwords do not match";
        }

        if (email == "") {
            formIsValid = false;
            errors['email'] = "Email is required";
        }

        if (phoneNumber == "") {
            formIsValid = false;
            errors['phoneNumber'] = "Phone number is required";
        }

        if(roleValue == "") {
            formIsValid = false;
            errors['roleValue'] = "Role is required";
        }

        setErrors(errors);
        return formIsValid;
    };

    const handleRoleChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
        setRoleValue(event.target.value);
    };

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setErrors({});
        setStatusMessages([]);
        if(!validateForm()) {
            return;
        }

        if(password !== confirmPassword) {
            setStatusMessages([{message: "Passwords do not match", type: "error"}]);
            return;
        }else{
            const user = {
                username,
                password,
                email,
                phoneNumber
            };

            const registerRequestUser = {user,roleName:roleValue};

            const response = await userService.registerUser(registerRequestUser);
            if(response.status === 400){
                const data = await response.json();
                setStatusMessages([{message: "Failed to register user", type: "error"}]);
                setErrors(data);
                return;
            }
            const newUser = await response.json();
            sessionStorage.setItem("verifyEmail", newUser.email);
            setStatusMessages([{message: "User registered succesfully, redirecting to verification page", type: "success"}]);
            setTimeout(() => {
                router.push("/verification");
            }, 3000);
            return newUser;
        }
    }

    return (
        <>
            {statusMessages  &&(
                <div className="flex justify-center text-xl text-blue-600 my-2">
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
                <form onSubmit={handleSubmit} className="max-w-xl mx-auto p-4 shadow-md rounded-lg">
                    <div className="mb-4">
                        <label htmlFor="usernameInput">{t("register.username")}</label>
                        <input type="text" id="usernameInput" name="username" 
                        value={username} onChange={(event => setUsername(event.target.value))}
                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" />
                    </div>
                    
                    <div className="mb-4">
                        <label htmlFor="passwordInput">{t("register.password")}</label>
                        <input type="password" id="passwordInput" name="password" 
                        value={password} onChange={(event => setPassword(event.target.value))}
                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" />
                    </div>       
                   
                    <div className="mb-4">
                        <label htmlFor="confirmPasswordInput">{t("register.confirmPassword")}</label>
                        <input type="password" id="confirmPasswordInput" name="confirmPassword" 
                        value={confirmPassword} onChange={(event => setConfirmPassword(event.target.value))} 
                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"/>
                    </div>
                    
                    <div className="mb-4">
                        <label htmlFor="emailInput">{t("register.email")}</label>
                        <input type="email" id="emailInput" name="email" 
                        value={email} onChange={(event => setEmail(event.target.value))} 
                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"/>
                    </div>
                    
                    <div className="mb-4">
                        <label htmlFor="phoneNumberInput">{t("register.phone")}</label>
                        <input type="tel" id="phoneNumberInput" name="phoneNumber" 
                        value={phoneNumber} onChange={(event => setPhoneNumber(event.target.value))} 
                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"/>
                    </div>

                    <div className="mb-4">
                        <label htmlFor="roleInput">{t("register.role")}</label>
                        <select name="role" id="roleInput" onChange={handleRoleChange} className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                            <option value=""> -- select an option -- </option>
                            <option value="OWNER">{t("register.owner")}</option>
                            <option value="RENTER">{t("register.renter")}</option>
                        </select>
                    </div>
                    
                    {errors && (
                        <div className="my-2 text-red-700">
                            <ul >
                                {Object.entries(errors).map(([key, value], index) => (
                                    <li className="error" key={index}>
                                        {value}
                                    </li>
                                ))}
                            </ul>
                        </div>
                    
                    )}
                    
                    <button type="submit" 
                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
                         {t("register.register")}
                    </button>
                    <button type="button" onClick={() => router.push("/user/login")}
                    className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline mx-4">
                        {t("register.login")}
                    </button>
                </form>
            </div>
        </>
    );
} 

export default UserRegisterForm;