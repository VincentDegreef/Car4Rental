import React, { useState } from 'react';
import { useRouter } from 'next/router';
import CarService from '@/services/CarService';
import { Car } from '@/types';
import { useTranslation } from 'next-i18next';

const CarForm: React.FC = () => {
  const { t } = useTranslation('common');

  const [car, setCar] = useState<Car>({
    brand: '',
    model: '',
    type: '',
    numberOfSeats: 0,
    numberOfChildSeats: 0,
    foldableRearSeats: false,
    towbar: false,
    licensePlate: '',
  });
  const [errors, setErrors] = useState<{ [key: string]: string }>({});

  const router = useRouter();

  const handleNexPage = () => {
    router.push('/car/overview');
  };

  const validateForm = () => {
    let formIsValid = true;
    let errors: { [key: string]: string } = {};

    if (!car.brand) {
      formIsValid = false;
      errors['brand'] = t("carForm.errors.brandRequired");
    }
    if (!car.type) {
      formIsValid = false;
      errors['type'] = t("carForm.errors.typeRequired");
    }
    if (!car.licensePlate) {
      formIsValid = false;
      errors['licensePlate'] = t("carForm.errors.licensePlateRequired");
    }
    if (car.numberOfSeats <= 0) {
      formIsValid = false;
      errors['numberOfSeats'] = t("carForm.errors.numberOfSeatsRequired");
    }

    setErrors(errors);
    return formIsValid;
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const target = e.target;
    const name = target.name;
    let value: string | number | boolean;

    if (target.type === 'checkbox') {
      value = (target as HTMLInputElement).checked;
    } else if (target.type === 'number') {
      value = parseInt(target.value, 10) || 0;
    } else {
      value = target.value;
    }

    setCar(prevState => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!validateForm()) {
      return;
    }

    let added = await CarService.AddCar(car);
    if (added) {
      handleNexPage();
    }
  };

  return (
    <form onSubmit={handleSubmit} className="max-w-lg mx-auto p-4 shadow-md rounded-lg">
      <div className="mb-4">
        <label htmlFor="brand">{t("carForm.brand")}:</label>
        <input type="text" id="brand" name="brand" value={car.brand} onChange={handleChange} className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" />
        {errors.brand && <p className="text-red-500 text-xs italic">{errors.brand}</p>}
      </div>

      <div className="mb-4">
        <label htmlFor="model">{t("carForm.model")}:</label>
        <input type="text" id="model" name="model" value={car.model} onChange={handleChange} className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" />
      </div>

      <div className="mb-4">
        <label htmlFor="type">{t("carForm.type")}:</label>
        <input type="text" id="type" name="type" value={car.type} onChange={handleChange} className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" />
        {errors.type && <p className="text-red-500 text-xs italic">{errors.type}</p>}
      </div>

      <div className="mb-4">
        <label htmlFor="numberOfSeats">{t("carForm.numberOfSeats")}:</label>
        <input type="number" id="numberOfSeats" name="numberOfSeats" value={car.numberOfSeats.toString()} onChange={handleChange} min="1" className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" />
        {errors.numberOfSeats && <p className="text-red-500 text-xs italic">{errors.numberOfSeats}</p>}
      </div>

      <div className="mb-4">
        <label htmlFor="numberOfChildSeats">{t("carForm.numberOfChildSeats")}:</label>
        <input type="number" id="numberOfChildSeats" name="numberOfChildSeats" value={car.numberOfChildSeats.toString()} onChange={handleChange} min="0" className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" />
      </div>

      <div className="mb-4 flex items-center">
        <input type="checkbox" name="foldableRearSeats" checked={car.foldableRearSeats} onChange={handleChange} className="mr-2" />
        <label htmlFor="foldableRearSeats" className="block text-gray-700 text-sm font-bold">{t("carForm.foldableRearSeats")}</label>
      </div>

      <div className="mb-4 flex items-center">
        <input type="checkbox" name="towbar" checked={car.towbar} onChange={handleChange} className="mr-2" />
        <label htmlFor="towbar" className="block text-gray-700 text-sm font-bold">{t("carForm.towbar")}</label>
      </div>

      <div className="mb-6">
        <label htmlFor="licensePlate">{t("carForm.licensePlate")}:</label>
        <input type="text" id="licensePlate" name="licensePlate" value={car.licensePlate} onChange={handleChange} className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" />
        {errors.licensePlate && <p className="text-red-500 text-xs italic">{errors.licensePlate}</p>}
      </div>

      <div className="flex justify-center space-x-4">
        <button type="submit" className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
          {t("carForm.addCar")}
        </button>
        <button type="button" onClick={handleNexPage} className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
          {t("carForm.cancel")}
        </button>
      </div>
    </form>
  );
};

export default CarForm;
