'use client'
import menu from '../assets/menu-icon.svg';
import Image from 'next/image';
import {Fragment} from 'react'
import {Menu, Transition} from '@headlessui/react'
import DeleteIcon from '@mui/icons-material/Delete';
import DriveFileRenameOutlineRoundedIcon from '@mui/icons-material/DriveFileRenameOutlineRounded';
import ShareIcon from '@mui/icons-material/Share';
import {useState} from 'react';
import Rename from './Rename';
import Delete from './Delete';
import Share from './Share';

export default function DocumentPill({file, files, setFiles}) {
    const [rename, setRename] = useState(false);
    const [deleteDoc, setDeleteDoc] = useState(false);
    const [share, setShare] = useState(false);

    return (
        <div
            className="flex justify-between items-center mb-4 py-4 pl-8 bg-white rounded-lg shadow-md w-10/12 hover:bg-blue-300 cursor-pointer">
            <h1 className="text-[#5f6368] font-['Product_sans'] truncate basis-7/12 text-xl font-bold">{file.title}</h1>
            <p className="text-[#5f6368] font-['Product_sans'] truncate text-center basis-2/12 text-md">{file.owner}</p>
            <p className="text-[#5f6368] font-['Product_sans'] truncate text-center basis-2/12 text-md">{file.date}</p>
            <Rename open={rename} setOpen={setRename} file={file} files={files} setFiles={setFiles}/>
            <Delete open={deleteDoc} setOpen={setDeleteDoc} file={file} files={files} setFiles={setFiles}/>
            <Share open={share} setOpen={setShare} title={file.title}/>

            <Menu as='div' className="relative inline-block basis-1/12 text-center">
                <Menu.Button type="button"
                             className="hover:bg-slate-400 rounded-full p-2.5 inline-flex items-center me-2">
                    <Image src={menu} alt="Menu" width={20} height={20}/>
                </Menu.Button>
                <Transition
                    as={Fragment}
                    enter="transition ease-out duration-100"
                    enterFrom="transform opacity-0 scale-95"
                    enterTo="transform opacity-100 scale-100"
                    leave="transition ease-in duration-75"
                    leaveFrom="transform opacity-100 scale-100"
                    leaveTo="transform opacity-0 scale-95"
                >
                    <Menu.Items
                        className="absolute right-0 z-10 mt-2 w-56 p-2 text-left origin-top-right rounded-md bg-white shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none">
                        <div className="py-1">
                            <Menu.Item>
                                <div
                                    onClick={() => setDeleteDoc(true)}
                                    className='hover:bg-gray-200 text-gray-900 block pl-2 pr-4 py-2 text-sm rounded-md'
                                >
                                    <DeleteIcon sx={{color:'#5f6368', marginRight:1}}/>
                                    Delete
                                </div>
                            </Menu.Item>
                            <Menu.Item>
                                <div
                                    onClick={() => setRename(true)}
                                    className='hover:bg-gray-200 text-gray-900 block pl-2 pr-4 py-2 text-sm rounded-md'
                                >
                                    <DriveFileRenameOutlineRoundedIcon sx={{color:'#5f6368', marginRight:1}}/>
                                    Rename
                                </div>
                            </Menu.Item>
                            <Menu.Item>
                                <div
                                    onClick={() => setShare(true)}
                                    className='hover:bg-gray-200 text-gray-900 block pl-2 pr-4 py-2 text-sm rounded-md'
                                >
                                    <ShareIcon sx={{color:'#5f6368', marginRight:1}}/>
                                    Share
                                </div>
                            </Menu.Item>
                        </div>
                    </Menu.Items>
                </Transition>
            </Menu>


        </div>
    )
}