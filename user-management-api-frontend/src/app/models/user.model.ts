export interface User {
  id: number;
  username: string;
  email: string;
  password: String;
  estado?: boolean;
  fechaCreacion: string;
}

export interface UserCreate {
  username: String;
  email: String;
  password?: string;
  estado?: boolean;
}
