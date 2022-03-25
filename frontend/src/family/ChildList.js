import useSWR from "swr";
import { Avatar, List, Space } from "antd";
import Title from "antd/es/typography/Title";
import { FaChild } from "react-icons/fa";
import { AiOutlineDelete, AiOutlineEdit, AiOutlinePlus } from "react-icons/ai";
import { TooltipButton } from "../util/TooltipButton";

export const ChildList = (props) => {
  const url = `/families/${props.familyId}/children`;
  const { data } = useSWR(url);

  return (
    <>
      <Title level={3}>Children</Title>
      <TooltipButton type="primary" icon={<AiOutlinePlus />} tooltip="Add child" />
      <List
        itemLayout="horizontal"
        dataSource={data}
        renderItem={(item) => (
          <List.Item>
            <>
              <List.Item.Meta
                title={item.name}
                description={`Preschool`}
                avatar={<Avatar src={<FaChild size="2em" color="grey" />} />}
              />
              <Space>
                <TooltipButton type="primary" size="small" icon={<AiOutlineEdit />} tooltip="Edit child" />
                <TooltipButton type="danger" size="small" icon={<AiOutlineDelete />} tooltip="Remove child" />
              </Space>
            </>
          </List.Item>
        )}
      />
    </>
  );
};
