import Header from "@/components/Header";
import UserRegisterForm from "@/components/user/UserRegisterForm";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import Head from "next/head";
import { useTranslation } from "next-i18next";

export const getServerSideProps = async (context: any) => {
    const {locale} = context;
  
    return {
      props: {
        ...(await serverSideTranslations(locale ?? "en", ['common']))
      },
    };
  };  


const Register: React.FC = () => {
    const {t} = useTranslation();
    return (
        <>
            <Head>
                <title>{t("register.register")}</title>
                <meta name="description" content="Register" />
            </Head>
            <Header></Header>
            <main> 
                <h1 className="flex justify-center text-4xl">{t("register.register")}</h1>
                <UserRegisterForm></UserRegisterForm>
            </main>
        </>
    );
}

export default Register;