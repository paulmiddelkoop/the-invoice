import '@testing-library/jest-dom';
import axios from "axios";

axios.defaults.adapter = require('axios/lib/adapters/http')