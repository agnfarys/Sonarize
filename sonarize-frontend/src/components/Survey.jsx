import React, { useState } from "react";
import arrow from "../assets/arrow.svg";

const questions = [
  {
    question: "mood",
    answers: ["happy", "relaxed", "energetic", "sad"],
  },
  {
    question: "genres",
    answers: ["pop", "rock", "hip-hop", "jazz"],
  },
  {
    question: "energy",
    answers: ["calm", "moderate", "high", "extreme"],
  },
  {
    question: "occcasion",
    answers: ["workout", "party", "relaxation", "study"],
  },
  {
    question: "language",
    answers: ["english", "spanish", "polish", "miesiana"],
  },
  {
    question: "length",
    answers: ["short", "medium", "long", "very long"],
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

  const handleNextClick = () => {
    if (currentQuestionIndex < questions.length - 1) {
      setCurrentQuestionIndex(currentQuestionIndex + 1);
    } else {
      // Handle survey completion
      console.log("Survey completed. Selected answers:", selectedAnswers);
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
