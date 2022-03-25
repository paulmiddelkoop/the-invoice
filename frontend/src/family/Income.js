import { Alert, Avatar, Button, Checkbox, Collapse, DatePicker, Form, Input, List, Space } from "antd";
import React, { useState } from "react";
import { ConditionalFormItem } from "../util/ConditionalFormItem";
import useSWR, { mutate } from "swr";
import { v4 as uuid } from "uuid";
import { HiddenField, required } from "../util/FormUtils";
import moment from "moment";
import useAxios from "axios-hooks";
import Title from "antd/es/typography/Title";
import { AiOutlineDelete, AiOutlineEdit, AiOutlinePlus } from "react-icons/ai";
import { GiMoneyStack } from "react-icons/gi";
import { TooltipButton } from "../util/TooltipButton";

export const IncomeList = (props) => {
  const url = `/families/${props.familyId}/incomes`;
  const callApi = async (id, apiCall, axiosOptions) => {
    await apiCall({ url: `${url}/${id}`, ...axiosOptions });
    await mutate(url);
  };

  const { data } = useSWR(url);

  const [{ error: deleteError }, executeDelete] = useAxios({ method: "delete" }, { manual: true });
  const deleteIncome = async (id) => {
    await callApi(id, executeDelete);
  };

  const [editIncomeId, setEditIncomeId] = useState(null);

  const { Panel } = Collapse;

  return (
    <>
      <Title level={3}>Income</Title>
      <Collapse>
        <Panel key="income">
          <Space direction="vertical">
            {deleteError && <Alert message="Error while communicating with server!" type="error" />}
            {editIncomeId && !data.find((income) => income.id === editIncomeId) ? (
              <IncomeForm callApi={callApi} setEditIncomeId={setEditIncomeId} initialValues={{ id: editIncomeId }} />
            ) : (
              <TooltipButton
                onClick={() => setEditIncomeId(uuid())}
                type="primary"
                icon={<AiOutlinePlus />}
                tooltip="New income"
              />
            )}
          </Space>
          <List
            itemLayout="horizontal"
            dataSource={data}
            renderItem={(item) => (
              <List.Item>
                {editIncomeId === item.id ? (
                  <IncomeForm
                    callApi={callApi}
                    setEditIncomeId={setEditIncomeId}
                    initialValues={{ ...item, ...{ startsOn: moment(item.startsOn) } }}
                  />
                ) : (
                  <>
                    <List.Item.Meta
                      title={item.max ? "Max income" : formatter.format(item.amount)}
                      description={`Start: ${item.startsOn}`}
                      avatar={<Avatar src={<GiMoneyStack size="2em" color="grey" />} />}
                    />
                    {editIncomeId !== item.id && (
                      <Space>
                        <TooltipButton
                          onClick={() => setEditIncomeId(item.id)}
                          type="primary"
                          size="small"
                          icon={<AiOutlineEdit />}
                          tooltip="Edit income"
                        />
                        <TooltipButton
                          onClick={() => deleteIncome(item.id)}
                          type="danger"
                          size="small"
                          icon={<AiOutlineDelete />}
                          tooltip="Remove income"
                        />
                      </Space>
                    )}
                  </>
                )}
              </List.Item>
            )}
          />
        </Panel>
      </Collapse>
    </>
  );
};

const IncomeForm = (props) => {
  const [form] = Form.useForm();

  const [{ error: putError }, executePut] = useAxios({ method: "put" }, { manual: true });
  const onSubmit = async (values) => {
    await props.callApi(values.id, executePut, {
      data: { ...values, ...{ startsOn: values.startsOn.format("YYYY-MM-DD") } }
    });
    props.setEditIncomeId(null);
  };

  return (
    <Space direction="vertical">
      {putError && <Alert message="Error while communicating with server!" type="error" />}

      <Form
        form={form}
        onFinish={onSubmit}
        initialValues={props.initialValues}
        layout="inline"
        hideRequiredMark="true"
        colon="false"
      >
        <HiddenField name="id" />
        <Form.Item name="max" valuePropName="checked">
          <Checkbox>Max</Checkbox>
        </Form.Item>
        <ConditionalFormItem name="max" visible={(max) => !max}>
          <Form.Item name="amount" {...required}>
            <Input placeholder="Amount" />
          </Form.Item>
        </ConditionalFormItem>
        <Form.Item name="startsOn" {...required}>
          <DatePicker placeholder="Start date" />
        </Form.Item>
        <Space>
          <Button type="primary" htmlType="submit">
            Submit
          </Button>
          <Button onClick={() => props.setEditIncomeId(null)}>Cancel</Button>
        </Space>
      </Form>
    </Space>
  );
};

const formatter = new Intl.NumberFormat("se-SE", {
  style: "currency",
  currency: "SEK"
});
