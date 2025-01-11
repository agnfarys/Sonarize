import { Link } from "react-router-dom";
import styles from "../style";
import { spotify } from "../assets";

const LogIn = () => {
  const handleSpotifySignUp = () => {
    const spotifyAuthUrl =
      "https://accounts.spotify.com/authorize?client_id=6bde7c93eba54dcb9b8bd1edec9d050b&response_type=code&redirect_uri=http://localhost:8080/api/auth/callback&scope=user-read-recently-played playlist-modify-public playlist-modify-private user-top-read user-read-private user-library-read playlist-read-private";
    window.location.href = spotifyAuthUrl;
  };

  return (
    <div class="bg-landing w-full h-screen">
      <div className="flex items-center justify-center min-h-screen flex-col">
        <div className="tile backdrop-blur-sm p-8 rounded-xl w-[30rem] h-[33rem] shadow-xl">
          <h2 className="login text-white font-krona text-2xl mb-6 text-center">
            login to your account
          </h2>

          <form className="font-raleway text-white space-y-6">
            <div>
              Email adress
              <input
                type="email"
                placeholder="e. g. youremail@gmail.com"
                className="insert w-full mt-3 p-3 rounded-lg text-white placeholder:text-white/50 focus:outline-none focus:border-sonarizeAccent"
              />
            </div>

            <div>
              Password
              <input
                type="password"
                placeholder="***********"
                className="insert w-full mt-3 p-3 mb-6 rounded-lg text-white placeholder:text-white/50 focus:outline-none focus:border-sonarizeAccent"
              />
            </div>

            <button
              type="submit"
              className="submit-button w-full p-3 rounded-lg font-krona transition-ease"
            >
              Sign In
            </button>

            <div className="text-center">or</div>

            <button
              type="button"
              className="spotify-button w-full p-3 rounded-lg bg-[#1DB954] text-white font-krona transition-colors flex items-center justify-center gap-2"
              onClick={handleSpotifySignUp}
            >
              <img src={spotify} alt="Spotify" className="w-6 h-6" />
              Sign in with Spotify
            </button>
          </form>
        </div>

        <div className="font-raleway font-semibold tracking-wide text-white text-center mt-6 text-2xl">
          DON'T HAVE AN ACCOUNT?{" "}
          <Link to="/signup" className=" text-secondary hover:underline ">
            JOIN US
          </Link>
        </div>
      </div>
    </div>
  );
};

export default LogIn;
