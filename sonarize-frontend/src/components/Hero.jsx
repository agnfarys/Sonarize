import { useNavigate } from "react-router-dom";
import { useAuth } from "../constants";
import styles from "../style";

const Hero = () => {
  const navigate = useNavigate();
  const { isLoggedIn } = useAuth();

  const handleButtonClick = () => {
    if (isLoggedIn) {
      navigate("/survey");
    } else {
      navigate("/survey"); // Change this to "/login" after implementing login
    }
  };

  return (
    <section
      id="home"
      className={`flex md:flex-row flex-col ${styles.paddingY}`}
    >
      <div className="bg-landing w-full h-screen">
        <div
          className={`flex-1 ${styles.flexStart} m-6 items-center justify-center flex-col xl:px-0 sm:px-16 px-6`}
        >
          <h1 className="flex flex-row items-center mx-auto justify-center text-gradient font-krona text-[40px] sm:text-[64px] md:text-[80px] lg:text-[128px]">
            SONARIZE
          </h1>
          <div className="flex flex-row items-center mx-auto justify-center font-raleway font-semibold tracking-widest text-white text-[32px]">
            Discover Music Like Never Before
          </div>
          <div className="flex flex-row items-center mx-auto justify-center font-raleway font-semibold tracking-wide mt-5 pt-10 text-white text-[20px]">
            Join us and let your journey to perfect playlists begin!
          </div>

          <button
            type="button"
            onClick={handleButtonClick}
            className="submit-button w-[30rem] mt-8 p-3 rounded-lg font-krona transition-ease"
          >
            Dive into your soundtrack
          </button>
        </div>
      </div>
    </section>
  );
};

export default Hero;
