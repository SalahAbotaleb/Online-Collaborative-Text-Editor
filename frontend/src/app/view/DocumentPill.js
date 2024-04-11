'use client'
import menu from '../assets/menu-icon.svg';
import Image from 'next/image';
import {Fragment} from 'react'
import {Menu, Transition} from '@headlessui/react'

function classNames(...classes) {
    return classes.filter(Boolean).join(' ')
}

export default function DocumentPill({name, owner, date}) {
    return (
        <div
            className="flex justify-between items-center mb-4 p-4 px-8 bg-white rounded-lg shadow-md w-10/12 hover:bg-blue-300 cursor-pointer">
            <h1 style={{color: '#5f6368', fontFamily: 'Product Sans'}}
                className="basis-7/12 text-xl font-bold">{name}</h1>
            <p style={{color: '#5f6368', fontFamily: 'Product Sans'}} className="text-md">{owner}</p>
            <p style={{color: '#5f6368', fontFamily: 'Product Sans'}} className="text-md">{date}</p>

            <Menu as='div' className="relative inline-block">
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
                        className="absolute right-0 z-10 mt-2 w-56 origin-top-right rounded-md bg-white shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none">
                        <div className="py-1">
                            <Menu.Item>
                                {({active}) => (
                                    <a
                                        href="#"
                                        className={classNames(
                                            active ? 'bg-gray-100 text-gray-900' : 'text-gray-700',
                                            'block px-4 py-2 text-sm'
                                        )}
                                    >
                                        Delete
                                    </a>
                                )}
                            </Menu.Item>
                            <Menu.Item>
                                {({active}) => (
                                    <a
                                        href="#"
                                        className={classNames(
                                            active ? 'bg-gray-100 text-gray-900' : 'text-gray-700',
                                            'block px-4 py-2 text-sm'
                                        )}
                                    >
                                        Rename
                                    </a>
                                )}
                            </Menu.Item>
                            <Menu.Item>
                                {({active}) => (
                                    <a
                                        href="#"
                                        className={classNames(
                                            active ? 'bg-gray-100 text-gray-900' : 'text-gray-700',
                                            'block px-4 py-2 text-sm'
                                        )}
                                    >
                                        Share
                                    </a>
                                )}
                            </Menu.Item>
                        </div>
                    </Menu.Items>
                </Transition>
            </Menu>


        </div>
    )
}
