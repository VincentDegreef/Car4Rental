import Header from "@/components/Header";
import CompareRentalsForm from "@/components/rentals/CompareRentalsForm";
import { useTranslation } from "next-i18next";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import Head from "next/head";
import { useRouter } from "next/router";
import { FaBackspace } from "react-icons/fa";

export const getServerSideProps = async (context: any) => {
    const {locale} = context;
  
    return {
      props: {
        ...(await serverSideTranslations(locale ?? "en", ['common']))
      },
    };
  };

const CompareOverview: React.FC = () => {
    const { t } = useTranslation();
    const router = useRouter();

    const handleBack = () => {
        router.back();
    }
    return (
        <>
            <Head>
                <title>{t("compare.compareRentals")}</title>
                <meta name="description" content="Compare two rentals to see which one is better" />
            </Head>
            <Header></Header>
            <div className="p-5">
                <FaBackspace size={30} onClick={handleBack} className="cursor-pointer hover:text-blue-500"/>
                <CompareRentalsForm></CompareRentalsForm>
            </div>
            
        </>
    );
};

export default CompareOverview;