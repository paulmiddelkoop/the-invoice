import useSWR from "swr";
import {useParams} from "react-router-dom";
import {NavigationButton} from "../NavigationButton";
import {API_ENDPOINT} from "../App";
import {Delivery} from "./FamilySchema";

export const Family = () => {
    const {id} = useParams();
    const {data} = useSWR(`${API_ENDPOINT}/families/${id}`);

    return (
        <>
            <h1>Family {data.name}</h1>

            <NavigationButton to={`/families/${id}/edit`} className="mb-3" iconClassName="fas fa-edit" label="Edit"/>

            <dl className="row">
                <dt className="col-sm-3">Personal identity number</dt>
                <dd className="col-sm-9"><a href={`https://mrkoll.se/resultat?n=${data.personalIdentityNumber}`}
                                            target="_blank" rel="noreferrer">{data.personalIdentityNumber}</a></dd>

                <dt className="col-sm-3">Email</dt>
                <dd className="col-sm-9"><a href={`mailto: ${data.email}`} target="_blank"
                                            rel="noreferrer">{data.email} </a></dd>

                <dt className="col-sm-3">Delivery</dt>
                <dd className="col-sm-9">{Delivery[data.delivery].description}</dd>

                <dt className="col-sm-3">Customer number</dt>
                <dd className="col-sm-9">{data.customerNumber}</dd>

                {data.address &&
                    <>
                        <dt className="col-sm-3">Address</dt>
                        <dd className="col-sm-9">{data.address.address}, {data.address.zipCode} {data.address.city}</dd>
                    </>
                }
            </dl>
        </>
    );
}