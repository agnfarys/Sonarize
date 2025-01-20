import React, { useState, useEffect } from "react";

const MyProfile = () => {
  const [activeTab, setActiveTab] = useState("info"); // Zakładki: info, playlists, surveys
  const [userData, setUserData] = useState(null);
  const [playlists, setPlaylists] = useState([]);
  const [surveys, setSurveys] = useState([]);
  const [userSummary, setUserSummary] = useState(null); // Dane z podsumowania użytkownika

  useEffect(() => {
    const fetchProfileData = async () => {
      const userId = sessionStorage.getItem("userId");
      if (!userId) {
        console.error("No user ID found. Redirecting to login.");
        return;
      }

      try {
        // Pobranie danych użytkownika
        const userResponse = await fetch(
          `https://sonarize-chbte2bqe6e5a0gz.westeurope-01.azurewebsites.net/api/users/user/${userId}`,
          {
            method: "GET",
            headers: { "Content-Type": "application/json" },
          }
        );
        const userData = await userResponse.json();
        setUserData(userData);

        // Pobranie playlist użytkownika
        const playlistsResponse = await fetch(
          `https://sonarize-chbte2bqe6e5a0gz.westeurope-01.azurewebsites.net/api/playlists/user/${userId}`,
          {
            method: "GET",
            headers: { "Content-Type": "application/json" },
          }
        );
        const playlistsData = await playlistsResponse.json();
        setPlaylists(playlistsData);

        // Pobranie ankiet użytkownika
        const surveysResponse = await fetch(
          `https://sonarize-chbte2bqe6e5a0gz.westeurope-01.azurewebsites.net/api/surveys/user/${userId}`,
          {
            method: "GET",
            headers: { "Content-Type": "application/json" },
          }
        );
        const surveysData = await surveysResponse.json();
        setSurveys(surveysData);

        // Pobranie podsumowania użytkownika
        const summaryResponse = await fetch(
          `https://sonarize-chbte2bqe6e5a0gz.westeurope-01.azurewebsites.net/api/playlists/user/${userId}/summary`,
          {
            method: "GET",
            headers: { "Content-Type": "application/json" },
          }
        );
        const summaryData = await summaryResponse.json();
        setUserSummary(summaryData);
      } catch (error) {
        console.error("Error fetching profile data:", error);
      }
    };

    fetchProfileData();
  }, []);

  if (!userData || !userSummary) {
    return <div className="text-white">Loading profile...</div>;
  }

  return (
    <div className="bg-landing w-full min-h-screen flex flex-col items-center py-10">
      {/* Nagłówek */}
      <h1 className="font-krona uppercase text-white text-center text-3xl mb-8">
        My Profile
      </h1>

      {/* Zakładki */}
      <div className="flex justify-center mb-6">
        <button
          className={`px-6 py-2 mx-2 rounded-lg ${
            activeTab === "info"
              ? "bg-secondary text-white"
              : "bg-gray-800 text-gray-300"
          }`}
          onClick={() => setActiveTab("info")}
        >
          User Info
        </button>
        <button
          className={`px-6 py-2 mx-2 rounded-lg ${
            activeTab === "playlists"
              ? "bg-secondary text-white"
              : "bg-gray-800 text-gray-300"
          }`}
          onClick={() => setActiveTab("playlists")}
        >
          Playlists
        </button>
        <button
          className={`px-6 py-2 mx-2 rounded-lg ${
            activeTab === "surveys"
              ? "bg-secondary text-white"
              : "bg-gray-800 text-gray-300"
          }`}
          onClick={() => setActiveTab("surveys")}
        >
          Surveys
        </button>
      </div>

      {/* Zawartość zakładek */}
      <div className="tile backdrop-blur-sm p-8 rounded-xl w-[60rem] shadow-xl">
        {activeTab === "info" && (
          <div>
            <h2 className="font-krona text-white text-2xl mb-5">
              User Information
            </h2>
            <p className="text-white mb-4">
              <strong>Name:</strong> {userData.username}
            </p>
            <p className="text-white mb-4">
              <strong>Account Created:</strong>{" "}
              {new Date(userData.createdAt).toLocaleString()}
            </p>
            <h3 className="font-krona text-white text-xl mb-4">Summary</h3>
            <p className="text-white mb-4">
              <strong>Top Genres:</strong>{" "}
              {userSummary.topGenres?.join(", ") || "N/A"}
            </p>
            <p className="text-white mb-4">
              <strong>Top Artists:</strong>{" "}
              {userSummary.topArtists?.join(", ") || "N/A"}
            </p>
            <p className="text-white mb-4">
              <strong>Total Playlists Created:</strong>{" "}
              {userSummary.totalPlaylists || 0}
            </p>
          </div>
        )}

        {activeTab === "playlists" && (
          <div>
            <h2 className="font-krona text-white text-2xl mb-5">
              Your Playlists
            </h2>
            <div className="overflow-y-auto max-h-[300px]">
              {playlists.length > 0 ? (
                <ul className="text-white">
                  {playlists.map((playlist) => (
                    <li key={playlist.id} className="mb-3">
                      <a
                        href={playlist.playlistLink}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="text-secondary hover:underline"
                      >
                        {playlist.playlistLink}
                      </a>{" "}
                      <span className="text-gray-400">
                        ({new Date(playlist.createdAt).toLocaleString()})
                      </span>
                    </li>
                  ))}
                </ul>
              ) : (
                <p className="text-gray-400">You have no playlists yet.</p>
              )}
            </div>
          </div>
        )}

        {activeTab === "surveys" && (
          <div>
            <h2 className="font-krona text-white text-2xl mb-5">
              Your Surveys
            </h2>
            <div className="overflow-y-auto max-h-[300px]">
              {surveys.length > 0 ? (
                <ul className="text-white">
                  {surveys.map((survey) => (
                    <li key={survey.id} className="mb-3">
                      <strong>Survey ID:</strong> {survey.id} <br />
                      <strong>Mood:</strong> {survey.mood} <br />
                      <strong>Genres:</strong> {survey.genres.join(", ")} <br />
                      <strong>Occasion:</strong> {survey.occasion} <br />
                      <strong>Date:</strong>{" "}
                      {new Date(survey.createdAt).toLocaleString()}
                    </li>
                  ))}
                </ul>
              ) : (
                <p className="text-gray-400">You have no surveys yet.</p>
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default MyProfile;
