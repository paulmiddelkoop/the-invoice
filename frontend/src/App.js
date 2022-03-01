import * as React from "react";
import { Suspense } from "react";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import "./App.css";
import { Families } from "./family/Families";
import { Family } from "./family/Family";
import { ReplaceFamily } from "./family/ReplaceFamily";
import ErrorBoundary from "./ErrorBoundary";
import { SWRConfig } from "swr";
import axios from "axios";
import { Layout, Result } from "antd";

const { Header, Footer } = Layout;

export function App() {
  return (
    <ErrorBoundary fallback={<Result status="error" title="Something went wrong!" />}>
      <Suspense fallback={<div>Loading...</div>}>
        <SWRConfig value={{ suspense: true, fetcher: (url) => axios.get(url).then((res) => res.data) }}>
          <Header className="header">
            <h1>
              <a href="/">
                <img src={process.env.PUBLIC_URL + "/logo.png"} alt="The Invoice" />
                The Invoice
              </a>
            </h1>
          </Header>
          <section className="content">
            <BrowserRouter>
              <Routes>
                <Route path="/" element={<Navigate replace to="/families" />} />
                <Route path="/families" element={<Families />} />
                <Route path="/families/:id" element={<Family />} />
                <Route path="/families/:id/edit" element={<ReplaceFamily />} />
                <Route path="/families/create" element={<ReplaceFamily />} />
              </Routes>
            </BrowserRouter>
          </section>
          <Footer className="footer">©2022 Paul Middelkoop</Footer>
        </SWRConfig>
      </Suspense>
    </ErrorBoundary>
  );
}
