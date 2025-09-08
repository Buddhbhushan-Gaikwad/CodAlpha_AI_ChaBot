import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";   // <-- must point to App.js you modified
import "./index.css";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <React.StrictMode>
    <App/>
  </React.StrictMode>
);