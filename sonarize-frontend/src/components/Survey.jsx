import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import arrow from "../assets/arrow.svg";

const Survey = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [userID, setUserID] = useState(sessionStorage.getItem("userId"));
  const [questions, setQuestions] = useState([
    {
      question: "mood",
      answers: ["happy", "calm", "energetic", "sad"],
      key: "mood",
    },
    {
      question: "genres",
      answers: ["pop", "rock", "hip-hop", "jazz"],
      key: "genres",
    },
    {
      question: "intensity",
      answers: ["low", "medium", "high", "very high"],
      key: "intensity",
    },
    {
      question: "occasion",
      answers: ["workout", "party", "relaxation", "study"],
      key: "occasion",
    },
    {
      question: "preference",
      answers: ["new releases", "classics"],
      key: "discoveryPreference",
    },
    {
      question: "language",
      answers: ["english", "spanish", "polish", "mixed"],
      key: "languagePreference",
    },
    {
      question: "length",
      answers: ["5", "10", "20", "50"],
      key: "playlistLength",
    },
  ]);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [selectedAnswers, setSelectedAnswers] = useState(
    Array(questions.length).fill("")
  );
  const [favoriteArtists, setFavoriteArtists] = useState([]);
  const [customInput, setCustomInput] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const params = new URLSearchParams(location.search);

    // Pobierz dane z parametrów URL
    const userId = params.get("userId") || sessionStorage.getItem("userId");
    const accessToken =
      params.get("accessToken") || sessionStorage.getItem("spotifyAccessToken");
    const username =
      params.get("username") || sessionStorage.getItem("username");

    // Zapisz dane do sessionStorage, jeśli nie są już zapisane
    if (userId && !sessionStorage.getItem("userId")) {
      sessionStorage.setItem("userId", userId);
      setUserID(userId);
    }
    if (accessToken && !sessionStorage.getItem("spotifyAccessToken")) {
      sessionStorage.setItem("spotifyAccessToken", accessToken);
    }
    if (username && !sessionStorage.getItem("username")) {
      sessionStorage.setItem("username", username);
    }

    if (!userId || !accessToken) {
      console.error("Missing user data. Redirecting to login.");
      navigate("/login");
    }
  }, [location.search, navigate]);

  const handleAnswerClick = (answer) => {
    const newSelectedAnswers = [...selectedAnswers];
    newSelectedAnswers[currentQuestionIndex] = answer;
    setSelectedAnswers(newSelectedAnswers);
    setCustomInput("");
  };

  const handleCustomInputChange = (event) => {
    const value = event.target.value;
    const currentQuestion = questions[currentQuestionIndex];
    if (currentQuestion.key === "length" && isNaN(value)) {
      return;
    }

    setCustomInput(value);
    const newSelectedAnswers = [...selectedAnswers];
    newSelectedAnswers[currentQuestionIndex] = value;
    setSelectedAnswers(newSelectedAnswers);
  };

  const handleNextClick = async () => {
    if (currentQuestionIndex < questions.length - 1) {
      setCurrentQuestionIndex(currentQuestionIndex + 1);
      setCustomInput("");
    } else {
      setLoading(true);

      const surveyResult = questions.reduce((result, question, index) => {
        const answer = selectedAnswers[index];
        result[question.key] =
          question.key === "genres" && typeof answer === "string"
            ? [answer]
            : answer;
        return result;
      }, {});

      surveyResult.favoriteArtists = favoriteArtists;

      try {
        const response = await fetch(
          `https://sonarize-chbte2bqe6e5a0gz.westeurope-01.azurewebsites.net/api/playlists/generate-chat-playlist?userId=${userID}`,
          {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(surveyResult),
          }
        );

        if (!response.ok) throw new Error("Failed to send POST request");

        const responseData = await response.json();
        if (responseData.playlistUrl) {
          navigate(
            `/playlist-result?playlistUrl=${encodeURIComponent(
              responseData.playlistUrl
            )}`
          );
        }
      } catch (error) {
        console.error("Error sending POST data:", error);
      } finally {
        setLoading(false);
      }
    }
  };

  const currentQuestion = questions[currentQuestionIndex];
  const selectedAnswer = selectedAnswers[currentQuestionIndex];

  return (
    <div className="bg-gradient w-full h-screen flex items-center justify-center">
      {loading ? (
        <div className="loader w-16 h-16 border-4 border-yellow-500 border-t-transparent rounded-full animate-spin"></div>
      ) : (
        <div className="tile backdrop-blur-sm p-8 rounded-xl w-[30rem] shadow-xl">
          <h1 className="font-krona uppercase text-white text-center text-2xl mb-5">
            Create Your Personalized Playlist
          </h1>
          <div className="flex justify-center items-center mb-2">
            <span className="font-krona text-white mx-6 text-[1.5rem]">
              {currentQuestionIndex + 1}/{questions.length}
            </span>
            <span className="font-krona text-secondary uppercase text-[1.5rem]">
              {currentQuestion.question}
            </span>
          </div>

          <div className="flex">
            <div className="flex-1 flex flex-col space-y-4">
              {currentQuestion.answers.map((answer) => (
                <button
                  key={answer}
                  type="button"
                  className={`submit-button w-full p-3 rounded-lg font-krona transition-ease ${
                    selectedAnswer === answer ? "selected" : ""
                  }`}
                  onClick={() => handleAnswerClick(answer)}
                >
                  {answer}
                </button>
              ))}
              <input
                type="text"
                value={customInput}
                onChange={handleCustomInputChange}
                placeholder="Type your custom answer here..."
                className="insert text-center font-raleway tracking-wide w-full mt-3 p-3 mb-6 rounded-lg text-white bg-gray-800 focus:outline-none"
              />
            </div>
            <div className="flex flex-col justify-end ml-5">
              <button
                type="button"
                className="arrow-button flex justify-center items-center bg-purple-600 p-4 rounded-full hover:bg-purple-500 transition"
                onClick={handleNextClick}
              >
                <img src={arrow} alt="Next" className="w-6 h-6" />
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Survey;
