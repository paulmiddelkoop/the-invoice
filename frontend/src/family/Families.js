import useSWR from "swr";
import { Delivery } from "./Delivery";
import { useNavigate } from "react-router-dom";
import { NavigationButton } from "../util/NavigationButton";
import React from "react";
import { Alert, Space, Table } from "antd";
import Title from "antd/es/typography/Title";
import { AiOutlinePlus } from "react-icons/ai";

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
      <Title level={2}>Families</Title>

      <Space direction="vertical" size="middle">
        <NavigationButton to="/families/create" tooltip="New family" icon={<AiOutlinePlus />} />

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
      </Space>
    </>
  );
};
