import * as React from "react";
import {Suspense} from "react";
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import "./App.css";
import {Families} from "./family/Families";
import {Family} from "./family/Family";
import {ReplaceFamily} from "./family/ReplaceFamily";
import ErrorBoundary from "./ErrorBoundary";
import {SWRConfig} from "swr";
import axios from "axios";

export const API_ENDPOINT = process.env.REACT_APP_API_ENDPOINT;

export function App() {
    return (
        <ErrorBoundary fallback={<div className="alert alert-danger" role="alert">Something went wrong!</div>}>
            <Suspense fallback={<div>Loading...</div>}>
                <SWRConfig value={{suspense: true, fetcher: url => axios.get(url).then(res => res.data)}}>
                    <BrowserRouter>
                        <Routes>
                            <Route path="/" element={<Navigate replace to="/families"/>}/>
                            <Route path="/families" element={<Families/>}/>
                            <Route path="/families/:id" element={<Family/>}/>
                            <Route path="/families/:id/edit" element={<ReplaceFamily/>}/>
                            <Route path="/families/create" element={<ReplaceFamily/>}/>
                        </Routes>
                    </BrowserRouter>
                </SWRConfig>
            </Suspense>
        </ErrorBoundary>
    );
}
