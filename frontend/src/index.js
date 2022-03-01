import * as React from "react";
import * as ReactDOM from "react-dom";
import { App } from "./App";
import axios from "axios";

axios.defaults.baseURL = process.env.REACT_APP_API_ENDPOINT;

const container = document.getElementById("react-content");
ReactDOM.createRoot(container).render(<App />);
