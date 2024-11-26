import Header from "@/components/Header";
import UserService from "@/services/UserService";
import { StatusMessage } from "@/types";
import { verify } from "crypto";
import { useTranslation } from "next-i18next";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import Head from "next/head";
import { useRouter } from "next/router";
import { useEffect, useState } from "react";

export const getServerSideProps = async (context: any) => {
    const {locale} = context;
  
    return {
      props: {
        ...(await serverSideTranslations(locale ?? "en", ['common']))
      },
    };
  };  

const Verification: React.FC = () => {
    const {t} = useTranslation();

    const [email, setEmail] = useState<string>("");
    const [statusMessages, setStatusMessages] = useState<StatusMessage[]>([]);
    const [tries, setTries] = useState<number>(0);
    const [retry, setRetry] = useState<boolean>(false);
    const [isVerified, setIsVerified] = useState<boolean>(false);

    const [code1, setCode1] = useState<string>("");
    const [code2, setCode2] = useState<string>("");
    const [code3, setCode3] = useState<string>("");
    const [code4, setCode4] = useState<string>("");
    const [code5, setCode5] = useState<string>("");
    const [code6, setCode6] = useState<string>("");

    const [showCodeInput, setShowCodeInput] = useState<boolean>(false);

    const router = useRouter();

    const fetchVerificationEmail = async () => {
        if(sessionStorage.getItem("verifyEmail") === null) {
            return;
        }
        const verifyEmail = sessionStorage.getItem("verifyEmail");
        setEmail(verifyEmail!);
    };

    const validateEmail = () => {
        let formIsValid = true;

        if (email == "") {
            formIsValid = false;
            setStatusMessages([{ message: "Email is required", type: "error" }]);
        }

        return formIsValid;
    };

    const validateCode = () => {
        let formIsValid = true;

        if (code1 == "" || code2 == "" || code3 == "" || code4 == "" || code5 == "" || code6 == "") {
            formIsValid = false;
            setStatusMessages([{ message: "Code is required", type: "error" }]);
        }
        return formIsValid;
    };

    const handleVerificationEmail = async () => {     
        setStatusMessages([]);
        if (!validateEmail()) {
            return;
        }
        setTries(0);
        setRetry(false);
        
        const response = await UserService.sendVerificationEmail(email);
        if (response.ok) {
            setShowCodeInput(true);
            setStatusMessages([{ message: "Email sent", type: "success" }]);
        } else {
            setStatusMessages([{ message: "Email not sent", type: "error" }]);
        }
    };

    const verify = async () => {
        setStatusMessages([]);
        if (!validateCode()) {
            return;
        }
        const codeNumber = parseInt(code1 + code2 + code3 + code4 + code5 + code6);
        const response = await UserService.verifyEmailCode(email, codeNumber);
        if (response.ok) {
            setIsVerified(true);
            setStatusMessages([{ message: "Email verified", type: "success" }]);
            setTimeout(() => {
                router.push("/user/login");
            }, 2000);
            
        } else {
            setTries(tries + 1);
            if (tries == 2) {
                setStatusMessages([{ message: "Too many tries. Please try again later.", type: "error" }]);
                setRetry(true);
            }
           setStatusMessages([{ message: "Code not verified", type: "error" }]);
        }
    };

    useEffect(() => {
        fetchVerificationEmail();
    }, []);

    return(
        <>
            <Head>
                <title>{t("verification.verification")}</title>
                <meta name="description" content="Verification" />
            </Head>
            <Header></Header>
            <main className="flex justify-center items-start h-screen">
                
                <div className="max-w-lg m-20 p-4 shadow-md rounded-lg ">
                    <h1 className="text-4xl text-center mb-6">{t("verification.verification")}</h1>
                    {statusMessages && (
                        <div className="text-center m-2 font-bold">
                            <ul>
                                {statusMessages.map(({ message, type }, index) => (
                                    <li className={`status ${type === 'success' ? 'text-green-500' : 'text-red-500'}`} key={index}>
                                        {message}
                                    </li>
                                ))}
                            </ul>
                        </div>
                    )}
                    {!showCodeInput && (
                        <div>
                            <p className="text-center mb-6">{t("verification.pleaseVerify")}</p>
                            <div className="flex justify-center flex-col mb-6">
                                <label htmlFor="emailInput" className="p-2">{t("verification.email")}</label>
                                <input type="text" name="email" id="emailInput" value={email} onChange={(event) => setEmail(event.target.value)}
                                    className="shadow appearance-none border-4 rounded-lg w-96 py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" />
                            </div>
                            <div className="flex justify-center">
                                <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline" onClick={handleVerificationEmail}>Send Verification Email</button>
                            </div>
                        </div>
                    )}
                    {showCodeInput && !isVerified && (
                        <div>
                            <h3 className="text-xl text-center mb-2">{t("verification.emailSentTo")}</h3>
                            <p className="text-center mb-6">{t("verification.pleaseEnter")}</p>
                            <div className="flex justify-center mb-6">
                                <label className="m-2" htmlFor="codeInput">{t("verification.code")}</label>
                                <div className="m-1">
                                    <input type="text" maxLength={1} name="code" id="codeInput" value={code1} onChange={(event) => setCode1(event.target.value)}
                                        className="shadow appearance-none border-4 rounded-lg w-10 py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" />
                                </div> 
                                <div className="m-1">
                                    <input type="text" maxLength={1} name="code" id="codeInput" value={code2} onChange={(event) => setCode2(event.target.value)}
                                            className="shadow appearance-none border-4 rounded-lg w-10 py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" />
                                </div>
                                <div className="m-1">
                                    <input type="text" maxLength={1} name="code" id="codeInput" value={code3} onChange={(event) => setCode3(event.target.value)}
                                            className="shadow appearance-none border-4 rounded-lg w-10 py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" />
                                </div>
                                <div className="m-1">
                                    <input type="text" maxLength={1} name="code" id="codeInput" value={code4} onChange={(event) => setCode4(event.target.value)}
                                            className="shadow appearance-none border-4 rounded-lg w-10 py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" />
                                </div>
                                <div className="m-1">
                                    <input type="text" maxLength={1} name="code" id="codeInput" value={code5} onChange={(event) => setCode5(event.target.value)}
                                            className="shadow appearance-none border-4 rounded-lg w-10 py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" />
                                </div>
                                <div className="m-1">
                                    <input type="text" maxLength={1} name="code" id="codeInput" value={code6} onChange={(event) => setCode6(event.target.value)}
                                            className="shadow appearance-none border-4 rounded-lg w-10 py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" />
                                </div>
                            </div>
                            <p className="text-center p-2">{t("verification.numberOfTries")} {tries}</p>
                            <div className="flex justify-center">
                                {!retry && <button onClick={verify} className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 mr-5 rounded focus:outline-none focus:shadow-outline">Verify</button>}
                                {retry && <button onClick={handleVerificationEmail} className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">Resend Verification Email</button>}
                            </div>
                        </div>
                    )}
                </div>
            </main>
        </>
    );
};

export default Verification;