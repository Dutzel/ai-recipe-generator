import React, { useState } from 'react';
import axios from 'axios';
import { TextField, Button, Typography } from '@mui/material';
import SongDetail from './SongDetail';

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
            <Typography variant="h2">Songtext-Erstellung</Typography>
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
            {antwort && <SongDetail song={antwort} />} {/* Verwende die SongDetail-Komponente */}
        </div>
    );
};

export default SongtextErstellung;
