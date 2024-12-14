import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { Navbar, Hero, LogIn, SignUp, Survey, Playlist } from "./components";
import { AuthProvider } from "./constants/index.jsx";
import styles from "./style";

const App = () => (
  <Router>
    <AuthProvider>
      <div className="bg-primary w-full overflow-hidden">
        <div className={`${styles.paddingX} ${styles.flexCenter}`}>
          <div className={`${styles.boxWidth}`}>
            <Navbar />
          </div>
        </div>

        <Routes>
          <Route path="/" element={<Hero />} />
          <Route path="/login" element={<LogIn />} />
          <Route path="/signup" element={<SignUp />} />
          <Route path="/survey" element={<Survey />} />
          {/* <Route path="/playlist" element={<Playlist />} /> */}
        </Routes>
      </div>
    </AuthProvider>
  </Router>
);

export default App;
