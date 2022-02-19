import React, {useEffect, useState} from 'react';
import {useForm} from 'react-hook-form';
import {yupResolver} from '@hookform/resolvers/yup';
import {useNavigate, useParams} from "react-router-dom";

import axios from 'axios';
import useSWR from "swr";
import {Delivery, familySchema} from "./FamilySchema";
import {API_ENDPOINT} from "../App";

export const ReplaceFamily = () => {
    const {register, handleSubmit, reset, watch, formState: {errors}} = useForm({
        resolver: yupResolver(familySchema)
    });
    const existingFamily = useExistingFamilyInForm(reset);

    const navigate = useNavigate()
    const {putError, onSubmit} = useSubmit(navigate);

    const singleParent = watch("singleParent");
    const delivery = watch("delivery");

    return (<>
            <h1> Family {existingFamily?.name}</h1>

            {putError &&
                <div className="alert alert-danger" role="alert">
                    Error while communicating with server!
                </div>
            }

            <form noValidate onSubmit={handleSubmit(onSubmit)}>
                <fieldset>
                    <legend>Guardian 1</legend>
                    <div className="row">
                        <label className="form-label col-5">
                            First name
                            <input {...register("guardian1.firstName")} autoFocus className="form-control"/>
                            <div className="invalid-feedback">{errors.guardian1?.firstName?.message}</div>
                        </label>
                        <label className="form-label col-7">
                            Last name
                            <input {...register("guardian1.lastName")} className="form-control"/>
                            <div className="invalid-feedback">{errors.guardian1?.lastName?.message}</div>
                        </label>
                    </div>
                    <div className="form-check col-12 mb-3">
                        <label className="form-check-label">
                            Single parent
                            <input {...register("singleParent")} type="checkbox" className="form-check-input"/>
                        </label>
                    </div>
                </fieldset>

                {!singleParent &&
                    <fieldset>
                        <legend>Guardian 2</legend>
                        <div className="row">
                            <label className="form-label col-5">
                                First name
                                <input {...register("guardian2.firstName")} className="form-control"/>
                                <div className="invalid-feedback">{errors.guardian2?.firstName?.message}</div>
                            </label>
                            <label className="form-label col-7">
                                Last name
                                <input  {...register("guardian2.lastName")} className="form-control"/>
                                <div className="invalid-feedback">{errors.guardian2?.lastName?.message}</div>
                            </label>
                        </div>
                    </fieldset>
                }

                <fieldset>
                    <legend>Invoice</legend>
                    <label className="form-label col-12 mb-3">
                        Personal identity number
                        <input {...register("personalIdentityNumber")} className="form-control"/>
                        <div className="invalid-feedback">{errors.personalIdentityNumber?.message}</div>
                    </label>

                    <label className="form-label col-12 mb-3">
                        Email
                        <input {...register("email")} type="email" className="form-control"/>
                        <div className="invalid-feedback">{errors.email?.message}</div>
                    </label>

                    <div className="mb-3">
                        <label className="form-label col-12">Delivery</label>

                        {Object.keys(Delivery).map(delivery => (
                            <div key={delivery} className="form-check form-check-inline">
                                <label className="form-check-label">
                                    <input {...register("delivery")} type="radio" name="delivery" value={delivery}
                                           className="form-check-input"/>
                                    {Delivery[delivery].description}
                                </label>
                            </div>
                        ))}

                        <div className="invalid-feedback">{errors.delivery?.message}</div>
                    </div>

                    {delivery === "POST" &&
                        <>
                            <label className="form-label col-12 mb-3">
                                Address
                                <input {...register("address.address")} className="form-control"/>
                                <div className="invalid-feedback">{errors.address?.address?.message}</div>
                            </label>
                            <div className="row">
                                <label className="form-label col-3">
                                    Zip code
                                    <input {...register("address.zipCode")} className="form-control"/>
                                    <div className="invalid-feedback">{errors.address?.zipCode?.message}</div>
                                </label>
                                <label className="form-label col-9">
                                    City
                                    <input  {...register("address.city")} className="form-control"/>
                                    <div className="invalid-feedback">{errors.address?.city?.message}</div>
                                </label>
                            </div>
                        </>
                    }
                </fieldset>

                <button className="btn btn-primary" type="submit">Submit</button>
                <button onClick={() => navigate(-1)} className="btn btn-secondary ms-2">Cancel</button>
            </form>
        </>
    );
}

function useExistingFamilyInForm(reset) {
    const {id} = useParams();
    const {data} = useSWR(id ? `${API_ENDPOINT}/families/${id}` : null);
    useEffect(() => {
        if (data) {
            data.singleParent = data.guardian2 === null
            reset(data)
        }
    }, [reset, data]);
    return data;
}

function useSubmit(navigate) {
    const [putError, setPutError] = useState(false);
    const onSubmit = data => {
        axios
            .put(`${API_ENDPOINT}/families/${data.id}`, data, {
                transformRequest: [(data) => {
                    if (data.singleParent) delete data.guardian2
                    if (data.delivery !== "POST") delete data.address
                    delete data.singleParent
                    return data;
                }, ...axios.defaults.transformRequest]
            })
            .then(() => navigate(`/families/${data.id}`))
            .catch(() => setPutError(true));
    }
    return {putError, onSubmit};
}