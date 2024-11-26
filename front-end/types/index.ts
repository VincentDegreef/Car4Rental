export type Greeting = {
  id: number;
  message: string;
};

export type Car = {
  id?: number;
  brand: string;
  model: string;
  type: string;
  numberOfSeats: number;
  numberOfChildSeats: number;
  foldableRearSeats: boolean;
  towbar: boolean;
  licensePlate: string;
  user?: User;
};

export type Rental = {
  id?: number;
  startDate: string;
  startTime: string;
  endDate: string;
  endTime: string;
  price: number;
  street: string;
  number: number;
  postal: number;
  city: string;
  email: string;
  phoneNumber: string;
  car: Car;
};

export type RentRequest = {
  id?: number;
  phoneNumber: string;
  email: string;
  identification: string;
  birthDate: string;
  drivingLicenceNumber: string;
  startDate: string;
  startMillage: number;
  startFuelQuantity: number;
};

export type Rent = {
  id?: number;
  phoneNumber: string;
  email: string;
  identification: string;
  birthDate: string;
  drivingLicenceNumber: string;
  rental: Rental;
  startDate: string;
  startMillage: number;
  startFuelQuantity: number;
  endDate: string;
  returnMillage: number;
  returnFuelQuantity: number;
  totalPrice: number;
};

export type CheckOut = {
  endDate: string;
  returnMillage: number;
  returnFuelQuantity: number;
};

export type RentalSearch = {
  email: string;
  brand: string;
  city: string;
  startDate: string;
  endDate: string;
  priceUnder: number;
};

export type Notification = {
  id?: number;
  rentId: number;
  message: string;
  tag: string;
  seen: boolean;
  confirmed: boolean;
  receivedAt: string;
};

export type StatusMessage = {
  message: string;
  type: "error" | "success";
};

export type User = {
    id?: number;
    username: string;
    password: string;
    email: string;
    phoneNumber: string;
    role?: Role;
    
};

export type Role = {
    id?: number;
    name: string;
};


export type registerRequestUser = {
  user: User;
  roleName: string;
};

export type loginUser = {
  email: string;
  password: string;
};
