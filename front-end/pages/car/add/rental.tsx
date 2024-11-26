import Header from "@/components/Header";
import RentalForm from "@/components/RentalForm";
import { useTranslation } from "next-i18next";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";

export const getServerSideProps = async (context: any) => {
  const { locale } = context;

  return {
    props: {
      ...(await serverSideTranslations(locale ?? "en", ['common']))
    },
  };
};

const AddRentalToCarPage: React.FC = () => {
  const { t } = useTranslation('common');

  return (
    <div>
      <Header />
      <main>
        <div className="flex justify-center text-4xl my-8">
          <h2>{t("addRentalToCarPage.addRentalForCar")}</h2>
        </div>
        <RentalForm />
      </main>
    </div>
  );
};

export default AddRentalToCarPage;
