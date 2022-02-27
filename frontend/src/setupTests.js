import "@testing-library/jest-dom";
import axios from "axios";

axios.defaults.adapter = require("axios/lib/adapters/http");

// See https://stackoverflow.com/questions/64813447/cannot-read-property-addlistener-of-undefined-react-testing-library
global.matchMedia =
  global.matchMedia ||
  function () {
    return {
      addListener: jest.fn(),
      removeListener: jest.fn()
    };
  };
