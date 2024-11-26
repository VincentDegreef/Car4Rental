const GetCars = async () => {
    try{
        const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
        const res = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/cars`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization : `Bearer ${token}`,
            }
        });
    
        if(!res.ok){
            throw new Error("Failed to fetch cars")
        }
        return res.json()
    } catch(error){
        console.error("Error loading cars:",error)
    }
}

const AddCar = async (carForm: any) : Promise<Boolean> => {
    try {
        const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
        const userId = JSON.parse(sessionStorage.getItem('loggedInUserDetails') ?? '').id;
        const response = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/users/${userId}/addCar`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization : `Bearer ${token}`,
          },
          body: JSON.stringify({ ...carForm, numberOfSeats: parseInt(carForm.numberOfSeats.toString(), 10) }),
        });
    
        if (response.ok) {
          return true;
        } else {
          console.error('Server error: Failed to add car');
          return false;
        }
      } catch (error) {
        console.error('Error adding car:', error);
        return false;
      }
}

const GetCarById = async (id: number) => {
    try{
        const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
        const res = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/cars/getCar/${id}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization : `Bearer ${token}`,
            }
        });
    
        if(!res.ok){
            throw new Error("Failed to fetch car");
        }
        return res.json();

    } catch(error){
        console.error("Error loading car:",error);

    }
}

const DeleteCar = async (id: number) : Promise<Boolean> => {
    try {
        const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
        const response = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/cars/delete/${id}`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
        });
    
        if (response.ok) {
          return true;
        } else {
          console.error('Server error: Failed to delete car');
          return false;
        }
      } catch (error) {
        console.error('Error deleting car:', error);
        return false;
      }
};

const MakeRental = async (carId:number, rentalInput: any) : Promise<Boolean> => {
  try {
      const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
      const response = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/cars/${carId}/makeRental`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(rentalInput),
      });
      
      if (response.ok) {
        return true;
      } else {
        console.error('Server error: Failed to add rental');
        return false;
      }
    } catch (error) {
      console.error('Error adding rental:', error);
      return false;
    }
}

const GetCarsByUserId = async (id: number) => {
    try{
        const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
        const res = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/cars/getByUserId/${id}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization : `Bearer ${token}`,
            }
        });
    
        if(!res.ok){
            throw new Error("Failed to fetch cars");
        }
        return res.json();

    } catch(error){
        console.error("Error loading cars:",error);
    }
}

const getRentalsByCarId = async (carId: number) => {
  const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
  return await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/cars/getRentalsByCarId/${carId}`,{
      method: "GET",
      headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
      }
  })
};

const CarService = {
    GetCars,
    AddCar,
    GetCarById,
    DeleteCar,
    MakeRental,
    GetCarsByUserId,
    getRentalsByCarId
}

export default CarService;