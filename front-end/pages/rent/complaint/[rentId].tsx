import Header from "@/components/Header";
import ComplaintsForm from "@/components/rent/ComplaintsForm";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import { useTranslation } from 'next-i18next';

export const getServerSideProps = async (context: any) => {
  const { locale } = context;

  return {
    props: {
      ...(await serverSideTranslations(locale ?? "en", ['common']))
    },
  };
};

const ComplaintsPage: React.FC = () => {
  const { t } = useTranslation('common');

  return (
    <div>
      <Header />
      <main>
        <div className="flex justify-center text-4xl">
          <h2>{t("complaint.fileComplaint")}</h2>
        </div>
        <ComplaintsForm />
      </main>
    </div>
  );
};

export default ComplaintsPage;
