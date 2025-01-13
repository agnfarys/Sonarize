import React from "react";
import { useLocation } from "react-router-dom";

const PlaylistResult = () => {
  const location = useLocation();
  const params = new URLSearchParams(location.search);
  const playlistUrl = params.get("playlistUrl");

  if (!playlistUrl) {
    return <div className="text-center text-white">Playlist URL not found.</div>;
  }

  const playlistId = playlistUrl.split("/playlist/")[1];

  return (
    <div className="bg-landing w-full h-screen flex items-center justify-center">
      <div className="tile backdrop-blur-sm p-8 rounded-xl w-[30rem] shadow-xl text-center">
        <h1 className="font-krona uppercase text-white text-center text-2xl mb-5">
          Your Personalized Playlist
        </h1>
        <iframe
          src={`https://open.spotify.com/embed/playlist/${playlistId}`}
          width="300"
          height="380"
          frameBorder="0"
          allow="encrypted-media"
          title="Spotify Playlist"
        ></iframe>
      </div>
    </div>
  );
};

export default PlaylistResult;
