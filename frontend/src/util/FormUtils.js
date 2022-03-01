import { Form, Input } from "antd";

export const required = { rules: [{ required: true, message: "Required" }] };

export const HiddenField = (props) => (
  <Form.Item name={props.name} noStyle>
    <Input type="hidden" />
  </Form.Item>
);
