import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { Navbar, Hero, LogIn, SignUp, Survey, Playlist } from "./components";
import { AuthProvider } from "./constants/index.jsx";
import styles from "./style";
import PlaylistResult from "./components/PlaylistResult";
import MyProfile from "./components/MyProfile";
import HomePage from "./components/HomePage.jsx";
import { useEffect } from "react";

const App = () => {
  useEffect(() => {
    const interBubble = document.querySelector(".interactive");
    if (!interBubble) return;

    let curX = 0;
    let curY = 0;
    let tgX = 0;
    let tgY = 0;

    function move() {
      curX += (tgX - curX) / 20;
      curY += (tgY - curY) / 20;
      interBubble.style.transform = `translate(${Math.round(
        curX
      )}px, ${Math.round(curY)}px)`;
      requestAnimationFrame(move);
    }

    window.addEventListener("mousemove", (event) => {
      tgX = event.clientX;
      tgY = event.clientY;
    });

    move();
  }, []); // Wykonuje się tylko raz po załadowaniu komponentu

  return (
    <Router>
      <AuthProvider>
        <div className="bg-primary w-full overflow-hidden">
          {/* Sekcja nawigacji */}
          <div className={`${styles.paddingX} ${styles.flexCenter}`}>
            <div className={`${styles.boxWidth}`}>
              <Navbar />
            </div>
          </div>

          {/* Główne ścieżki aplikacji */}
          <Routes>
            <Route path="/" element={<Hero />} />
            <Route path="/home" element={<HomePage />} />
            <Route path="/login" element={<LogIn />} />
            <Route path="/signup" element={<SignUp />} />
            <Route path="/survey" element={<Survey />} />
            <Route path="/playlist-result" element={<PlaylistResult />} />
            <Route path="/profile" element={<MyProfile />} />
          </Routes>
        </div>
      </AuthProvider>
    </Router>
  );
};

export default App;
