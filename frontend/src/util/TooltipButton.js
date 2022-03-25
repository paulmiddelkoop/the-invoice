import { Button, Tooltip } from "antd";

export const TooltipButton = (props) => (
  <Tooltip title={props.tooltip}>
    <Button shape="circle" {...props}>
      {props.label}
    </Button>
  </Tooltip>
);
