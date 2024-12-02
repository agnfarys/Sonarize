import React, { useState } from 'react';

function Playlists() {
  const [playlists, setPlaylists] = useState([]);
  const [newPlaylist, setNewPlaylist] = useState('');

  const handleAddPlaylist = () => {
    if (newPlaylist) {
      setPlaylists([...playlists, newPlaylist]);
      setNewPlaylist('');
    }
  };

  const handleDeletePlaylist = (index) => {
    const updatedPlaylists = playlists.filter((_, i) => i !== index);
    setPlaylists(updatedPlaylists);
  };

  return (
    <div className="playlists-container">
      <h2>My Playlists</h2>
      <div className="add-playlist">
        <input
          type="text"
          placeholder="New Playlist Name"
          value={newPlaylist}
          onChange={(e) => setNewPlaylist(e.target.value)}
        />
        <button onClick={handleAddPlaylist}>Add Playlist</button>
      </div>
      <ul>
        {playlists.map((playlist, index) => (
          <li key={index}>
            {playlist}
            <button onClick={() => handleDeletePlaylist(index)}>Delete</button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default Playlists;
