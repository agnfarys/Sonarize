import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { Navbar, Hero, LogIn, SignUp, Survey, Playlist} from "./components";
import { AuthProvider } from "./constants/index.jsx";
import styles from "./style";
import PlaylistResult from "./components/PlaylistResult";
import MyProfile from "./components/MyProfile";
import HomePage from "./components/HomePage.jsx";


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
          <Route path="/home" element={<HomePage />} />
          <Route path="/login" element={<LogIn />} />
          <Route path="/signup" element={<SignUp />} />
          <Route path="/survey" element={<Survey />} />
          <Route path="/playlist-result" element={<PlaylistResult />} />
          <Route path="/profile" element={<MyProfile />} />
          {/* <Route path="/playlist" element={<Playlist />} /> */}
        </Routes>
      </div>
    </AuthProvider>
  </Router>
);

export default App;
