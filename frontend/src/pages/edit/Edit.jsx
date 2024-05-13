import NavBar from "../../components/NavBar/NavBar";
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import {useState, useEffect, useRef} from 'react';
import './editor.css'
import {useParams, useLocation} from "react-router-dom";
import {useSubscription, useStompClient} from "react-stomp-hooks";
import InputField from "../../utils/InputField.jsx";
import Delta from 'quill-delta';

class item {
    constructor(id, left, right, content, isdeleted = false) {
        this.id = id;
        this.left = left;
        this.right = right;
        this.content = content;
        this.isdeleted = isdeleted;
    }
}

const ids = [];

// const item1 = new item("1@h", null, "1@m", th.ops[0]);
// const item2 = new item("1@m", "1@h", null, th.ops[1]);

const CRDT = {}
// [1@m, 1@h, 2@m, 3@m]

export default function Edit({username}) {
    const quillRef = useRef(null);
    const [value, setValue] = useState(quillRef.current?.getEditor().getContents());
    const [range, setRange] = useState();
    const [lastChange, setLastChange] = useState();
    const [test, setTest] = useState();
    const {docId} = useParams();
    const {state} = useLocation();
    const [counter, setCounter] = useState(0);
    const [input, setInput] = useState('');
    const [incomingId, setIncomingId] = useState('');
    const [firstItem, setFirstItem] = useState(null);
    const [left, setLeft] = useState('');
    const [right, setRight] = useState('');
    // const [incomingItem, setIncomingItem] = useState(null);
    // var editor;

    useSubscription(`/docs/broadcast/changes/${docId}`, (msg) => {
        let incomingItem = JSON.parse(msg.body);
        if (incomingItem === null) return;
        if (incomingItem.id.split('@')[1] === left) return;
        console.log(incomingItem);
        if (incomingItem.id.split('@')[1] === 'z') return;
        if (incomingItem.operation === 'delete') {
            const index = ids.indexOf(incomingItem.id);
            ids.splice(index, 1);
            CRDT[incomingItem.id].isdeleted = true;
            quillRef.current.getEditor().updateContents(new Delta().retain(index).delete(1), "silent");
            console.log(CRDT);
            return;
        }

        console.log(incomingItem);
        const incoming = new item(incomingItem.id, incomingItem.left, incomingItem.right, incomingItem.content);
        console.log(incoming);
        if (incoming.left === null) {
            if (firstItem !== incoming.right && firstItem.split('@')[1] > incoming.id.split('@')[1]) {
                incoming.left = firstItem;
                console.log(incoming);
            } else {
                incoming.right = firstItem;
                if (firstItem !== null)
                    CRDT[firstItem].left = incoming.id;
                setFirstItem(incoming.id);

                CRDT[incoming.id] = new item(incoming.id, incoming.left, incoming.right, incoming.content);

                const quillidx = ids.indexOf(incoming.left);
                quillRef.current.getEditor().updateContents(new Delta().retain(quillidx + 1).insert(incoming.content), "silent");
                ids.splice(quillidx + 1, 0, incoming.id);
                console.log('here');
                return;
            }
        }
        while (CRDT[incoming.left].right !== incoming.right && CRDT[incoming.left].right.split('@')[1] > incoming.id.split('@')[1]) {
            incoming.left = CRDT[incoming.left].right;
        }
        incoming.right = CRDT[incoming.left].right;
        CRDT[incoming.id] = new item(incoming.id, incoming.left, incoming.right, incoming.content);
        CRDT[incoming.left].right = incoming.id;
        if (incoming.right !== null)
            CRDT[incoming.right].left = incoming.id;

        while (CRDT[incoming.left].isdeleted) {
            incoming.left = CRDT[incoming.left].left;
        }
        const quillidx = ids.indexOf(incoming.left);
        quillRef.current.getEditor().updateContents(new Delta().retain(quillidx + 1).insert(incoming.content), "silent");
        ids.splice(quillidx + 1, 0, incoming.id);
    });
    const stompClient = useStompClient();

    // useEffect(() => {
    //
    //
    // }, [incomingItem]);

    return (<>
        <NavBar title={state}/>
        <InputField value={left} setValue={setLeft} label='Left' type='text'/>
        <InputField value={right} setValue={setRight} label='Right' type='text'/>
        <InputField value={incomingId} setValue={setIncomingId} label='Incoming ID' type='text'/>
        <InputField value={input} setValue={setInput} label='Title' type='text'/>
        <button onClick={() => {
            const incoming = {
                id: incomingId, left: null, right: right, content: {insert: input}
            }

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
                            const id = counter + "@" + left;
                            ids.splice(index, 0, id);
                            setCounter(counter + 1);
                            let itm = new item(id, null, null, delta.ops[delta.ops.length - 1].insert);
                            if (!index) {
                                itm.left = null;
                                itm.right = firstItem;
                                if (firstItem !== null) {
                                    CRDT[firstItem].left = id;
                                }
                                setFirstItem(id);
                            } else {
                                itm.left = ids[index - 1];
                                if (CRDT[ids[index - 1]].right !== null) {
                                    itm.right = CRDT[ids[index - 1]].right;
                                    CRDT[CRDT[ids[index - 1]].right].left = id;
                                }
                                CRDT[ids[index - 1]].right = id;
                            }
                            CRDT[id] = itm;
                            console.log(CRDT);
                            stompClient.publish({
                                destination: `/docs/change/${docId}`,
                                body: JSON.stringify(itm)
                            });
                        }
                        if ('delete' in delta.ops[delta.ops.length - 1]) {
                            const index = delta.ops[0].retain ? delta.ops[0].retain : 0;
                            const id = ids[index];
                            ids.splice(index, 1);
                            CRDT[id].isdeleted = true;
                            console.log({operation: "delete", id: id});
                            stompClient.publish({
                                destination: `/docs/change/${docId}`,
                                body: JSON.stringify({operation: "delete", id: id})
                            });
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
