import useSWR from "swr";
import { Delivery } from "./Delivery";
import { useNavigate } from "react-router-dom";
import { NavigationButton } from "../util/NavigationButton";
import React from "react";
import { Alert, Table } from "antd";
import { PlusOutlined } from "@ant-design/icons";

export const Families = () => {
  const { data } = useSWR("/families");
  const navigate = useNavigate();
  const columns = [
    { title: "Name", dataIndex: "name", key: "name" },
    { title: "Delivery", dataIndex: "delivery", key: "delivery", render: (text) => Delivery[text].description },
    { title: "Customer number", dataIndex: "customerNumber", key: "customerNumber" }
  ];

  return (
    <>
      <h1>Families</h1>

      <NavigationButton to="/families/create" tooltip="New family" icon={<PlusOutlined />} />

      {!data.length ? (
        <Alert message="No families found." type="error" />
      ) : (
        <Table
          columns={columns}
          dataSource={data}
          rowKey={"id"}
          onRow={(row) => ({
            onClick: () => navigate(`/families/${row.id}`)
          })}
        />
      )}
    </>
  );
};
