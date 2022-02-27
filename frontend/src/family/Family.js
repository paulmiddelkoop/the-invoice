import useSWR from "swr";
import { useParams } from "react-router-dom";
import { NavigationButton } from "../NavigationButton";
import { API_ENDPOINT } from "../App";
import { Delivery } from "./Delivery";
import { Button, Col, Collapse, List, Row, Tooltip } from "antd";
import { EditOutlined, PlusOutlined } from "@ant-design/icons";

export const Family = () => {
  const formatter = new Intl.NumberFormat("se-SE", {
    style: "currency",
    currency: "SEK"
  });

  const { id } = useParams();
  const { data } = useSWR(`${API_ENDPOINT}/families/${id}`);
  const { Panel } = Collapse;

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

      <Collapse>
        <Panel header="Income" key="income">
          <Button type="primary" shape="circle" icon={<PlusOutlined />} />
          <List
            itemLayout="horizontal"
            dataSource={data.incomes}
            renderItem={(item) => (
              <List.Item>
                <List.Item.Meta
                  title={item.max ? "Max income" : formatter.format(item.amount)}
                  description={`Since ${item.changedOn}`}
                />
                <Tooltip title="Edit">
                  <Button type="primary" shape="circle" icon={<EditOutlined />} />
                </Tooltip>
              </List.Item>
            )}
          />
        </Panel>
      </Collapse>
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
