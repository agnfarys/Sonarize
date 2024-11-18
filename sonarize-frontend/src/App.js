import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './Login';
import Register from './Register';
import Playlists from './Playlists';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <h1>Sonarize</h1>
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/playlists" element={<Playlists />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
