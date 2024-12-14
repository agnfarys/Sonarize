import { createContext, useContext, useState } from "react";

export const navLinks = [
  {
    id: "login",
    title: "LOG IN",
    path: "/login",
  },
  {
    id: "signup",
    title: "SIGN UP",
    path: "/signup",
  },
];

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  return (
    <AuthContext.Provider value={{ isLoggedIn, setIsLoggedIn }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
