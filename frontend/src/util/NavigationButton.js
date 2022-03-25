import { useNavigate } from "react-router-dom";
import { Tooltip } from "antd";
import { TooltipButton } from "./TooltipButton";

export const NavigationButton = (props) => {
  const navigate = useNavigate();

  return (
    <Tooltip title={props.tooltip}>
      <TooltipButton
        type="primary"
        size="large"
        icon={props.icon}
        onClick={() => navigate(props.to)}
        tooltip={props.tooltip}
      >
        {props.label}
      </TooltipButton>
    </Tooltip>
  );
};
