import { useTranslation } from "next-i18next";

interface ConfirmationModalProps {
  title: string;
  message: string;
  onConfirm: () => void;
  onCancel: () => void;
}

interface ConfirmationModalOkProps {
  title: string;
  message: string;
  onOk: () => void;
}

export const ConfirmationModal = ({
  title,
  message,
  onConfirm,
  onCancel,
}: ConfirmationModalProps) => {
  const { t } = useTranslation();
  return (
    <div id="modal" className="fixed z-10 inset-0 overflow-y-auto hidden">
      <div className="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div className="fixed inset-0 transition-opacity" aria-hidden="true">
          <div className="absolute inset-0 bg-gray-500 opacity-75"></div>
        </div>
        <span
          className="hidden sm:inline-block sm:align-middle sm:h-screen"
          aria-hidden="true"
        >
          &#8203;
        </span>
        <div className="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
          <div className="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
            <h3 className="text-lg font-medium text-gray-900">{title}</h3>
            <p className="mt-4">{message}</p>
            <div className="flex justify-center mt-8">
              <button
                onClick={() => onConfirm()}
                className="mr-2 bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
              >
                {t("carOverview.yes")}
              </button>
              <button
                onClick={() => onCancel()}
                className="bg-gray-300 hover:bg-gray-400 text-gray-700 font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
              >
                {t("carOverview.no")}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export const ConfirmationModalOk = ({
  title,
  message,
  onOk,
}: ConfirmationModalOkProps) => {
  return (
    <div id="modalOk" className="fixed z-10 inset-0 overflow-y-auto hidden">
      <div className="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div className="fixed inset-0 transition-opacity" aria-hidden="true">
          <div className="absolute inset-0 bg-gray-500 opacity-75"></div>
        </div>
        <span
          className="hidden sm:inline-block sm:align-middle sm:h-screen"
          aria-hidden="true"
        >
          &#8203;
        </span>
        <div className="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
          <div className="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
            <h3 className="text-lg font-medium text-gray-900">{title}</h3>
            <p className="mt-4">{message}</p>
            <div className="flex justify-center mt-8">
              <button
                onClick={() => onOk()}
                className="bg-gray-300 hover:bg-gray-400 text-gray-700 font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
              >
                Ok
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
