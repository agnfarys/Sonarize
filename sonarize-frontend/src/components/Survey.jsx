import React, { useState, useEffect } from "react";
import arrow from "../assets/arrow.svg";

const userID = "f3090913-4366-413f-8b4e-de6594024501";

const defaultQuestions = [
  { question: "mood", answers: ["happy", "relaxed", "energetic", "sad"], key: "mood" },
  { question: "genres", answers: ["pop", "rock", "hip-hop", "jazz"], key: "genres" },
  { question: "energy", answers: ["calm", "moderate", "high", "extreme"], key: "energyLevel" },
  { question: "occasion", answers: ["workout", "party", "relaxation", "study"], key: "occasion" },
  { question: "preference", answers: ["new releases", "classics"], key: "discoveryPreference" },
  { question: "language", answers: ["english", "spanish", "polish", "german", "mixed"], key: "languagePreference" },
  { question: "length", answers: ["5", "10", "20", "50"], key: "playlistLength" },
];

const Survey = () => {
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [selectedAnswers, setSelectedAnswers] = useState(Array(defaultQuestions.length).fill(""));
  const [questions, setQuestions] = useState(defaultQuestions);
  const [favoriteArtists, setFavoriteArtists] = useState([]);
  const [inputText, setInputText] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/playlists/user/${userID}/summary`);
        if (!response.ok) throw new Error("Failed to fetch user data");

        const data = await response.json();

        const updatedQuestions = defaultQuestions.map((question) => {
          if (question.key === "genres") {
            return { ...question, answers: data.topGenres || question.answers };
          }
          return question;
        });

        setQuestions(updatedQuestions);
        setFavoriteArtists(data.topArtists || []);
      } catch (error) {
        console.error("Error fetching user data:", error);
      }
    };

    fetchUserData();
  }, []);

  const handleAnswerClick = (answer) => {
    const newSelectedAnswers = [...selectedAnswers];
    newSelectedAnswers[currentQuestionIndex] = answer;
    setSelectedAnswers(newSelectedAnswers);
    setInputText("");
  };

  const handleInputChange = (event) => {
    const value = event.target.value;
    setInputText(value);

    if (value) {
      const newSelectedAnswers = [...selectedAnswers];
      newSelectedAnswers[currentQuestionIndex] = value;
      setSelectedAnswers(newSelectedAnswers);
    }
  };

  const handleNextClick = async () => {
    if (currentQuestionIndex < questions.length - 1) {
      setCurrentQuestionIndex(currentQuestionIndex + 1);
      setInputText("");
    } else {
      setLoading(true);
      const surveyResult = questions.reduce((result, question, index) => {
        const answer = selectedAnswers[index];
        result[question.key] = question.key === "genres" && typeof answer === "string"
          ? [answer]
          : answer;
        return result;
      }, {});

      surveyResult.favoriteArtists = favoriteArtists;

      try {
        const response = await fetch(
          `http://localhost:8080/api/playlists/generate-chat-playlist?userId=${userID}`,
          {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(surveyResult),
          }
        );

        if (!response.ok) throw new Error("Failed to send POST request");
        const responseData = await response.json();
        console.log("POST response:", responseData);

        if (responseData.playlistUrl) {
          window.open(responseData.playlistUrl, "_blank");
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
    <div className="bg-landing w-full h-screen flex items-center justify-center">
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
                    selectedAnswer === answer && inputText === "" ? "selected" : ""
                  }`}
                  onClick={() => handleAnswerClick(answer)}
                >
                  {answer}
                </button>
              ))}
              <input
                type="text"
                value={inputText}
                onChange={handleInputChange}
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
