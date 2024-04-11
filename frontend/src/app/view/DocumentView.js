'use client';
import {useState, Fragment} from 'react'
import {Listbox, Transition} from '@headlessui/react'
import DocumentPill from './DocumentPill'

const files = [
    {
        name: 'Document 1',
        owner: 'John Doe',
        date: '12/12/2021'
    },
    {
        name: 'Document 2',
        owner: 'Jane Doe',
        date: '12/12/2021'
    },
    {
        name: 'Document 3',
        owner: 'John Doe',
        date: '12/12/2021'
    },
    {
        name: 'Document 4',
        owner: 'Jane Doe',
        date: '12/12/2021'
    },
    {
        name: 'Document 5',
        owner: 'John Doe',
        date: '12/12/2021'
    },
    {
        name: 'Document 6',
        owner: 'Jane Doe',
        date: '12/12/2021'
    },
    {
        name: 'Document 7',
        owner: 'John Doe',
        date: '12/12/2021'
    },
    {
        name: 'Document 8',
        owner: 'Jane Doe',
        date: '12/12/2021'
    },
    {
        name: 'Document 9',
        owner: 'John Doe',
        date: '12/12/2021'
    },
    {
        name: 'Document 10',
        owner: 'Jane Doe',
        date: '12/12/2021'
    },
    {
        name: 'Document 11',
        owner: 'John Doe',
        date: '12/12/2021'
    },
    {
        name: 'Document 12',
        owner: 'Jane Doe',
        date: '12/12/2021'
    },
    {
        name: 'Document 13',
        owner: 'John Doe',
        date: '12/12/2021'
    },
    {
        name: 'Document 14',
        owner: 'Jane Doe',
        date: '12/12/2021'
    },
    {
        name: 'Document 15',
        owner: 'John Doe',
        date: '12/12/2021'
    },
    {
        name: 'Document 16',
        owner: 'Jane Doe',
        date: '12/12/2021'
    },
    {
        name: 'Document 17',
        owner: 'John Doe',
        date: '12/12/2021'
    },
    {
        name: 'Document 18',
        owner: 'Jane Doe',
        date: '12/12/2021'
    },
    {
        name: 'Document 19',
        owner: 'John Doe',
        date: '12/12/2021'
    },
    {
        name: 'Document 20',
        owner: 'Jane Doe',
        date: '12/12/2021'
    }
]
export default function DocumentView() {
    const [selected, setSelected] = useState('owned by anyone')
    return (
        <div style={{backgroundColor: '#f1f3f4'}} className="flex flex-col justify-top items-center p-4 min-h-screen">
            <div className="flex justify-around items-top p-4 w-10/12">
                <div>
                    <h1 style={{color: '#5f6368', fontFamily: 'Product Sans'}} className="text-2xl font-bold">
                        Recent Documents
                    </h1>
                </div>
                <Listbox as='div' value={selected} onChange={setSelected}>
                    <Listbox.Button className="bg-white px-4 py-2 rounded-lg shadow-md">
                        <span style={{color: '#5f6368', fontFamily: 'Product Sans'}}
                              className="text-lg">{selected}</span>
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
                        <Listbox.Options style={{color: '#5f6368', fontFamily: 'Product Sans'}}
                                         className="absolute z-10 mt-2 w-48 p-2 bg-white rounded-md shadow-md text-md">
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
            {files.map((file, index) => {
                return <DocumentPill key={index} name={file.name} owner={file.owner} date={file.date}/>
            })}
        </div>
    )
}
