import {useNavigate} from "react-router-dom";

export const NavigationButton = (props) => {
    const navigate = useNavigate()

    return (
        <button type="button" onClick={() => navigate(props.to)} className={`btn btn-primary ${props.className}`}>
            <i className={`${props.iconClassName} fa-l`}/> {props.label}
        </button>
    )
}