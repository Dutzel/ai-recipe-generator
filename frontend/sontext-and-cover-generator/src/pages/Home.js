import React from 'react';
import { Link } from 'react-router-dom';

const Home = () => {
  return (
      <div>
        <h1>Willkommen auf der Homepage!</h1>
        <Link to="/songtext-erstellung">Songtext-Erstellung</Link>
      </div>
  );
};

export default Home;
