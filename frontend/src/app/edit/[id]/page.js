'use client'
import NavBar from "../../NavBar/NavBar";
import dynamic from 'next/dynamic';
import 'react-quill/dist/quill.snow.css';
import React, {useState, useEffect} from 'react';
import './editor.css'
import {useRouter} from 'next/navigation';

const ReactQuill = dynamic(() => import('react-quill'), {ssr: false});

export default function Edit({params}) {
    const [value, setValue] = useState();
    const [range, setRange] = useState();
    const [lastChange, setLastChange] = useState();
    const router = useRouter();


    useEffect(() => {
        console.log(params.id);
    }, []);

    return (<>
    <NavBar title='Docs'/>
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

        {/*<DocumnetView/>*/
        }
        {/*<NavBar title='document2'/>*/
        }
    </>)
        ;
}
