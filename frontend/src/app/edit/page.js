'use client'
import NavBar from "../NavBar/NavBar";
import Editor from "./Editor"
import React, {useRef, useState} from 'react';

export default function Edit() {
    const [range, setRange] = useState();
    const [lastChange, setLastChange] = useState();
    const quillRef = useRef();

    return (<>
    <NavBar title='Docs'/>
    <div className="bg-[#f1f3f4] flex justify-center p-4 min-h-screen">
        <div className="w-7/12 text-black h-full bg-white">
            <div id="toolbar" className='flex justify-center '>
                <button className="ql-bold"/>
                <button className="ql-italic"/>
            </div>
            <Editor
                ref={quillRef}
                defaultValue={""}
                onSelectionChange={setRange}
                onTextChange={setLastChange}
            />
            <div>
                <div>Current Range:</div>
                {range ? JSON.stringify(range) : 'Empty'}
            </div>
            <div>
                <div>Last Change:</div>
                {lastChange ? JSON.stringify(lastChange.ops) : 'Empty'}
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
