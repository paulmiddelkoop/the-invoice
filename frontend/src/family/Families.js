import useSWR from "swr";
import { Link } from "react-router-dom";

export const Families = () => {
    const {data} = useSWR("http://localhost:8080/api/v1/families");

    return (
        <>
            <h1>Families</h1>
            <ul>
                {data.map(family => (
                    <li key={family.id}>
                        <Link to={`/${family.id}`}>{family.name}</Link>
                    </li>
                ))}
            </ul>
        </>
    );
}