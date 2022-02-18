import * as React from "react";
import {Route, Routes} from "react-router-dom";
import "./App.css";
import {Families} from "./family/Families";
import {Family} from "./family/Family";
import {ReplaceFamily} from "./family/ReplaceFamily";

export function App() {
    return (
        <div className="App">
            <Routes>
                <Route path="/" element={<Families/>}/>
                <Route path="/:id" element={<Family/>}/>
                <Route path="/:id/edit" element={<ReplaceFamily/>}/>
                <Route path="/create" element={<ReplaceFamily/>}/>
            </Routes>
        </div>
    );
}
