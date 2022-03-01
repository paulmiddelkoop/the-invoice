import { useNavigate } from "react-router-dom";
import { Button, Tooltip } from "antd";

export const NavigationButton = (props) => {
  const navigate = useNavigate();

  return (
    <Tooltip title={props.tooltip}>
      <Button type="primary" shape="circle" size="large" icon={props.icon} onClick={() => navigate(props.to)}>
        {props.label}
      </Button>
    </Tooltip>
  );
};
