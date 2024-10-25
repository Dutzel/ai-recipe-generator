import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Home from './pages/Home';
import SongtextErstellung from './sontext-erstellung/tools/SongtextErstellung';

const App = () => {
  return (
      <Router>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/songtext-erstellung" element={<SongtextErstellung />} />
        </Routes>
      </Router>
  );
};

export default App;
