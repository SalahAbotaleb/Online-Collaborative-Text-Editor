import NavBar from "../../components/NavBar/NavBar";
import ReactQuill, {Quill} from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import {useState, useRef, useEffect} from 'react';
import './editor.css'
import {useParams, useLocation} from "react-router-dom";
import {useSubscription, useStompClient} from "react-stomp-hooks";
import InputField from "../../utils/InputField.jsx";
import Delta from 'quill-delta';
import QuillCursors from "quill-cursors";
import loadingGif from "../../assets/loading.gif";

Quill.register('modules/cursors', QuillCursors);

class item {
    constructor(id, left, right, content, isdeleted = false, isbold = false, isitalic = false) {
        this.id = id;
        this.left = left;
        this.right = right;
        this.content = content;
        this.isdeleted = isdeleted;
        this.isbold = isbold;
        this.isitalic = isitalic;
    }
}

// const ids = [];

// const item1 = new item("1@h", null, "1@m", th.ops[0]);
// const item2 = new item("1@m", "1@h", null, th.ops[1]);

// const CRDT = {}
// [1@m, 1@h, 2@m, 3@m]

export default function Edit() {
    const quillRef = useRef(null);
    const [value, setValue] = useState(quillRef.current?.getEditor().getContents());
    const [range, setRange] = useState();
    const [lastChange, setLastChange] = useState();
    const [test, setTest] = useState();
    const {docId} = useParams();
    const {state} = useLocation();
    const [counter, setCounter] = useState(0);
    const [loading, setLoading] = useState(true);
    const [loadingContent, setLoadingContent] = useState(true);
    const [firstItem, setFirstItem] = useState(null);
    const [cursor, setCursor] = useState(null);
    const [username] = useState(localStorage.getItem('username'));
    const [ids, setIds] = useState([]);
    const [CRDT, setCRDT] = useState({});
    const [currentUsers, setCurrentUsers] = useState([]);

    const [isOwner, setIsOwner] = useState(false);
    const [isEditor, setIsEditor] = useState(false);

    const stompClient = useStompClient();

    useEffect(() => {
        fetch(`https://docscrdt.azurewebsites.net/api/docs/${docId}`, {
            method: 'GET', headers: {
                "Authorization": localStorage.getItem('jwtKey')
            },
        }).then(res => res.json()).then(data => {
            setIsOwner(data.owner === username);
            setIsEditor(data.sharedWith.some(user => user.username === username && user.permission === 'EDIT'));
            setLoading(false);
        }).catch(err => {
            console.log(err);
        });
    }, []);

    useEffect(() => {
        if (!quillRef.current) return;
            setCursor(quillRef.current.getEditor().getModule('cursors'));

        fetch(`https://docscrdt.azurewebsites.net/api/docs/changes/${docId}`, {
            method: 'GET', headers: {
                "Authorization": localStorage.getItem('jwtKey')
            },
        }).then(res => res.json()).then(data => {
            // console.log(data);
            setLoading(false);
            const tempids = [];
            let maxcounter = 0;
            data.forEach((itm) => {
                if (itm.id.split('@')[1] === username) {
                    maxcounter = Math.max(maxcounter, parseInt(itm.id.split('@')[0]));
                }

                const id = itm.id;
                // console.log(itm);
                CRDT[id] = new item(id, itm.left, itm.right, itm.content, itm.isdeleted, itm.isbold, itm.isitalic);
                // setCRDT(oldstate => ({...oldstate, [id]: new item(id, itm.left, itm.right, itm.content, itm.isdeleted, itm.isbold, itm.isitalic)}));
                if (itm.left === null) {
                    setFirstItem(id);
                }
                // console.log(ids);
                if (itm.isdeleted) return;
                tempids.push(id);
                // setIds([...ids, id])
                // console.log(id);
                const quillidx = tempids.indexOf(itm.id);
                let attributes = {};
                attributes.bold = itm.isbold;
                attributes.italic = itm.isitalic;
                quillRef.current.getEditor().updateContents(new Delta().retain(quillidx).insert(itm.content, attributes), "silent");

            });
            setIds(tempids);
            setCounter(maxcounter + 1);
            setLoadingContent(false);
        }).catch(err => {
            console.log(err);
        });
    }, [loading]);

    useEffect(() => {
        if (!cursor) return;
        currentUsers.forEach((username) => {
            const randomColor = '#' + Math.floor(Math.random() * 16777215).toString(16);
            cursor.createCursor(username, username, randomColor);
        });
    }, [cursor, currentUsers]);

    // useEffect(() => {
    //     if (!quillRef.current) return;
    //     setCursor(quillRef.current.getEditor().getModule('cursors'));
    //
    //     ids.forEach((id) => {
    //         const index = ids.indexOf(id);
    //         console.log(CRDT[id]);
    //         let attributes = {};
    //         attributes.bold = CRDT[id].isbold;
    //         attributes.italic = CRDT[id].isitalic;
    //         quillRef.current.getEditor().updateContents(new Delta().retain(index + 1).insert(CRDT[id].content, attributes), "silent");
    //     });
    // }, [quillRef.current]);

    useSubscription(`/docs/broadcast/usernames/${docId}`, (msg) => {
        // if (!quillRef.current) return;
        console.log(msg.body);
        let incomingUsername = JSON.parse(msg.body).usernames;
        if (incomingUsername === null) return;

        setCurrentUsers(incomingUsername);
    });

    useSubscription(`/docs/broadcast/cursors/${docId}`, (msg) => {
        if (loading) return;
        let incomingCursor = JSON.parse(msg.body);
        if (incomingCursor === null || incomingCursor.username === username) return;
        console.log(incomingCursor);
        cursor.moveCursor(incomingCursor.username, {index: incomingCursor.index, length: incomingCursor.length});
    });

    useSubscription(`/docs/broadcast/changes/${docId}`, (msg) => {
        // if (loading) return;
        let incomingItem = JSON.parse(msg.body);
        if (incomingItem === null) return;

        // console.log(CRDT);
        // console.log(ids);

        // console.log(incomingItem);
        if (incomingItem.operation === 'format') {
            CRDT[incomingItem.id].isbold = incomingItem.isbold;
            CRDT[incomingItem.id].isitalic = incomingItem.isitalic;
            const index = ids.indexOf(incomingItem.id);
            let attributes = {};
            attributes.bold = incomingItem.isbold;
            attributes.italic = incomingItem.isitalic;
            quillRef.current.getEditor().updateContents(new Delta().retain(index).retain(1, attributes), "silent");
            return;
        }
        if (incomingItem.operation === 'delete') {
            if (CRDT[incomingItem.id].isdeleted) return;
            const index = ids.indexOf(incomingItem.id);
            ids.splice(index, 1);
            // setIds(ids.filter(id => id !== incomingItem.id));
            CRDT[incomingItem.id].isdeleted = true;
            quillRef.current.getEditor().updateContents(new Delta().retain(index).delete(1), "silent");
            // console.log(CRDT);
            return;
        }

        if (incomingItem.id.split('@')[1] === username) return;

        // console.log(incomingItem);
        const incoming = new item(incomingItem.id, incomingItem.left, incomingItem.right, incomingItem.content, incomingItem.isdeleted, incomingItem.isbold, incomingItem.isitalic);
        // console.log(incoming);
        if (incoming.left === null) {
            if (firstItem !== incoming.right && firstItem.split('@')[1] > incoming.id.split('@')[1]) {
                incoming.left = firstItem;
                // console.log(incoming);
            } else {
                incoming.right = firstItem;
                if (firstItem !== null) CRDT[firstItem].left = incoming.id;
                setFirstItem(incoming.id);

                CRDT[incoming.id] = new item(incoming.id, incoming.left, incoming.right, incoming.content);

                const quillidx = ids.indexOf(incoming.left);
                quillRef.current.getEditor().updateContents(new Delta().retain(quillidx + 1).insert(incoming.content), "silent");
                ids.splice(quillidx + 1, 0, incoming.id);
                // setIds([incoming.id, ...ids]);
                // console.log('here');
                return;
            }
        }
        console.log(incoming);
        console.log(CRDT);
        while (CRDT[incoming.left].right !== incoming.right && CRDT[incoming.left].right.split('@')[1] > incoming.id.split('@')[1]) {
            incoming.left = CRDT[incoming.left].right;
        }
        incoming.right = CRDT[incoming.left].right;
        CRDT[incoming.id] = new item(incoming.id, incoming.left, incoming.right, incoming.content);
        // setCRDT(oldstate => ({...oldstate, [incoming.id]: new item(incoming.id, incoming.left, incoming.right, incoming.content)}));
        CRDT[incoming.left].right = incoming.id;
        if (incoming.right !== null) CRDT[incoming.right].left = incoming.id;

        while (CRDT[incoming.left].isdeleted) {
            incoming.left = CRDT[incoming.left].left;
        }
        const quillidx = ids.indexOf(incoming.left);
        let attributes = {};
        attributes.bold = incoming.isbold;
        attributes.italic = incoming.isitalic;
        // console.log(attributes);
        quillRef.current.getEditor().updateContents(new Delta().retain(quillidx + 1).insert(incoming.content, attributes), "silent");
        ids.splice(quillidx + 1, 0, incoming.id);
        // setIds(oldstate => [...oldstate.slice(0, quillidx + 1), incoming.id, ...oldstate.slice(quillidx + 1)]);

    });

    return (<>
        {loading && <div className="h-screen w-screen bg-[#f1f3f4] flex justify-center items-center">
            <img src={loadingGif} className="m-auto"/>
        </div>}
        {!loading && <>
            <NavBar title={state} signedin={loading} setsignedin={setLoading} usernames={currentUsers}/>

            <div className="bg-[#f1f3f4] flex justify-center p-4 min-h-screen">
                <div className="w-10/12 lg:w-8/12 text-black bg-white">
                    <div id="toolbar" className='flex justify-center '>
                        <button className="ql-bold"/>
                        <button className="ql-italic"/>
                    </div>
                    <ReactQuill
                        ref={quillRef}
                        readOnly={(!isEditor && !isOwner) || loadingContent}
                        onChange={(value, delta, source, editor) => {
                            setValue(editor.getContents());
                            setLastChange(delta.ops);
                            if (source === 'silent') return;
                            // console.log(delta.ops);
                            if ('insert' in delta.ops[delta.ops.length - 1]) {
                                const index = delta.ops[0].retain;
                                const id = counter + "@" + username;
                                ids.splice(index, 0, id);
                                // setIds([...ids.slice(0, index), id, ...ids.slice(index)]);
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
                                    console.log(ids[index - 1]);
                                    console.log(CRDT[ids[index - 1]]);
                                    console.log(CRDT);
                                    if (CRDT[ids[index - 1]].right !== null) {
                                        itm.right = CRDT[ids[index - 1]].right;
                                        CRDT[CRDT[ids[index - 1]].right].left = id;
                                    }
                                    CRDT[ids[index - 1]].right = id;
                                }
                                if ('attributes' in delta.ops[delta.ops.length - 1]) {
                                    let attribute = delta.ops[delta.ops.length - 1].attributes;
                                    if ('bold' in attribute) itm.isbold = true;
                                    if ('italic' in attribute) itm.isitalic = true;
                                }
                                CRDT[id] = itm;
                                // setCRDT(oldstate => ({...oldstate, [id]: itm}));
                                // console.log(CRDT);
                                stompClient.publish({
                                    destination: `/docs/change/${docId}`,
                                    body: JSON.stringify({...itm, operation: "insert"})
                                });
                            } else if ('delete' in delta.ops[delta.ops.length - 1]) {
                                const index = delta.ops[0].retain ? delta.ops[0].retain : 0;
                                const id = ids[index];
                                ids.splice(index, 1);
                                // setIds(ids.filter(id => id !== ids[index]));
                                CRDT[id].isdeleted = true;
                                // console.log({operation: "delete", id: id});
                                stompClient.publish({
                                    destination: `/docs/change/${docId}`,
                                    body: JSON.stringify({operation: "delete", id: id})
                                });
                                // console.log(CRDT);
                            } else if ('retain' in delta.ops[delta.ops.length - 1]) {
                                // const letters = delta.ops[delta.ops.length - 1].retain;
                                let index = 0;
                                for (let i = 0; i < delta.ops.length; i++) {
                                    if ('attributes' in delta.ops[i]) {
                                        for (let j = 0; j < delta.ops[i].retain; j++) {
                                            const id = ids[index + j];
                                            CRDT[id].isbold = delta.ops[i].attributes.bold;
                                            CRDT[id].isitalic = delta.ops[i].attributes.italic;

                                            stompClient.publish({
                                                destination: `/docs/change/${docId}`,
                                                body: JSON.stringify({...CRDT[id], operation: "format"})
                                            });
                                        }
                                    }
                                    index += delta.ops[i].retain;
                                }
                            }

                            // console.log(ids);
                            // console.log(newdelta)
                            setTest(value)
                            // console.log(delta.diff(newdelta))
                        }}
                        onChangeSelection={(range, source, editor) => {
                            setRange(range);
                            // if (source === 'test') return;
                            stompClient.publish({
                                destination: `/docs/cursor/${docId}`,
                                body: JSON.stringify({username: username, index: range.index, length: range.length})
                            });
                        }}
                        modules={{
                            toolbar: ['bold', 'italic'], cursors: {selectionChangeSource: 'test'}
                        }}
                    />
                </div>
            </div>
        </>}
    </>);
}
