import React, { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import axios from "axios";
import useSWR from "swr";
import { API_ENDPOINT } from "../App";
import { Alert, Button, Checkbox, Form, Input, Radio, Space } from "antd";
import Personnummer from "personnummer";
import { Delivery } from "./Delivery";
import { v4 as uuid } from "uuid";

export const ReplaceFamily = () => {
  const [form] = Form.useForm();
  const initialValues = useInitialValues();
  const navigate = useNavigate();
  const { putError, onSubmit } = useSubmit(initialValues, navigate);

  return (
    <>
      <h1> Family {initialValues?.name}</h1>

      {putError && <Alert message="Error while communicating with server!" type="error" />}

      <Form
        form={form}
        layout="vertical"
        onFinish={onSubmit}
        hideRequiredMark="true"
        colon="false"
        initialValues={initialValues}
      >
        <HiddenField name="id" />
        <HiddenField name="incomes" />

        <fieldset>
          <legend>Guardian 1</legend>
          <HiddenField name={["guardian1", "id"]} />
          <Form.Item noStyle>
            <Form.Item
              name={["guardian1", "firstName"]}
              label="First name"
              style={{ display: "inline-block", width: "40%" }}
              {...required}
            >
              <Input />
            </Form.Item>
            <Form.Item
              name={["guardian1", "lastName"]}
              label="Last name"
              style={{ display: "inline-block", width: "60%", paddingLeft: "12px" }}
              {...required}
            >
              <Input />
            </Form.Item>
          </Form.Item>
          <Form.Item name="singleParent" valuePropName="checked">
            <Checkbox>Single parent</Checkbox>
          </Form.Item>
        </fieldset>

        <Form.Item noStyle shouldUpdate={(oldValues, newValues) => oldValues.singleParent !== newValues.singleParent}>
          {({ getFieldValue }) =>
            !getFieldValue("singleParent") && (
              <fieldset>
                <legend>Guardian 2</legend>
                <HiddenField name={["guardian2", "id"]} />
                <Form.Item noStyle>
                  <Form.Item
                    name={["guardian2", "firstName"]}
                    label="First name"
                    style={{ display: "inline-block", width: "40%" }}
                    {...required}
                  >
                    <Input />
                  </Form.Item>
                  <Form.Item
                    name={["guardian2", "lastName"]}
                    label="Last name"
                    style={{ display: "inline-block", width: "60%", paddingLeft: "10px" }}
                    {...required}
                  >
                    <Input />
                  </Form.Item>
                </Form.Item>
              </fieldset>
            )
          }
        </Form.Item>

        <fieldset>
          <legend>Invoice</legend>

          <Form.Item
            name="personalIdentityNumber"
            label="Personal identity number"
            rules={[
              { required: true, message: "Required" },
              {
                validator: async (_, value) => {
                  if (value && !Personnummer.valid(value)) throw Error("Invalid number");
                }
              }
            ]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="email"
            label="Email"
            rules={[
              { required: true, message: "Required" },
              { type: "email", message: "Invalid email address" }
            ]}
          >
            <Input />
          </Form.Item>

          <Form.Item name="delivery" label="Delivery">
            <Radio.Group>
              {Object.keys(Delivery).map((delivery) => (
                <Radio key={delivery} value={delivery}>
                  {Delivery[delivery].description}
                </Radio>
              ))}
            </Radio.Group>
          </Form.Item>

          <Form.Item noStyle shouldUpdate={(oldValues, newValues) => oldValues.delivery !== newValues.delivery}>
            {({ getFieldValue }) =>
              getFieldValue("delivery") === "POST" && (
                <>
                  <Form.Item name={["address", "address"]} label="Address" {...required}>
                    <Input />
                  </Form.Item>

                  <Form.Item noStyle>
                    <Form.Item
                      name={["address", "zipCode"]}
                      label="Zipcode"
                      style={{ display: "inline-block", width: "30%" }}
                      {...required}
                    >
                      <Input />
                    </Form.Item>
                    <Form.Item
                      name={["address", "city"]}
                      label="City"
                      style={{ display: "inline-block", width: "70%", paddingLeft: "12px" }}
                      {...required}
                    >
                      <Input />
                    </Form.Item>
                  </Form.Item>
                </>
              )
            }
          </Form.Item>
        </fieldset>

        <Space>
          <Button type="primary" htmlType="submit">
            Submit
          </Button>
          <Button onClick={() => navigate(-1)}>Cancel</Button>
        </Space>
      </Form>
    </>
  );
};

export const HiddenField = (props) => (
  <Form.Item name={props.name} noStyle>
    <Input type="hidden" />
  </Form.Item>
);

const required = { rules: [{ required: true, message: "Required" }] };

function useInitialValues() {
  const { id } = useParams();
  const { data } = useSWR(id ? `${API_ENDPOINT}/families/${id}` : null);
  return (
    data ?? {
      id: uuid(),
      guardian1: { id: uuid() },
      guardian2: { id: uuid() },
      incomes: [] // TODO do we want to send incomes?
    }
  );
}

function useSubmit(existingFamily, navigate) {
  const [putError, setPutError] = useState(false);
  const onSubmit = (values) => {
    axios
      .put(`${API_ENDPOINT}/families/${values.id}`, values)
      .then(() => navigate(`/families/${values.id}`))
      .catch(() => setPutError(true));
  };
  return { putError, onSubmit };
}
