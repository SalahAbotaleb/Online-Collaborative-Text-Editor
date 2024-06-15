import { BrowserRouter, Routes, Route, useNavigate } from "react-router-dom";
import { StompSessionProvider } from "react-stomp-hooks";
import Sign from './pages/Sign';
import View from './pages/View';
import Edit from './pages/edit/Edit'
import ScrollToTop from "./utils/ScrollToTop.jsx";
import { useEffect, useState } from "react";


function App() {

    const [username, setUsername] = useState('');
    const [loggedin, setLoggedin] = useState(false);
    const [jwtKey, setJwtKey] = useState('');


    // useEffect(() => {
    //     let jwt = document.cookie.split(';').find(cookie => cookie.includes('jwt'));
    //     console.log(document.cookie.split(';'));
    //     if (jwt) {
    //         setJwtKey(jwt.split('=')[1]);
    //     }
    // }, [username]);

    // useEffect(() => {
    //     setJwtKey(localStorage.getItem('jwtKey'));
    // }, [loggedin]);

    return (
        <BrowserRouter>
            <ScrollToTop />
            <Routes>
                <Route path="/" element={<Sign setUsername={setUsername} username={username} setLoggedin={setLoggedin} />} />
                <Route path="/view" element={<View />} />
                <Route path={'/edit/:docId'} element={<EditWrapper username={username} />} />
            </Routes>
        </BrowserRouter>
    )
}

function EditWrapper({ username }) {
    const navigate = useNavigate();

    return (
        <StompSessionProvider url={'wss://docscrdt.azurewebsites.net/docs/ws'}
            connectHeaders={{ "Authentication": localStorage.getItem('jwtKey') }}
            onDisconnect={() => navigate('/view')}
            debug={test => console.log(test)}>
            <Edit username={username} />
        </StompSessionProvider>
    );
}

export default App
