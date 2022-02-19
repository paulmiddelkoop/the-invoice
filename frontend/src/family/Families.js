import useSWR from "swr";
import {Delivery} from "./FamilySchema";
import {useNavigate} from "react-router-dom";
import {NavigationButton} from "./NavigationButton";
import {API_ENDPOINT} from "../App";

export const Families = () => {
    const {data} = useSWR(`${API_ENDPOINT}/families`);
    const navigate = useNavigate();

    return (
        <>
            <h1>Families</h1>

            <NavigationButton to="/families/create" className="mb-3" iconClassName="fa-solid fa-plus" label="New"/>

            <table className="table table-hover">
                <thead className="table-light">
                <tr>
                    <th scope="col">Name</th>
                    <th scope="col">Delivery</th>
                    <th scope="col">External reference</th>
                </tr>
                </thead>
                <tbody>
                {data.map(family => (
                    <tr onClick={() => navigate(`/families/${(family.id)}`)} key={family.id} >
                        <td>{family.name}</td>
                        <td>{Delivery[family.delivery].description}</td>
                        <td>{family.externalReference}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </>
    );
}