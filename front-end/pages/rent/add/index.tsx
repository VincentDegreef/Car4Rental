import  Header  from "@/components/Header";
import RentForm from "@/components/rent/RentForm";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import { useTranslation } from "next-i18next";

export const getServerSideProps = async (context: any) => {
  const {locale} = context;

  return {
    props: {
      ...(await serverSideTranslations(locale ?? "en", ['common']))
    },
  };
};

const AddCarPage: React.FC = () => {
  const { t } = useTranslation('common');
  return (
    <div>
      <Header />
      <main>
        <div className="flex justify-center text-4xl">
          <h2>{t("rentAdd.rentCar")}</h2>
        </div>
        <RentForm />
      </main>
    </div>
  );
};

export default AddCarPage;
