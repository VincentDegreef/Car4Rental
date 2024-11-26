import AlertDialog from "@/components/AlertDialog";
import { ConfirmationModal } from "@/components/ConfirmationModal";
import  Header  from "@/components/Header";
import CarService from "@/services/CarService";
import RentService from "@/services/RentService";
import RentalService from "@/services/RentalService";
import UserService from "@/services/UserService";
import { Car, Rent, Rental } from "@/types";
import { log } from "console";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import { useRouter } from "next/router";
import { use, useEffect, useState } from "react";
import Popup from "reactjs-popup";
import useSWR, { mutate } from "swr";
import useInterval from "use-interval";

export const getServerSideProps = async (context: any) => {
  const {locale} = context;

  return {
    props: {
      ...(await serverSideTranslations(locale ?? "en", ['common']))
    },
  };
};

const UserPage: React.FC = () => {
    const router = useRouter();
    const [deleteEmail, setDeleteEmail] = useState('');
    const [alertMessage, setAlertMessage] = useState('');
    const { userId } = router.query;


    let userIdInt = 0;
    if (typeof userId === 'string') {
        userIdInt = parseInt(userId);
    }

    const [amount, setAmount] = useState(0);

    const [cars, setCars] = useState<Car[]>([]);
    const [rentals, setRentals] = useState<Rental[]>([]);
    const [rents, setRents] = useState<Rent[]>([]);
    const [role,setRole] = useState({
      id: 0,
      name: ''
    });
    const [user, setUser] = useState({
        id: 0,
        username: '',
        password: '',
        email: '',
        phoneNumber: '',
        balance: 0,
        role: role
    });

    const addBalance = async (amount: number) => {
        UserService.addBalance(userIdInt, amount);
        setAlertMessage("Balance updated successfully.");
    }

    const showPopup = () => {
      setDeleteEmail(user.email);
      document.getElementById("modal")?.classList.remove("hidden");
    };

    const closePopup = () => {
      document.getElementById("modal")?.classList.add("hidden");
    };

    const removeAccount = async () => {
        UserService.deleteAccount(deleteEmail);
        sessionStorage.clear();
        router.push('/');
    }
    


    const fetchData = async () => {
      const loggedInUserDetails = JSON.parse(sessionStorage.getItem('loggedInUserDetails') || '');
      setUser(loggedInUserDetails);

      let userBalance = 0;
      
      if(userId) {
          const carsData = await CarService.GetCarsByUserId(userIdInt);
          setCars(carsData);

          const rentalsData = await RentalService.GetRentalsByUserId(userIdInt);
          setRentals(rentalsData);

          const rentsData = await UserService.getUserRentsList(loggedInUserDetails.email)
          const rentsList = await rentsData.json();
          setRents(rentsList);

          const balanceData = await UserService.getBalance(loggedInUserDetails.username);
          const wallet = await balanceData.json();
          userBalance = wallet.balance;
      }
      return { userBalance};
    }  

    

    const { data, isLoading, error } = useSWR('fetchData', fetchData);

    useInterval(() => {
      mutate('rents', fetchData());
    }, 5000);

      
    
    return (
    <div>
      <Header />
      <main>
        <div className="flex justify-center text-4xl my-8">
          <h2>Profile {user.username} </h2>
        </div>
        <div className="grid grid-cols-3 gap-4 w-full max-w-screen-xl mx-auto">
          <section className="col-span-1 flex flex-col gap-4">
            <div className="bg-stone-100 shadow-sm p-8 rounded-xl">
              <h3 className="text-xl font-semibold mb-4">Profile</h3>
              <div className="">
                <p>Username: {user.username}</p>
                <p>Email: {user.email}</p>
                <p>Phone Number: {user.phoneNumber}</p>
                <p>{user.role.name}</p>
              </div>
              <button onClick={showPopup} className="text-white bg-red-600 hover:bg-red-700 font-medium rounded-lg text-sm px-4 py-2 text-center my-2">Delete Account</button>

            </div>
            <div className="bg-stone-100 shadow-sm p-8 rounded-xl">
              <h3 className="text-xl font-semibold mb-4">Wallet</h3>
              <p>Balance: €{data?.userBalance}</p>
              <div className="flex items-center mt-4">
                <input
                  type="number"
                  placeholder="Amount"
                  className="mr-2 px-4 py-2 border border-gray-300 rounded-md"
                  onChange={(event) => {setAmount(parseFloat(event.target.value))}}
                />
                <button className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600"
                onClick={() => addBalance(amount)}>
                  Deposit
                </button>
              </div>
            </div>
          </section>
          <section className="col-span-2 flex flex-col gap-4">
            <div className="bg-stone-100 shadow-sm p-8 rounded-xl">
              <h3 className="text-xl font-semibold mb-4">Cars</h3>
              <table className="table-auto w-full text-left whitespace-no-wrap">
                      <thead>
                          <tr className="text-xs font-semibold tracking-wide text-left text-gray-500 uppercase border-b dark:border-gray-700 bg-gray-50">
                          <th className="px-4 py-3">Brand</th>
                          <th className="px-4 py-3">Model</th>
                          <th className="px-4 py-3">License Plate</th>
                          <th className="px-4 py-3">Actions</th>
                          </tr>
                      </thead>
                      <tbody className="bg-white divide-y dark:divide-gray-700 dark:bg-gray-800">
                          {cars && cars.length > 0 && cars.map(car => {
                              return (<tr className="text-gray-700 dark:text-gray-400" key={car.id}>
                                  <td className="px-4 py-3">{car.brand}</td>
                                  <td className="px-4 py-3">{car.model}</td>
                                  <td className="px-4 py-3">{car.licensePlate}</td>
                                  <td className="px-4 py-3 flex gap-4">
                                      <button
                                      className="text-white bg-red-600 hover:bg-red-700 font-medium rounded-lg text-sm px-4 py-2 text-center"
                                      >
                                      Remove
                                      </button>
                                  </td>
                              </tr>)
                          })}
                      </tbody>
                      </table>
            </div>
            <div className="bg-stone-100 shadow-sm p-8 rounded-xl">
              <h3 className="text-xl font-semibold mb-4">Rentals</h3>
              <table className="table-auto w-full text-left whitespace-no-wrap">
                      <thead>
                          <tr className="text-xs font-semibold tracking-wide text-left text-gray-500 uppercase border-b dark:border-gray-700 bg-gray-50">
                          <th className="px-4 py-3">Price</th>
                          <th className="px-4 py-3">Start Date</th>
                          <th className="px-4 py-3">End Date</th>
                          <th className="px-4 py-3">License Plate</th>
                          <th className="px-4 py-3">Actions</th>
                          </tr>
                      </thead>
                      <tbody className="bg-white divide-y dark:divide-gray-700 dark:bg-gray-800">
                          {rentals && rentals.length > 0 && rentals.map(rental => {
                          return (<tr className="text-gray-700 dark:text-gray-400" key={rental.id}>
                          <td className="px-4 py-3">€{rental.price} /day</td>
                          <td className="px-4 py-3">{rental.startDate}</td>
                          <td className="px-4 py-3">{rental.endDate}</td>
                          <td className="px-4 py-3">{rental.car.licensePlate}</td>
                          <td className="px-4 py-3 flex gap-4">
                          <button className="text-white bg-red-600 hover:bg-red-700 font-medium rounded-lg text-sm px-4 py-2 text-center">
                              Remove
                          </button>
                          </td>
                          </tr>)})}
                      </tbody>
                      </table>
            </div>
            <div className="bg-stone-100 shadow-sm p-8 rounded-xl">
              <h3 className="text-xl font-semibold mb-4">Rents</h3>
              <table className="table-auto w-full text-left whitespace-no-wrap">
                      <thead>
                          <tr className="text-xs font-semibold tracking-wide text-left text-gray-500 uppercase border-b dark:border-gray-700 bg-gray-50">
                          <th className="px-4 py-3">Email</th>
                          <th className="px-4 py-3">Phone Number</th>
                          <th className="px-4 py-3">Start Date</th>
                          <th className="px-4 py-3">End Date</th>
                          <th className="px-4 py-3">License Plate</th>
                          <th className="px-4 py-3">Actions</th>
                          </tr>
                      </thead>
                      <tbody className="bg-white divide-y dark:divide-gray-700 dark:bg-gray-800">
                          {rents && rents.length > 0 && rents.map(rent => {
                          return (<tr className="text-gray-700 dark:text-gray-400" key={rent.id}>
                          <td className="px-4 py-3">{rent.email}</td>
                          <td className="px-4 py-3">{rent.phoneNumber}</td>
                          <td className="px-4 py-3">{rent.rental.startDate}</td>
                          <td className="px-4 py-3">{rent.rental.endDate}</td>
                          <td className="px-4 py-3">{rent.rental.car.licensePlate}</td>
                          <td className="px-4 py-3 flex gap-4">
                          <button className="text-white bg-red-600 hover:bg-red-700 font-medium rounded-lg text-sm px-4 py-2 text-center">
                              Remove
                          </button>
                          </td>
                          </tr>)})}
                      </tbody>     
              </table>
            </div>
          </section>
        </div>
        <AlertDialog content={alertMessage} onClose={()=>setAlertMessage("")}></AlertDialog>
      </main>
      <ConfirmationModal title="Are you sure you want to delete your account?" 
      message="This action cannot be undone." 
      onConfirm={removeAccount} onCancel={closePopup} />
    </div>
  );
};
  
  export default UserPage;