import React from 'react';
import { Link } from 'react-router-dom';
import { Box, Typography, Button } from '@mui/material';

const Home = () => {
    return (
        <Box sx={{ textAlign: 'center', padding: '40px', backgroundColor: '#f5f5f5' }}>
            <Typography variant="h2" gutterBottom>
                Willkommen auf der Homepage!
            </Typography>
            <Typography variant="h5" color="textSecondary" paragraph>
                Erstelle deinen eigenen Songtext und lass deiner Kreativität freien Lauf.
            </Typography>
            <Link to="/songtext-erstellung" style={{ textDecoration: 'none' }}>
                <Button variant="contained" color="primary">
                    Songtext-Erstellung starten
                </Button>
            </Link>
        </Box>
    );
};

export default Home;
