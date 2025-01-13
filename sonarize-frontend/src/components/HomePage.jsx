import React, { useEffect, useState } from "react";

const HomePage = () => {
  const [lastPlaylist, setLastPlaylist] = useState(null);
  const [recommendations, setRecommendations] = useState([]);
  const [chatGPTRecommendations, setChatGPTRecommendations] = useState([]);
  const [featuredPlaylists, setFeaturedPlaylists] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      const userId = sessionStorage.getItem("userId");
      if (!userId) {
        console.error("No user ID found. Redirecting to login.");
        window.location.href = "/login";
        return;
      }

      try {
        // Pobierz ostatnią playlistę użytkownika
        const lastPlaylistResponse = await fetch(
          `http://localhost:8080/api/playlists/user/${userId}/last`
        );
        if (lastPlaylistResponse.ok) {
          const lastPlaylistData = await lastPlaylistResponse.json();
          setLastPlaylist(lastPlaylistData);
        } else {
          console.error("Failed to fetch last playlist:", await lastPlaylistResponse.text());
        }

        // Pobierz rekomendacje
        const recommendationsResponse = await fetch(
          `http://localhost:8080/api/recommendations/${userId}`
        );
        if (recommendationsResponse.ok) {
          const recommendationsData = await recommendationsResponse.json();

          // Losowanie playlist od różnych użytkowników
          const uniquePlaylists = randomizePlaylists(recommendationsData.recommendedPlaylists, 4);
          setRecommendations(uniquePlaylists);
          setChatGPTRecommendations(recommendationsData.chatGPTRecommendations.slice(0, 5));
        } else {
          console.error("Failed to fetch recommendations:", await recommendationsResponse.text());
        }
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };

    fetchData();
  }, []);

  // Funkcja losująca playlisty od różnych użytkowników
  const randomizePlaylists = (playlists, limit) => {
    const groupedByUser = playlists.reduce((acc, playlist) => {
      acc[playlist.userId] = acc[playlist.userId] || [];
      acc[playlist.userId].push(playlist);
      return acc;
    }, {});

    const uniquePlaylists = [];
    const userIds = Object.keys(groupedByUser);

    while (uniquePlaylists.length < limit && userIds.length > 0) {
      const randomIndex = Math.floor(Math.random() * userIds.length);
      const randomUserId = userIds.splice(randomIndex, 1)[0];
      const userPlaylists = groupedByUser[randomUserId];

      if (userPlaylists && userPlaylists.length > 0) {
        uniquePlaylists.push(userPlaylists[0]);
      }
    }

    return uniquePlaylists;
  };

  return (
    <div className="bg-landing w-full min-h-screen flex flex-col items-center py-10">
      <h1 className="font-krona uppercase text-white text-center text-3xl mb-8">
        Welcome Back!
      </h1>

      {/* Ostatnia playlista w widgecie */}
      {lastPlaylist && (
        <div className="tile backdrop-blur-sm p-8 rounded-xl w-[60rem] shadow-xl mb-10">
          <h2 className="font-krona text-white text-2xl mb-5">Last Played Playlist</h2>
          <iframe
            src={`https://open.spotify.com/embed/playlist/${lastPlaylist.playlistLink.split("/playlist/")[1]}`}
            width="100%"
            height="80"
            frameBorder="0"
            allow="autoplay; clipboard-write; encrypted-media; fullscreen; picture-in-picture"
            className="rounded-lg"
          ></iframe>
          <p className="text-gray-400 mt-4">
            Playlist created on: {new Date(lastPlaylist.createdAt).toLocaleString()}
          </p>
        </div>
      )}

      {/* Rekomendacje */}
      <div className="tile backdrop-blur-sm p-8 rounded-xl w-[60rem] shadow-xl mb-10">
        <h2 className="font-krona text-white text-2xl mb-5">Recommended for You</h2>

        {/* Rekomendacje od podobnych użytkowników */}
        <div className="mb-10">
          <h3 className="font-raleway text-white text-xl mb-3">From Similar Users</h3>
          <div className="grid grid-cols-2 gap-6">
            {recommendations.map((playlist) => (
              <div key={playlist.id} className="p-4 bg-gray-800 rounded-lg flex flex-col items-start">
                <iframe
                  src={`https://open.spotify.com/embed/playlist/${playlist.playlistLink.split("/playlist/")[1]}`}
                  width="100%"
                  height="80"
                  frameBorder="0"
                  allow="autoplay; clipboard-write; encrypted-media; fullscreen; picture-in-picture"
                  className="rounded-lg mb-4"
                ></iframe>
                <a
                  href={playlist.playlistLink}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="text-secondary hover:underline"
                >
                  Open Playlist
                </a>
                <p className="text-gray-400 mt-2 text-sm">
                  Created at: {new Date(playlist.createdAt).toLocaleString()}
                </p>
              </div>
            ))}
          </div>
        </div>

        {/* Rekomendacje od ChatGPT */}
        <div className="mb-10">
          <h3 className="font-raleway text-white text-xl mb-3">Generated by ChatGPT</h3>
          <ul className="text-white grid grid-cols-1 gap-3">
            {chatGPTRecommendations.map((song, index) => (
              <li key={index} className="bg-gray-800 p-3 rounded-lg">
                {song}
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default HomePage;
