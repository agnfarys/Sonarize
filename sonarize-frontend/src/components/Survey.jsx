import React, { useState } from "react";
import arrow from "../assets/arrow.svg";

const questions = [
  {
    question: "userID",
    answers: [""],
    key: "userId",
  },
  {
    question: "mood",
    answers: ["happy", "relaxed", "energetic", "sad"],
    key: "mood",
  },
  {
    question: "genres",
    answers: ["pop", "rock", "hip-hop", "jazz"],
    key: "genres",
  },
  {
    question: "energy",
    answers: ["calm", "moderate", "high", "extreme"],
    key: "energyLevel",
  },
  {
    question: "occasion",
    answers: ["workout", "party", "relaxation", "study"],
    key: "occasion",
  },
  {
    question: "favoriteArtists",
    answers: ["pitbull", "malik montana", "ekipa", "fagata"],
    key: "favoriteArtists",
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
];

const Survey = () => {
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [selectedAnswers, setSelectedAnswers] = useState(
    Array(questions.length).fill("")
  );

  const handleAnswerClick = (answer) => {
    const newSelectedAnswers = [...selectedAnswers];
    newSelectedAnswers[currentQuestionIndex] = answer;
    setSelectedAnswers(newSelectedAnswers);
    console.log(
      `Selected answer for question ${currentQuestionIndex + 1}: ${answer}`
    );
  };

  const handleNextClick = async () => {
    if (currentQuestionIndex < questions.length - 1) {
      setCurrentQuestionIndex(currentQuestionIndex + 1);
    } else {
      // Handle survey completion
      const surveyResult = questions.reduce((result, question, index) => {
        result[question.key] =
          question.key === "genres" || question.key === "favoriteArtists"
            ? [selectedAnswers[index]]
            : selectedAnswers[index];
        return result;
      }, {});
      console.log("Survey completed. Selected answers:", surveyResult);

      // Transmit the JSON object to the backend
      try {
        const response = await fetch(
          "http://localhost:8080/api/playlists/generate-chat-playlist?userId=",
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(surveyResult),
          }
        );

        if (!response.ok) {
          throw new Error("Network response was not ok");
        }

        const responseData = await response.json();
        console.log("Response from backend:", responseData);
      } catch (error) {
        console.error("Error transmitting survey results:", error);
      }
    }
  };

  const currentQuestion = questions[currentQuestionIndex];
  const selectedAnswer = selectedAnswers[currentQuestionIndex];

  return (
    <div className="bg-landing w-full h-screen">
      <div className="flex items-center justify-center min-h-screen flex-col">
        <h1 className="font-krona uppercase text-white text-center text-2xl mb-5">
          Create Your Personalized Playlist
        </h1>
        <div className="tile backdrop-blur-sm p-8 rounded-xl w-[30rem] h-[33rem] shadow-xl">
          <div className="flex justify-center items-center mb-2">
            <span className="font-krona text-white mx-6 text-[1.5rem]">
              {currentQuestionIndex + 1}/{questions.length}
            </span>
            <span className="font-krona text-secondary uppercase text-[1.5rem]">
              {currentQuestion.question}
            </span>
          </div>
          <div className="flex">
            <div className="flex-1 flex flex-col space-y-6">
              {currentQuestion.answers.map((answer) => (
                <button
                  key={answer}
                  type="button"
                  className={`submit-button w-full p-3 rounded-lg font-krona transition-ease ${
                    selectedAnswer === answer ? "bg-secondary text-white" : ""
                  }`}
                  onClick={() => handleAnswerClick(answer)}
                >
                  {answer}
                </button>
              ))}
              <input
                type="text"
                placeholder="I'm feeling..."
                className="insert text-center font-raleway tracking-wide w-full mt-3 p-3 mb-6 rounded-lg text-white placeholder:text-white/50 focus:outline-none focus:border-sonarizeAccent"
              />
            </div>
            <div className="flex flex-col justify-end ml-5">
              <button
                type="button"
                className="arrow-button flex justify-center items-center"
                onClick={handleNextClick}
              >
                <img src={arrow} alt="Next" className="w-6 h-6" />
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Survey;
