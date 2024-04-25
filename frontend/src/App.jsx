import {BrowserRouter, Routes, Route} from "react-router-dom";
import Sign from './pages/Sign';
import View from './pages/View';
import Edit from './pages/edit/Edit'
import ScrollToTop from "./utils/ScrollToTop.jsx";


function App() {

    return (
        <BrowserRouter>
            <ScrollToTop/>
            <Routes>
                <Route path="/" element={<Sign/>}/>
                <Route path="/view" element={<View/>}/>
                <Route path='/edit/:id' element={<Edit/>}/>
            </Routes>
        </BrowserRouter>
    )
}

export default App
