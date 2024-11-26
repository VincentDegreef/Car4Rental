import { Rental, RentalSearch } from "@/types";

const GetRentals = async () : Promise<any> => {
    try{
        const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
        const res = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/rentals`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization : `Bearer ${token}`,
            }

            });
    
        if(!res.ok){
            throw new Error("Failed to fetch rentals")
        }
        return res.json()
    }catch(error){
        console.error("Error loading rentals:",error)

    }
}

const CancelRental = async (id: number) : Promise<Boolean> => {
    try {
        const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
        const response = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/rentals/cancel/${id}`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
        });
    
        if (response.ok) {
          return true;
        } else {
          console.error('Server error: Failed to cancel rental');
          return false;
        }
      } catch (error) {
        console.error('Error canceling rental:', error);
        return false;
      }

}

const GetRentalById = async (id: number) : Promise<any> => {
    try{
        const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
        const res = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/rentals/rentalById/${id}`, {
          method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization : `Bearer ${token}`,
            }
            });
              if(!res.ok){
                throw new Error("Failed to fetch rental")
        }
        return res.json()
    }catch(error){
        console.error("Error loading rental:",error)

    }
}

const getRentalsByEmail = async (email: string) : Promise<Rental[]> => {
    try{
        const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
        const res = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/rentals/rentalsByEmail/${email}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization : `Bearer ${token}`

            }

            });
    
        if(!res.ok){
              throw new Error("Failed to fetch rentals")
        }
        return res.json()
    }catch(error){
        console.error("Error loading rentals:",error)
    }
    return [];
}

const getRentalsByBrand = async (brand: string) : Promise<Rental[]> => {
    try{
        const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
        const res = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/rentals/rentalsByCarBrand/${brand}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization : `Bearer ${token}`
            }
            });
    
        if(!res.ok){
            throw new Error("Failed to fetch rentals")
        }
        return res.json()
    }catch(error){
        console.error("Error loading rentals:",error)
    }
    return [];
}

const getRentalsByCity = async (city: string) : Promise<Rental[]> => {
    try{
        const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
        const res = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/rentals/rentalsByCity/${city}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization : `Bearer ${token}`
            }

            });
    
        if(!res.ok){
            throw new Error("Failed to fetch rentals")
        }
        return res.json()
    }catch(error){
        console.error("Error loading rentals:",error)
    }
    return [];
}

const getRentalsByStartDate = async (startDate: string) : Promise<Rental[]> => {
    try{
        const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
        const res = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/rentals/rentalsByStartDate/${startDate}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization : `Bearer ${token}`
            }

            });
    
        if(!res.ok){
            throw new Error("Failed to fetch rentals")
        }
        return res.json()
    }catch(error){
        console.error("Error loading rentals:",error)
    }
    return [];
}

const getRentalsByEndDate = async (endDate: string) : Promise<Rental[]> => {
    try{
        const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
        const res = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/rentals/rentalsByEndDate/${endDate}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization : `Bearer ${token}`
            }
            });
    
        if(!res.ok){
            throw new Error("Failed to fetch rentals")
        }
        return res.json()
    }catch(error){
        console.error("Error loading rentals:",error)
    }
    return [];
}

const GetRentalsByUserId = async (id: number) => {
    try{
        const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
        const res = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/rentals/getByUserId/${id}`, {
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

const GetRentalsUnderPrice = async (price: number) => {
    try{
        const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
        const res = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/rentals/getByPriceUnder/${price}`, {
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

const compareRentals = async (rentalId1: number, rentalId2: number,miles:number,  fuelQuantity:number,  endDate: string) => {
    try {
        const token = JSON.parse(sessionStorage.getItem('loggedInUserToken') ?? '').token;
        const res = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/rentals/compare/${rentalId1}/${rentalId2}/${miles}/${fuelQuantity}/${endDate}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization : `Bearer ${token}`,
            },
        });

        if (!res.ok) {
            throw new Error("Failed to compare rentals");
        }
        return res.json();
    } catch (error) {
        console.error("Error comparing rentals:", error);
    }
};

const RentalService = {
    GetRentals,
    CancelRental,
    GetRentalById,
    getRentalsByEmail,
    getRentalsByBrand,
    getRentalsByCity,
    getRentalsByStartDate,
    getRentalsByEndDate,
    GetRentalsByUserId,
    GetRentalsUnderPrice,
    compareRentals

}

export default RentalService;