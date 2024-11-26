import Header from "@/components/Header";
import CarForm from "@/components/AddCar";
import { useTranslation } from "next-i18next";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";

export const getServerSideProps = async (context: any) => {
  const {locale} = context;

  return {
    props: {
      ...(await serverSideTranslations(locale ?? "en", ['common']))
    },
  };
};

const AddCarPage: React.FC = () => {
  const { t } = useTranslation();

  return (
    <div>
      <Header />
      <main>
        <div className="flex justify-center text-4xl">
          <h2>{t("addCarPage.addNewCar")}</h2>
        </div>
        <CarForm />
      </main>
    </div>
  );
};

export default AddCarPage;
