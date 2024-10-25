import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Home from './pages/Home';
import SongtextErstellungContainer from "./sontext-erstellung/tools/SongtextErstellungContainer";

const App = () => {
  return (
      <Router>
          <div style={{flex: 1}}>
              <Routes>
                  <Route path="/" element={<Home/>}/>
                  <Route path="/songtext-erstellung" element={<SongtextErstellungContainer/>}/>
              </Routes>
          </div>
      </Router>
);
};

export default App;
