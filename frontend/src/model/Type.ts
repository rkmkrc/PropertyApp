type User = {
  id: number;
  name: string;
  surname: string;
  email: string;
  phoneNumber: string;
  // Add other fields as necessary
};

type Listing = {
  id: number;
  title: string;
  description: string;
  type: string;
  price: number;
  status: string;
  area: number;
  publishedDate: string;
  isTemplate?: boolean;
};

type Package = {
  title: string;
  type: string;
  description: string;
  expirationDate: string;
};
