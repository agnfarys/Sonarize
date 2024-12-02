import React from 'react';
import { Link } from 'react-router-dom';
import './Landing.css';

function Landing() {
  return (
    <div className="landing">
      {/* Nawigacja */}
      <nav className="landing-nav">
        <Link to="/login" className="log-in">Log In</Link>
        <Link to="/register" className="sign-up">Sign Up</Link>
      </nav>

      {/* Gradientowe elipsy */}
      <div className="ellipse-1"></div>
      <div className="ellipse-2"></div>
      <div className="ellipse-3"></div>
      <div className="ellipse-4"></div>
      <div className="ellipse-5"></div>

      {/* Treść strony */}
      <div className="landing-content">
        <h1 className="sonarize">SONARIZE</h1>
        <p className="discover-music-like-never-before">
          DISCOVER MUSIC LIKE NEVER BEFORE
        </p>
        <p className="join-us-and-let-your-journey-to-perfect-playlists-begin">
          Join us and let your journey to perfect playlists begin!
        </p>
        <div className="rectangle-1">
          <span className="dive-into-your-soundtrack">DIVE INTO YOUR SOUNDTRACK</span>
        </div>
      </div>
    </div>
  );
}

export default Landing;
