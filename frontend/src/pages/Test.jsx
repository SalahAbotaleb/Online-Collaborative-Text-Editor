import {useSubscription, useStompClient} from "react-stomp-hooks";
import {useState} from "react";
import InputField from "../utils/InputField.jsx";

export default function Test() {
    const [msg, setMsg] = useState('');

    useSubscription('/docs/broadcast/changes/663b79bd198a822bc0f095f8', (msg) => setMsg(msg.body));
    const stompClient = useStompClient();
    return (<div>
        <InputField label='Message' value={msg} setValue={setMsg} type='text'/>
        <button onClick={() => {
            stompClient.publish({
                destination: '/docs/change/663b79bd198a822bc0f095f8',
                body: JSON.stringify({msg})
            })
        }}>Send Message</button>
    </div>)
}
