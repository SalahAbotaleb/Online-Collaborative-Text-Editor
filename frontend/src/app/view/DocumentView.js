'use client';
import {useState, Fragment} from 'react'
import {Listbox, Transition} from '@headlessui/react'
import DocumentPill from './DocumentPill'
import ArrowDropDownRoundedIcon from '@mui/icons-material/ArrowDropDownRounded';

const files = [
    {
        _id: 1,
        title: 'Document jknkjnkjnkjnasd1',
        owner: 'Johneqweqweqweasdasdasdasad Doe',
        date: '12/12/2021'
    },
    {
        _id: 2,
        title: 'Document 2',
        owner: 'Jane Doe',
        date: '12/12/2021'
    },
    {
        _id: 3,
        title: 'Document 3',
        owner: 'Jane Doe',
        date: '12/12/2021'
    },
    {
        _id: 4,
        title: 'Document 4',
        owner: 'Jane Doe',
        date: '12/12/2021'
    },
    {
        _id: 5,
        title: 'Document 5',
        owner: 'Jane Doe',
        date: '12/12/2021'
    },
    {
        _id: 6,
        title: 'Document 6',
        owner: 'Jane Doe',
        date: '12/12/2021'
    },
    {
        _id: 7,
        title: 'Document 7',
        owner: 'Jane Doe',
        date: '12/12/2021'
    },
    {
        _id: 8,
        title: 'Document 8',
        owner: 'Jane Doe',
        date: '12/12/2021'
    },
    {
        _id: 9,
        title: 'Document 9',
        owner: 'Jane Doe',
        date: '12/12/2021'
    },
    {
        _id: 10,
        title: 'Document 10',
        owner: 'Jane Doe',
        date: '12/12/2021'
    },
    {
        _id: 11,
        title: 'Document 11',
        owner: 'Jane Doe',
        date: '12/12/2021'
    },
    {
        _id: 12,
        title: 'Document 12',
        owner: 'Jane Doe',
        date: '12/12/2021'
    },
    {
        _id: 13,
        title: 'Document 13',
        owner: 'Jane Doe',
        date: '12/12/2021'
    },
    {
        _id: 14,
        title: 'Document 14',
        owner: 'Jane Doe',
        date: '12/12/2021'
    }
]
export default function DocumentView() {
    const [selected, setSelected] = useState('owned by anyone');
    const [docs, setDocs] = useState(files);
    return (
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
                        <ArrowDropDownRoundedIcon sx={{color:'#5f6368', marginLeft:1, fontSize: 33}}/>
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
                        <Listbox.Options className="text-[#5f6368] font-['Product_sans'] absolute z-10 mt-2 w-48 p-2 bg-white rounded-md shadow-md text-md">
                            <Listbox.Option value="owned by anyone"
                                            className="rounded-md cursor-pointer p-2 hover:bg-gray-200">Owned by
                                anyone</Listbox.Option>
                            <Listbox.Option value="owned by me"
                                            className="rounded-md cursor-pointer p-2 hover:bg-gray-200">Owned
                                by me</Listbox.Option>
                            <Listbox.Option value="shared with me"
                                            className="rounded-md cursor-pointer p-2 hover:bg-gray-200">Shared with
                                me</Listbox.Option>
                        </Listbox.Options>
                    </Transition>
                </Listbox>
            </div>
            <div className="flex justify-between items-center py-4 pl-8 rounded-lg w-10/12">
                <h1 className="text-[#5f6368] font-['Product_sans'] truncate basis-7/12 text-xl font-bold">Title</h1>
                <p className="text-[#5f6368] font-['Product_sans'] truncate text-center basis-2/12 text-md">Owner</p>
                <p className="text-[#5f6368] font-['Product_sans'] truncate text-center basis-2/12 text-md">Date</p>
                <div className="basis-1/12"/>
            </div>
            {docs.map((file) => {
                return <DocumentPill key={file._id} file={file} files={docs} setFiles={setDocs}/>
            })}
        </div>
    )
}
