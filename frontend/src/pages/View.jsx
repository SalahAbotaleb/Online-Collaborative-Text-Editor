import {useState, Fragment, useEffect} from 'react'
import {Listbox, Transition} from '@headlessui/react'
import DocumentPill from '../components/view/DocumentPill'
import ArrowDropDownRoundedIcon from '@mui/icons-material/ArrowDropDownRounded';
import plus from '../assets/plus.png';
import CreateDoc from '../components/view/CreateDoc';
import NavBar from "../components/NavBar/NavBar.jsx";
import loadingGif from '../assets/loading.gif';
import {useNavigate} from "react-router-dom";

export default function View() {
    const [selected, setSelected] = useState('owned by anyone');
    const [docs, setDocs] = useState([]);
    const [create, setCreate] = useState(false);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const username = localStorage.getItem('username');

    useEffect(() => {
        fetch('https://docscrdt.azurewebsites.net/api/docs/all', {
            headers: {
                "Authorization": localStorage.getItem('jwtKey')
            }
        })
            .then(res => res.json())
            .then(data => {
                setDocs(data);
                console.log(data);
                setLoading(false)
            })
            .catch(() => {
                navigate('/');
            })
    }, []);
    return (<>
        {loading && <div className="h-screen w-screen bg-[#f1f3f4] flex justify-center items-center">
            <img src={loadingGif} className="m-auto"/>
        </div>}
        {!loading && <><NavBar title='Docs' signedin={!loading} setsignedin={setLoading}/>
            <div className="bg-[#f1f3f4] flex flex-col justify-top items-center p-4 min-h-screen">
                <div className="flex justify-around items-top p-4 w-10/12">
                    <div>
                        <h1 className="text-[#5f6368] font-['Product_sans'] text-2xl font-bold">
                            Recent Documents
                        </h1>
                    </div>
                    <Listbox as='div' value={selected} onChange={setSelected}>
                        <Listbox.Button className="bg-white pl-4 pr-3 py-2 rounded-lg shadow-md">
                            <span className="text-[#5f6368] font-['Product_sans'] text-lg">{selected}</span>
                            <ArrowDropDownRoundedIcon sx={{color: '#5f6368', marginLeft: 1, fontSize: 33}}/>
                        </Listbox.Button>
                        <Transition
                            as={Fragment}
                            enter="transition ease-out duration-100"
                            enterFrom="transform opacity-0 scale-95"
                            enterTo="transform opacity-100 scale-100"
                            leave="transition ease-in duration-75"
                            leaveFrom="transform opacity-100 scale-100"
                            leaveTo="transform opacity-0 scale-95"
                        >
                            <Listbox.Options
                                className="text-[#5f6368] font-['Product_sans'] absolute z-10 mt-2 w-48 p-2 bg-white rounded-md shadow-md text-md">
                                <Listbox.Option value="owned by anyone"
                                                className="rounded-md cursor-pointer p-2 hover:bg-gray-200">Owned by
                                    anyone</Listbox.Option>
                                <Listbox.Option value="owned by me"
                                                className="rounded-md cursor-pointer p-2 hover:bg-gray-200">Owned
                                    by me</Listbox.Option>
                                <Listbox.Option value="shared with me"
                                                className="rounded-md cursor-pointer p-2 hover:bg-gray-200">Shared
                                    with
                                    me</Listbox.Option>
                            </Listbox.Options>
                        </Transition>
                    </Listbox>
                </div>

                <div className="flex justify-between items-center py-4 pl-8 rounded-lg w-10/12">
                    <h1 className="text-[#5f6368] font-['Product_sans'] truncate basis-7/12 text-xl font-bold">Title</h1>
                    <p className="text-[#5f6368] font-['Product_sans'] truncate text-center basis-2/12 text-md">Owner</p>
                    <p className="text-[#5f6368] font-['Product_sans'] truncate text-center basis-2/12 text-md">Shared with</p>
                    <div className="basis-1/12"/>
                </div>

                {docs.map((file) => {
                    if (selected === 'owned by me' && file.owner !== username) return null;
                    if (selected === 'shared with me' && !file.sharedWith.some(shared => shared.username === username)) return null;
                    return <DocumentPill key={file.id} file={file} files={docs} setFiles={setDocs}/>
                })}
                {docs.length === 0 && <div className="w-10/12 bg-white p-4 rounded-lg shadow-md">
                    <h1 className="text-[#5f6368] font-['Product_sans'] text-xl font-bold text-center">No documents
                        found</h1>
                </div>}
                <button
                    onClick={() => setCreate(true)}
                    className="bg-white shadow-md hover:shadow-xl fixed right-8 bottom-8 text-white rounded-full w-14 h-14 flex justify-center items-center overflow-hidden">
                    <img src={plus} alt="Add" width={64} height={64}/>
                </button>
                <CreateDoc open={create} setOpen={setCreate} files={docs} setFiles={setDocs}/>
            </div>
        </>}
    </>)
}
