import React from "react";

const Playlist = () => {
  return (
    <div className="flex items-center justify-center min-h-screen">
      <div className="backdrop-blur-sm bg-white/10 p-8 rounded-xl w-96 shadow-xl">
        <h2 className="text-white font-krona text-2xl mb-6 text-center">
          Your Playlist
        </h2>

        <iframe
          src="https://open.spotify.com/embed/playlist/37i9dQZF1DXcBWIGoYBM5M"
          width="300"
          height="380"
          frameBorder="0"
          allowtransparency="true"
          allow="encrypted-media"
          className="rounded-lg"
        ></iframe>
      </div>
    </div>
  );
};

export default Playlist;
