// import { StrictMode } from "react";
// import { createRoot } from "react-dom/client";
// import "./index.css";
// import App from "./App.tsx";
//
// import axios from "axios";
//
// //WICHTIG: Session-Cookies erlauben
// axios.defaults.withCredentials = true;
//
// //Backend-Base-URL (lokal vs. prod)
// axios.defaults.baseURL =
//     window.location.host === "localhost:5173"
//         ? "http://localhost:8080"
//         : window.location.origin;
//
// createRoot(document.getElementById("root")!).render(
//     <StrictMode>
//         <App />
//     </StrictMode>
// );


import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
