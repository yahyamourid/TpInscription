import React from "react";
import Student from "./components/Student";
import Role from "./components/Role";
import Major from "./components/Major";
import Statistiques from "./components/Statistiques";
import {Routes, Route} from 'react-router-dom'
import Sidebar from "./components/Sidebar";

function App() {
  return (
    <>
    
    <Routes>
      <Route path="/" exact element={<Student/>}/>
      <Route path="/role" exact element={<Role/>}/>
      <Route path="/student" exact element={<Student/>}/>
      <Route path="/major" exact element={<Major/>}/>
      <Route path="/statistics" exact element={<Statistiques/>}/>
    </Routes>
    </>
    
  );
}

export default App;
