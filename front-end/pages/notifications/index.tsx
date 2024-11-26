import Header from "@/components/Header";
import OwnerOverviewNotification from "@/components/notification/OwnerNotification";
import RenterOverviewNotification from "@/components/notification/RenterNotification";
import UserLoginForm from "@/components/user/UserLoginForm";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import Head from "next/head";
import { useEffect, useState } from "react";
import { useTranslation } from "next-i18next";

export const getServerSideProps = async (context: any) => {
    const {locale} = context;
  
    return {
      props: {
        ...(await serverSideTranslations(locale ?? "en", ['common']))
      },
    };
  };

const Notifications: React.FC = () => {
    const { t } = useTranslation('common');
    const [role, setRole] = useState<string>("");

    useEffect(() => {
        const userDetails = sessionStorage.getItem('loggedInUserDetails');
        if (userDetails !== null) {
            const parsedUserDetails = JSON.parse(userDetails);

            setRole(parsedUserDetails.role.name);
            
        }
    }, []);
    return (
        <>
            <Head>
                <title>{t("notifications.title")}</title>
                <meta name="description" content="Login" />
            </Head>
            <Header></Header>
            <main> 
                {role === "RENTER" ? <RenterOverviewNotification /> : <OwnerOverviewNotification />}
            </main>
        </>
    );
}

export default Notifications;