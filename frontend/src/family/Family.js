import useSWR from "swr";
import { useParams } from "react-router-dom";
import { NavigationButton } from "../util/NavigationButton";
import { Delivery } from "./Delivery";
import { Col, Row, Space } from "antd";
import React from "react";
import { IncomeList } from "./Income";
import { ChildList } from "./ChildList";
import Title from "antd/es/typography/Title";
import { AiOutlineEdit } from "react-icons/ai";

export const Family = () => {
  const { id } = useParams();
  const { data } = useSWR(`/families/${id}`);

  return (
    <>
      <Title level={2}>Family {data.name}</Title>

      <Space direction="vertical" size="middle">
        <NavigationButton to={`/families/${id}/edit`} icon={<AiOutlineEdit />} tooltip="Edit" />

        <div>
          <Title level={3}>Invoice</Title>
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
        </div>

        <ChildList familyId={id} />

        <IncomeList familyId={id} />
      </Space>
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
