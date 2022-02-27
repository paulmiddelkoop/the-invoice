import React, { Suspense } from "react";
import { SWRConfig, useSWRConfig } from "swr";
import axios from "axios";
import { MemoryRouter } from "react-router-dom";
import { render, screen, waitForElementToBeRemoved } from "@testing-library/react";
import nock from "nock";

const AllTheProviders = ({ children }) => {
  return (
    <Suspense fallback={<div>Loading...</div>}>
      <SWRConfig
        value={{ suspense: true, fetcher: (url) => axios.get(url).then((res) => res.data), dedupingInterval: 0 }}
      >
        <MemoryRouter>
          {children}
          <ClearSwrCache />
        </MemoryRouter>
      </SWRConfig>
    </Suspense>
  );
};

// Needed because SWR cache is not cleared between test with when using the recommended approach of a provider.
const ClearSwrCache = () => {
  const { cache } = useSWRConfig();
  cache.clear();
  return <></>;
};

const customRender = async (ui, options) => {
  render(ui, { wrapper: AllTheProviders, ...options });
  await waitForElementToBeRemoved(() => screen.queryByText("Loading..."));
};

export const mockApi = () => nock("http://the-invoice-test/api/v1");

export * from "@testing-library/react";
export { customRender as render };
