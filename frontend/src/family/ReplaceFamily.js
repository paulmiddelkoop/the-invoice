import React, {useEffect, useState} from 'react';
import {useForm} from 'react-hook-form';
import {yupResolver} from '@hookform/resolvers/yup';
import {useNavigate, useParams} from "react-router-dom";

import axios from 'axios';
import useSWR from "swr";
import {familySchema} from "./FamilySchema";

export const ReplaceFamily = () => {
    const {register, handleSubmit, reset, watch, formState: {errors}} = useForm({
        mode: "onBlur",
        resolver: yupResolver(familySchema)
    });
    useStoredFamilyInForm(reset);

    const {putError, onSubmit} = useSubmit();

    const singleParent = watch("singleParent");
    const delivery = watch("delivery");

    return (
        <form noValidate onSubmit={handleSubmit(onSubmit)}>
            <fieldset>
                <legend>Guardian 1</legend>
                <div className="row">
                    <label className="form-label col-5">
                        First name
                        <input {...register("guardian1.firstName")} className="form-control"/>
                        <div className="invalid-feedback">{errors.guardian1?.firstName?.message}</div>
                    </label>
                    <label className="form-label col-7">
                        Last name
                        <input {...register("guardian1.lastName")} className="form-control"/>
                        <div className="invalid-feedback">{errors.guardian1?.lastName?.message}</div>
                    </label>
                </div>
            </fieldset>

            <fieldset>
                <legend>Guardian 2</legend>
                <div className="form-check col-12 mb-3">
                    <label className="form-check-label">
                        Single parent
                        <input {...register("singleParent")} type="checkbox" className="form-check-input"/>
                    </label>
                </div>
                {!singleParent &&
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
                    </div>}
            </fieldset>

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
                    <div className="form-check form-check-inline">
                        <label className="form-check-label">
                            <input {...register("delivery")} type="radio" name="delivery" value="E_INVOICE"
                                   className="form-check-input"/>
                            E-invoice
                        </label>
                    </div>
                    <div className="form-check form-check-inline">
                        <label className="form-check-label">
                            <input {...register("delivery")} type="radio" name="delivery" value="EMAIL"
                                   className="form-check-input"/>
                            Email
                        </label>
                    </div>
                    <div className="form-check form-check-inline">
                        <label className="form-check-label">
                            <input {...register("delivery")} type="radio" name="delivery" value="POST"
                                   className="form-check-input"/>
                            Post
                        </label>
                    </div>
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
                    </>}
            </fieldset>

            {putError && <span>Error while communicating with server!</span>}
            <button className="btn btn-primary" type="submit">Submit</button>
        </form>
    );
}

function useStoredFamilyInForm(reset) {
    const {id} = useParams();
    const {data} = useSWR(id ? `http://localhost:8080/api/v1/families/${id}` : null);
    useEffect(() => {
        if (data) {
            data.singleParent = data.guardian2 === null
            reset(data)
        }
    }, [reset, data]);
}

function useSubmit() {
    const [putError, setPutError] = useState(false);
    const navigate = useNavigate()
    const onSubmit = data => {
        axios
            .put(`http://localhost:8080/api/v1/families/${data.id}`, data, {
                transformRequest: [(data) => {
                    if (data.singleParent) delete data.guardian2
                    if (data.delivery !== "POST") delete data.address
                    delete data.singleParent
                    return data;
                }, ...axios.defaults.transformRequest]
            })
            .then(response => navigate(`/${data.id}`, {replace: true}))
            .catch(() => setPutError(true));
    }
    return {putError, onSubmit};
}