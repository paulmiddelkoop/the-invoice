import useSWR from "swr";
import { useParams } from "react-router-dom";
import { NavigationButton } from "../util/NavigationButton";
import { Delivery } from "./Delivery";
import { Col, Row } from "antd";
import { EditOutlined } from "@ant-design/icons";
import React from "react";
import { IncomeList } from "./Income";

export const Family = () => {
  const { id } = useParams();
  const { data } = useSWR(`/families/${id}`);

  return (
    <>
      <h1>Family {data.name}</h1>

      <NavigationButton to={`/families/${id}/edit`} icon={<EditOutlined />} tooltip="Edit" />

      <Row>
        <Header>Personal identity number</Header>
        <Value>{data.personalIdentityNumber}</Value>
      </Row>
      <Row>
        <Header>Email</Header>
        <Value>
          <a href={`mailto: ${data.email}`} target="_blank" rel="noreferrer">
            {data.email}{" "}
          </a>
        </Value>
      </Row>
      <Row>
        <Header>Delivery</Header>
        <Value>{Delivery[data.delivery].description}</Value>
      </Row>
      {data.address && (
        <Row>
          <Header>Address</Header>
          <Value>
            {data.address.address}, {data.address.zipCode} {data.address.city}
          </Value>
        </Row>
      )}
      <Row>
        <Header>Customer number</Header>
        <Value>{data.customerNumber}</Value>
      </Row>

      <IncomeList familyId={id} />
    </>
  );
};

const Header = (props) => {
  return (
    <Col span={6} className="label">
      {props.children}
    </Col>
  );
};

const Value = (props) => {
  return <Col span={18}>{props.children}</Col>;
};
