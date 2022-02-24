import {screen} from '@testing-library/react';

import {mockApi, render} from "./../testUtils"
import {Families} from "./Families";
import * as React from "react";

describe('render', () => {
    describe('given families exist', () => {
        it('should show them', async () => {
            mockApi()
                .get("/families")
                .replyWithFile(200, __dirname + "/families.test.json");

            await render(<Families/>)

            const rows = screen.getAllByRole("row");
            expect(rows.length).toBe(3);
            expect(rows[1]).toHaveTextContent(/(John & Jane Doe)(Post)(1)/);
            expect(rows[2]).toHaveTextContent(/(Paul Middelkoop)(E-invoice)(2)/);
        });
    });

    describe('given no families exist', () => {
        it('shows that no families are found', async () => {
            mockApi()
                .get("/families")
                .reply(200, {});

            await render(<Families/>)

            expect(screen.queryByText("No families found.")).toBeVisible();
            expect(screen.queryByRole("table")).not.toBeInTheDocument();
        });
    });
});
