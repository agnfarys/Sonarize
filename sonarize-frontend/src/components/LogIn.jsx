import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const LogIn = () => {
  const navigate = useNavigate();

  const handleSpotifySignUp = () => {
    const spotifyAuthUrl = new URL("https://accounts.spotify.com/authorize");
    spotifyAuthUrl.searchParams.append(
      "client_id",
      "6bde7c93eba54dcb9b8bd1edec9d050b"
    );
    spotifyAuthUrl.searchParams.append("response_type", "code");
    spotifyAuthUrl.searchParams.append(
      "redirect_uri",
      "https://sonarize-chbte2bqe6e5a0gz.westeurope-01.azurewebsites.net/api/auth/callback"
    );
    spotifyAuthUrl.searchParams.append(
      "scope",
      [
        "user-read-recently-played",
        "playlist-modify-public",
        "playlist-modify-private",
        "user-top-read",
        "user-read-private",
        "user-library-read",
        "playlist-read-private",
      ].join(" ")
    );

    window.location.href = spotifyAuthUrl.toString();
  };

  const handleCallback = async () => {
    const code = new URLSearchParams(window.location.search).get("code");
    if (!code) return;

    try {
      const response = await fetch(
        `https://sonarize-chbte2bqe6e5a0gz.westeurope-01.azurewebsites.net/api/auth/callback?code=${code}`
      );
      if (response.ok) {
        const data = await response.json();

        // Zapisz dane w sessionStorage
        sessionStorage.setItem("userId", data.userId);
        sessionStorage.setItem("spotifyAccessToken", data.spotifyAccessToken);

        navigate("/survey");
      } else {
        console.error("Error during Spotify login:", await response.text());
      }
    } catch (error) {
      console.error("Error during callback:", error);
    }
  };

  useEffect(() => {
    handleCallback();
  }, []);

  return (
    <div className="bg-gradient">
      <div className="flex items-center justify-center min-h-screen flex-col">
        <div className="tile backdrop-blur-sm p-8 rounded-xl w-[30rem] h-[15rem] shadow-xl">
          <h2 className="login text-white font-krona text-2xl mb-6 text-center">
            Login to your account
          </h2>
          <button
            className="spotify-button w-full p-3 rounded-lg text-white font-krona transition-colors flex items-center justify-center gap-2"
            onClick={handleSpotifySignUp}
          >
            <img
              className="spotify-logo"
              src="./src/assets/spotify.svg"
              alt="Spotify"
            />
            Sign in with Spotify
          </button>
        </div>
      </div>
    </div>
  );
};

export default LogIn;
