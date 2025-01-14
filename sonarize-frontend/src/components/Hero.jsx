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
      navigate("/login");
    }
  };

  return (
    <div>
      <div className="text-container">
        <div className="content">
          <h1>SONARIZE</h1>
          <div className="subheading">Discover Music Like Never Before</div>
          <div className="description">
            Join us and let your journey to perfect playlists begin!
          </div>
          <button
            type="button"
            onClick={handleButtonClick}
            className="submit-button text-center"
          >
            Dive into your soundtrack
          </button>
        </div>
      </div>

      {/* Gradientowe tło i dodatkowe elementy */}
      <div className="bg-gradient">
        <svg xmlns="http://www.w3.org/2000/svg">
          <defs>
            <filter id="goo">
              <feGaussianBlur
                in="SourceGraphic"
                stdDeviation="10"
                result="blur"
              />
              <feColorMatrix
                in="blur"
                mode="matrix"
                values="1 0 0 0 0  0 1 0 0 0  0 0 1 0 0  0 0 0 18 -8"
                result="goo"
              />
              <feBlend in="SourceGraphic" in2="goo" />
            </filter>
          </defs>
        </svg>
        <div className="bg-landing w-full h-full">
          <div className="g1"></div>
          <div className="g2"></div>
          <div className="g3"></div>
          <div className="g4"></div>
          <div className="g5"></div>
          <div className="interactive"></div>
        </div>
      </div>
    </div>
  );
};

export default Hero;
