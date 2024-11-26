import { Rent } from "@/types";

const GetRents = async (): Promise<any> => {
  try {
    const token = JSON.parse(
      sessionStorage.getItem("loggedInUserToken") ?? ""
    ).token;
    const res = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/rents`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    if (!res.ok) {
      throw new Error("Failed to fetch rents");
    }
    return res.json();
  } catch (error) {
    console.error("Error loading rents:", error);
  }
};

const CancelRent = async (id: number): Promise<Boolean> => {
  try {
    const token = JSON.parse(
      sessionStorage.getItem("loggedInUserToken") ?? ""
    ).token;
    const response = await fetch(
      `${process.env.NEXT_PUBLIC_BACKEND_URL}/rents/cancel/${id}`,
      {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      }
    );

    if (response.ok) {
      return true;
    } else {
      console.error("Server error: Failed to cancel rent");
      return false;
    }
  } catch (error) {
    console.error("Error canceling rent:", error);
    return false;
  }
};

const MakeRent = async (
  rentalId: number,
  rentInput: any,
  userEmail: string
) => {
  const token = JSON.parse(
    sessionStorage.getItem("loggedInUserToken") ?? ""
  ).token;
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_BACKEND_URL}/rents/makeRent/${rentalId}/${userEmail}`,
    {
      method: "POST",
      // mode: 'no-cors',
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(rentInput),
    }
  );
  return response;
};

const GetRentsByEmail = async (email: string): Promise<any> => {
  try {
    const token = JSON.parse(
      sessionStorage.getItem("loggedInUserToken") ?? ""
    ).token;
    const res = await fetch(
      `${process.env.NEXT_PUBLIC_BACKEND_URL}/rents/rentsByEmail/${email}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      }
    );
    if (!res.ok) {
      throw new Error("Failed to fetch rent");
    }
    return res.json();
  } catch (error) {
    console.error("Error loading rent:", error);
  }
};

const GetRentsByUserId = async (id: number) => {
  try {
    const token = JSON.parse(
      sessionStorage.getItem("loggedInUserToken") ?? ""
    ).token;
    const res = await fetch(
      `${process.env.NEXT_PUBLIC_BACKEND_URL}/rents/getByUserId/${id}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      }
    );

    if (!res.ok) {
      throw new Error("Failed to fetch cars");
    }
    return res.json();
  } catch (error) {
    console.error("Error loading cars:", error);
  }
};

const GetRentById = async (id: number): Promise<any> => {
  try {
    /*const token = JSON.parse(
      sessionStorage.getItem("loggedInUserToken") ?? ""
    ).token;*/
    const res = await fetch(
      `${process.env.NEXT_PUBLIC_BACKEND_URL}/rents/${id}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          //Authorization: `Bearer ${token}`,
        },
      }
    );

    if (!res.ok) {
      throw new Error("Failed to fetch rent");
    }
    return res.json();
  } catch (error) {
    console.error("Error loading rent:", error);
  }
};

const CheckOut = async (rentId: number, rent: any) => {
  const token = JSON.parse(
    sessionStorage.getItem("loggedInUserToken") ?? ""
  ).token;
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_BACKEND_URL}/rents/checkout/${rentId}`,
    {
      method: "POST",
      // mode: 'no-cors',
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(rent),
    }
  );
  return response;
};

const AddComplaint = async (rentId: number, complaintMessage: string) => {
  const token = JSON.parse(
    sessionStorage.getItem("loggedInUserToken") ?? ""
  ).token;
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_BACKEND_URL}/complaints/add/${rentId}`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: complaintMessage,
    }
  );
  return response;
};

const RentService = {
  GetRents,
  CancelRent,
  MakeRent,
  GetRentsByEmail,
  GetRentsByUserId,
  GetRentById,
  CheckOut,
  AddComplaint,
};

export default RentService;
