import {useId} from "react";

export default function InputField({label, value, setValue, type}) {
    const id = useId();
    return (
        <div className="relative">
            <input id={id} type={type} value={value}
                   onChange={(e) => setValue(e.target.value)}
                   className="peer block text-black border-gray-500 border-2 w-full px-4 py-4 rounded-lg shadow-md focus:outline-none focus:border-blue-600 focus:border-opacity-50 mt-4"
                   placeholder=' '/>
            <label htmlFor={id}
                   className="absolute text-[17px] text-opacity-80 peer-focus:text-opacity-80 text-sm text-gray-500 duration-300 transform -translate-y-4 scale-75 top-2 z-10 origin-[0] px-4 peer-focus:px-4 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:-translate-y-1/2 peer-placeholder-shown:top-7 cursor-text peer-focus:top-2 peer-focus:scale-75 peer-focus:-translate-y-4 bg-white rounded-sm start-1">
                {label}
            </label>
        </div>
    )
}
