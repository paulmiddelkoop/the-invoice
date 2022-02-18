import useSWR from "swr";
import {useParams} from "react-router-dom";

export const Family = () => {
    const { id } = useParams();
    const {data} = useSWR(`http://localhost:8080/api/v1/families/${id}`);

    return (
        <>
            <h1>Family {data.name}</h1>
        </>
    );
}