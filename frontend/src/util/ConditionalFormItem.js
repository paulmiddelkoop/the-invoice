import { Form } from "antd";
import React from "react";

export const ConditionalFormItem = (props) => {
  return (
    <Form.Item noStyle shouldUpdate={(oldValues, newValues) => oldValues[props.name] !== newValues[props.name]}>
      {({ getFieldValue }) => props.visible(getFieldValue(props.name)) && <>{props.children}</>}
    </Form.Item>
  );
};
