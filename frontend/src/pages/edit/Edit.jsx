import NavBar from "../../components/NavBar/NavBar";
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import {useState, useEffect, useRef} from 'react';
import './editor.css'
import {useParams, useLocation} from "react-router-dom";
import InputField from "../../utils/InputField.jsx";
import Delta from 'quill-delta';

class item {
    constructor(id, left, right, content, isdeleted = false) {
        this.id = id;
        this.left = left;
        this.right = right;
        this.content = content;
    }
}

const ids = [];

// const item1 = new item("1@h", null, "1@m", th.ops[0]);
// const item2 = new item("1@m", "1@h", null, th.ops[1]);

const CRDT = {}
// [1@m, 1@h, 2@m, 3@m]

export default function Edit() {
    const quillRef = useRef(null);
    const [value, setValue] = useState(quillRef.current?.getEditor().getContents());
    const [range, setRange] = useState();
    const [lastChange, setLastChange] = useState();
    const [test, setTest] = useState();
    const {id} = useParams();
    const {state} = useLocation();
    const [counter, setCounter] = useState(0);
    const [input, setInput] = useState('');
    const [incomingId, setIncomingId] = useState('');
    // var editor;

    useEffect(() => {
        // editor = quillRef.current.getEditor();
        // editor.insertText(0, 'Hello, World!');
    }, []);

    return (<>
        <NavBar title={state}/>
        <InputField value={incomingId} setValue={setIncomingId} label='Incoming ID' type='text'/>
        <InputField value={input} setValue={setInput} label='Title' type='text'/>
        <button onClick={() => {
            const incoming = {
                id: incomingId, left: "2@m", right: "3@m", content: {insert: input}
            }
            // TODO: check if left is deleted then set left to the left of left
            // TODO: check if right is deleted then set right to the right of right
            while (CRDT[incoming.left].right !== incoming.right && CRDT[incoming.left].right.split('@')[1] > incoming.id.split('@')[1]) {
                incoming.left = CRDT[incoming.left].right;
            }
            incoming.right = CRDT[incoming.left].right;
            CRDT[incoming.id] = new item(incoming.id, incoming.left, incoming.right, incoming.content);
            CRDT[incoming.left].right = incoming.id;
            CRDT[incoming.right].left = incoming.id;
            console.log(CRDT);

            const quillidx = ids.indexOf(incoming.left);
            quillRef.current.getEditor().updateContents(new Delta().retain(quillidx).insert(incoming.content.insert), "silent");
            ids.splice(quillidx + 1, 0, incoming.id);


        }}
                className="text-blue-600 bg-blue-500 mr-6 self-end px-4 py-2 mb-4 rounded-3xl hover:bg-slate-100"
        >Save
        </button>
        <div className="bg-[#f1f3f4] flex justify-center p-4 min-h-screen">
            <div className="w-10/12 lg:w-8/12 text-black bg-white">
                <div id="toolbar" className='flex justify-center '>
                    <button className="ql-bold"/>
                    <button className="ql-italic"/>
                </div>
                <ReactQuill
                    ref={quillRef}
                    onChange={(value, delta, source, editor) => {
                        setValue(editor.getContents());
                        setLastChange(delta.ops);
                        if (source === 'silent') return;
                        console.log(delta.ops);
                        if ('insert' in delta.ops[delta.ops.length - 1]) {
                            const index = delta.ops[0].retain;
                            const id = counter + "@m";
                            ids.splice(index, 0, id);
                            setCounter(counter + 1);
                            CRDT[id] = new item(id, ids[index - 1], ids[index + 1], delta.ops[delta.ops.length - 1]);
                            if (index > 0) CRDT[ids[index - 1]].right = id;
                            if (index < ids.length - 1) CRDT[ids[index + 1]].left = id;
                            console.log(CRDT);
                        }
                        if ('delete' in delta.ops[delta.ops.length - 1]) {
                            const index = delta.ops[0].retain;
                            const id = ids[index];
                            ids.splice(index, 1);
                            CRDT[id].isdeleted = true;
                            console.log(CRDT);
                        }
                        console.log(ids);
                        // console.log(newdelta)
                        setTest(value)
                        // console.log(delta.diff(newdelta))
                    }}
                    onChangeSelection={setRange}
                    modules={{
                        toolbar: ['bold', 'italic']
                    }}
                />
                <div>
                    <div>Current value:</div>
                    {value ? JSON.stringify(value.ops) : 'Empty'}
                </div>
                <div>
                    <div>Current Range:</div>
                    {range ? JSON.stringify(range) : 'Empty'}
                </div>
                <div>
                    <div>Last Change:</div>
                    {lastChange ? JSON.stringify(lastChange) : 'Empty'}
                </div>
                <div>
                    <div>Test:</div>
                    {test}
                </div>
            </div>
        </div>
    </>);
}
