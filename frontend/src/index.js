import * as React from "react";
import {Suspense} from "react";
import * as ReactDOM from "react-dom";
import {BrowserRouter} from "react-router-dom";
import {App} from "./App";
import ErrorBoundary from "./ErrorBoundary";
import axios from "axios";
import {SWRConfig} from 'swr'

axios.defaults.headers.post['Content-Type'] = 'application/json';

const container = document.getElementById('app');
const root = ReactDOM.createRoot(container);
root.render(
    <ErrorBoundary fallback={<h2>Could not load data.</h2>}>
    <Suspense fallback={<div>loading...</div>}>
        <BrowserRouter>
            <SWRConfig value={{suspense: true ,fetcher: url => axios.get(url).then(res => res.data)}}>
            <App />
            </SWRConfig>
        </BrowserRouter>
    </Suspense>
    </ErrorBoundary>
);