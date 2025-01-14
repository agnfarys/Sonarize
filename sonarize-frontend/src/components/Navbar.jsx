import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { close, menu } from "../assets";

const Navbar = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [username, setUsername] = useState("");
  const [toggle, setToggle] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const userId = sessionStorage.getItem("userId");
    const storedUsername = sessionStorage.getItem("username");
    setIsLoggedIn(!!userId);
    setUsername(storedUsername || "");
  }, []);

  const handleLogout = async () => {
    console.log("Logout successful.");
    // Usuń dane z localStorage
    sessionStorage.removeItem("userId");
    sessionStorage.removeItem("spotifyAccessToken");
    sessionStorage.removeItem("username");
    setIsLoggedIn(false);
    navigate("/login");
  };

  const handleLogoClick = () => {
    if (isLoggedIn) {
      navigate("/home");
    } else {
      navigate("/");
    }
  };

  return (
    <nav className="w-full flex py-3 justify-between items-center navbar">
      <div
        className="text-2xl font-krona orange-gradient cursor-pointer"
        onClick={handleLogoClick}
      >
        SONARIZE
      </div>

      {/* Desktop Navigation */}
      <ul className="hidden sm:flex list-none justify-end items-center flex-1">
        {isLoggedIn ? (
          <>
            <li className="mx-4 text-white font-krona">Welcome, {username}</li>
            <li className="mx-4">
              <Link to="/survey" className="text-white hover:text-secondary">
                New Survey
              </Link>
            </li>
            <li className="mx-4">
              <Link to="/profile" className="text-white hover:text-secondary">
                My Profile
              </Link>
            </li>
            <li className="mx-4">
              <button
                onClick={handleLogout}
                className="text-red-500 hover:text-red-700"
              >
                Logout
              </button>
            </li>
          </>
        ) : (
          <>
            <li className="mx-4">
              <Link to="/login" className="text-white hover:text-secondary">
                Login
              </Link>
            </li>
          </>
        )}
      </ul>

      {/* Mobile Navigation */}
      <div className="sm:hidden flex flex-1 justify-end items-center">
        <img
          src={toggle ? close : menu}
          alt="menu"
          className="w-[28px] h-[28px] object-contain"
          onClick={() => setToggle(!toggle)}
        />
        <div
          className={`${
            toggle ? "flex" : "hidden"
          } p-6 bg-black-gradient absolute top-20 right-0 mx-4 my-2 min-w-[140px] rounded-xl sidebar`}
        >
          <ul className="list-none flex flex-col items-start">
            {isLoggedIn ? (
              <>
                <li className="mb-4 text-white font-krona">
                  Welcome, {username}
                </li>
                <li className="mb-4">
                  <Link
                    to="/survey"
                    className="text-white hover:text-secondary"
                    onClick={() => setToggle(false)}
                  >
                    New Survey
                  </Link>
                </li>
                <li className="mb-4">
                  <Link
                    to="/profile"
                    className="text-white hover:text-secondary"
                    onClick={() => setToggle(false)}
                  >
                    My Profile
                  </Link>
                </li>
                <li>
                  <button
                    onClick={() => {
                      setToggle(false);
                      handleLogout();
                    }}
                    className="text-red-500 hover:text-red-700"
                  >
                    Logout
                  </button>
                </li>
              </>
            ) : (
              <>
                <li className="mb-4">
                  <Link
                    to="/login"
                    className="text-white hover:text-secondary"
                    onClick={() => setToggle(false)}
                  >
                    Login
                  </Link>
                </li>
                <li>
                  <Link
                    to="/signup"
                    className="text-white hover:text-secondary"
                    onClick={() => setToggle(false)}
                  >
                    Sign Up
                  </Link>
                </li>
              </>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
