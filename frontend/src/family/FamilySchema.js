import {boolean, object, setLocale, string} from "yup";
import {v4 as uuid} from "uuid";
import Personnummer from "personnummer";

setLocale({
    mixed: {
        required: 'Is required',
    },
    string: {
        email: 'Must be a valid email'
    }
});

const requiredString = string().required();

const id = requiredString.uuid().default(uuid());

const guardianSchema = object({
    id: id,
    firstName: requiredString,
    lastName: requiredString
})

export const familySchema = object({
    id: id,
    singleParent: boolean(),
    guardian1: guardianSchema,
    guardian2: object().nullable().when("singleParent", {
        is: false,
        then: guardianSchema
    }),
    delivery: requiredString.nullable().oneOf(["E_INVOICE", "EMAIL", "POST"]),
    personalIdentityNumber: requiredString
        .test('is-person-number', 'Must be a valid Swedish person number', value => Personnummer.valid(value)),
    email: requiredString.email(),
    address: object().nullable().when("delivery", {
        is: "POST",
        then: object({
            address: requiredString,
            zipCode: requiredString,
            city: requiredString
        })
    }),
});