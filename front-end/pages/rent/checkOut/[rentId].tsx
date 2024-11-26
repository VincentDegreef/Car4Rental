import Header  from "@/components/Header";
import CheckOutForm from "@/components/rent/CheckOutForm";
import { t } from "i18next";
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

const CheckOutPage: React.FC = () => {
  const { t } = useTranslation('common');
  return (
    <div>
      <Header />
      <main>
        <div className="flex justify-center text-4xl">
          <h2>{t("checkOut.checkOutCar")}</h2>
        </div>
        <CheckOutForm />
      </main>
    </div>
  );
};

export default CheckOutPage;
