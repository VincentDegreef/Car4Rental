import React, { useState, useEffect } from 'react';
import AdminUsersList from '@/components/adminpanel/Adminpanel';
import Header from '@/components/Header';
import Head from 'next/head';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';

export const getServerSideProps = async (context: any) => {
    const { locale } = context;
  
    return {
      props: {
        ...(await serverSideTranslations(locale ?? "en", ['common']))
      },
    };
  };

const AdminPanel: React.FC = () => {
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
                <title>{t("adminPanel.title")}</title>
                <link rel="icon" href="/favicon.ico" /> 
            </Head>
            <Header />
            <main>
                {role !== "ADMIN" && (
                    <div className='m-20'>
                        <h1 className="flex justify-center text-4xl text-red-500">{t("adminPanel.unauthorized")}</h1>
                        <p className="flex justify-center text-2xl ">{t("adminPanel.notAuthorizedMessage")}</p>
                    </div>
                )}
                {role === "ADMIN" &&(<AdminUsersList />)}
            </main>
        </>
    );
}

export default AdminPanel;
