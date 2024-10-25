import React from 'react';
import { Typography, Box, List, ListItem } from '@mui/material';

const SongDetail = ({ song }) => {
    if (!song) {
        return <Typography variant="h6">Keine Songdaten verfügbar.</Typography>;
    }

    const { name, description, words, instructions, imageUrl } = song;

    return (
        <Box sx={{ padding: 2, border: '1px solid #ccc', borderRadius: '8px', marginTop: '20px' }}>
            <Typography variant="h4" gutterBottom>
                {name}
            </Typography>
            <Typography variant="h6" color="textSecondary" gutterBottom>
                {description}
            </Typography>

            <Typography variant="h5" gutterBottom>
                Words:
            </Typography>
            <List>
                {words && words.length > 0 ? (
                    words.map((word, index) => (
                        <ListItem key={index}>{word}</ListItem>
                    ))
                ) : (
                    <ListItem>Keine Wörter verfügbar.</ListItem>
                )}
            </List>

            <Typography variant="h5" gutterBottom>
                Instructions:
            </Typography>
            <List>
                {instructions && instructions.length > 0 ? (
                    instructions.map((instruction, index) => (
                        <ListItem key={index}>{instruction}</ListItem>
                    ))
                ) : (
                    <ListItem>Keine Anweisungen verfügbar.</ListItem>
                )}
            </List>

            {imageUrl && imageUrl.trim() !== "" && (
                <img src={imageUrl} alt={name} style={{ width: '100%', marginTop: '20px' }} />
            )}
        </Box>
    );
};

export default SongDetail;
