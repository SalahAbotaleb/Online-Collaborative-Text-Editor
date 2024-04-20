import NavBar from "../../components/NavBar/NavBar";
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import {useState, useEffect} from 'react';
import './editor.css'
import {useParams} from "react-router-dom";
import {useLocation} from "react-router-dom";

export default function Edit() {
    const [value, setValue] = useState();
    const [range, setRange] = useState();
    const [lastChange, setLastChange] = useState();
    const {id} = useParams();
    const {state} = useLocation();

    useEffect(() => {
        console.log(id);
        console.log(state);
    }, []);

    return (<>
    <NavBar title={state}/>
    <div className="bg-[#f1f3f4] flex justify-center p-4 min-h-screen">
        <div className="w-10/12 lg:w-8/12 text-black bg-white">
            <div id="toolbar" className='flex justify-center '>
                <button className="ql-bold"/>
                <button className="ql-italic"/>
            </div>
            <ReactQuill
                // theme="snow"
                value={value}
                // onSelectionChange={setRange}
                onChange={(value, delta, source, editor) => {
                    setValue(editor.getContents());
                    setLastChange(delta.ops);
                }}
                onChangeSelection={setRange}
                modules={
                    {
                    toolbar: ['bold', 'italic']
                }
                }
            />
            <div>
                <div>Current value:</div>
                {value? JSON.stringify(value.ops) : 'Empty'}
            </div>
            <div>
                <div>Current Range:</div>
                {range ? JSON.stringify(range) : 'Empty'}
            </div>
            <div>
                <div>Last Change:</div>
                {lastChange ? JSON.stringify(lastChange) : 'Empty'}
            </div>
        </div>
    </div>
    </>)
        ;
}
