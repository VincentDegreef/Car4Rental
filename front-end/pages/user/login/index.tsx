import Header from "@/components/Header";
import UserLoginForm from "@/components/user/UserLoginForm";
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

const Login: React.FC = () => {
    const {t} = useTranslation();

    return (
        <>
            <Head>
                <title>{t("login.login")}</title>
                <meta name="description" content="Login" />
            </Head>
            <Header></Header>
            <main> 
                <h1 className="flex justify-center text-4xl">{t("login.login")}</h1>
                <UserLoginForm></UserLoginForm>
            </main>
        </>
    );
}

export default Login;