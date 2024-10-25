import React, { useState } from 'react';
import axios from 'axios';
import { TextField, Button, Typography } from '@mui/material';

const SongtextErstellung = () => {
    const [wort, setWort] = useState('');
    const [antwort, setAntwort] = useState('');

    const handleInputChange = (event) => {
        setWort(event.target.value);
    };

    const handleSubmit = async (event) => {
        event.preventDefault(); // Verhindert die Standard-Formularübertragung

        try {
            const response = await axios.post('http://localhost:8080/songtext', { wort });
            console.log('Antwort vom Server:', response.data);
            setAntwort(response.data); // Setze die Antwort vom Server in den Zustand
            setWort(''); // Eingabefeld nach dem Senden zurücksetzen
        } catch (error) {
            console.error('Fehler beim Senden:', error);
            setAntwort('Fehler beim Senden'); // Fehlerbehandlung
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
            {antwort && (
                <Typography variant="h6" style={{ marginTop: '20px' }}>
                    Antwort vom Server: {antwort}
                </Typography>
            )}
        </div>
    );
};

export default SongtextErstellung;
