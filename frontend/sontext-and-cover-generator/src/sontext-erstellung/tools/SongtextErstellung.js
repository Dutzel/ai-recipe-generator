import React, { useState } from 'react';
import axios from 'axios';
import { TextField, Button, Typography } from '@mui/material';
import SongDetail from './SongDetail';
import './SongtextErstellung.css';

const SongtextErstellung = () => {
    const [wort, setWort] = useState('');
    const [antwort, setAntwort] = useState(null); // Ändere den Typ von antwort

    const handleInputChange = (event) => {
        setWort(event.target.value);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        try {
            const response = await axios.post('http://localhost:8080/songtext', { wort });
            console.log('Antwort vom Server:', response.data);
            setAntwort(response.data); // Setze die Antwort direkt auf die Daten
            setWort('');
        } catch (error) {
            console.error('Fehler beim Senden:', error);
            setAntwort(null); // Fehlerfall
        }
    };

    return (
        <div>
            <Typography variant="h4">Gib ein paar Wörter für deinen Song ein</Typography>
            <form onSubmit={handleSubmit}>
                <TextField
                    label="Gib ein Wort ein"
                    variant="outlined"
                    value={wort}
                    onChange={handleInputChange}
                    fullWidth
                    margin="normal"
                />
                <Button type="submit" variant="contained" color="primary">
                    Senden
                </Button>
            </form>
            <div className="details-area"> {/* Scrollbarer Bereich für die SongDetails */}
                {antwort && <SongDetail song={antwort}/>}
            </div>
        </div>
    );
};

export default SongtextErstellung;
