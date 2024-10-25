import React from 'react';
import { Box, Typography } from '@mui/material';
import SongtextErstellung from './SongtextErstellung';
import './SongtextErstellungContainer.css';

const SongtextErstellungContainer = () => {
    return (
        <div className="container">
            <Typography className="header"gutterBottom>
                Songtext-Erstellung
            </Typography>
            <Typography className="description" paragraph>
                In diesem Abschnitt kannst du deinen eigenen Songtext erstellen.
                Gib ein Wort ein und klicke auf „Senden“, um kreative Texte zu generieren.
                Lass deiner Fantasie freien Lauf!
            </Typography>
            <div className="songtext-area">
                <SongtextErstellung/>
            </div>
        </div>
    );
};

export default SongtextErstellungContainer;
