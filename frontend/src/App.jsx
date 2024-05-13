import {BrowserRouter, Routes, Route} from "react-router-dom";
import {StompSessionProvider} from "react-stomp-hooks";
import Sign from './pages/Sign';
import View from './pages/View';
import Edit from './pages/edit/Edit'
import ScrollToTop from "./utils/ScrollToTop.jsx";
import {useState} from "react";


function App() {

    const [username, setUsername] = useState('');

    return (<BrowserRouter>
        <ScrollToTop/>
        <Routes>
            <Route path="/" element={<Sign setUsername={setUsername} username={username}/>}/>
            <Route path="/view" element={<View/>}/>
            <Route path={'/edit/:docId'} element=
                {<StompSessionProvider url={'wss://docscrdt.azurewebsites.net/docs/ws'}
                                       connectHeaders={{"Authentication": `Bearer ${localStorage.getItem('token')}`}}
                                       debug={test => console.log(test)}>
                    <Edit username={username}/>
                </StompSessionProvider>}/>
        </Routes>
    </BrowserRouter>)
}

export default App
