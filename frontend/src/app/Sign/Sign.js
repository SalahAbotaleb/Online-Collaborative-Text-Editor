'use client';
import {useState} from 'react';
import docimg from '../assets/doc_image.png';
import Image from 'next/image';
import {Transition} from '@headlessui/react';

export default function Sign() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [email, setEmail] = useState('');
    const [signingIn, setSigningIn] = useState(true);


    return (
        <div className='bg-[#f0f4f9] h-screen flex justify-center items-center'>
            <div style={{transition: "all 0.6s"}}
                 className={`bg-white w-9/12 rounded-3xl flex space-between ${signingIn ? 'h-3/6' : 'h-4/6'}`}>
                <div className='w-1/2 p-8 flex flex-col'>
                    <Image src={docimg} alt="Docs" width={50} height={50} className='mb-6'/>
                    <h1 className='text-4xl text-black font-["Product_sans"]'>Sign in</h1>
                    <p className='text-black mt-4'>Continue to Docs</p>
                </div>
                <div className='w-1/2 p-4 flex flex-col justify-center items-center'>
                    <div className='flex flex-col flex-1 w-full justify-center mr-6 space-y-4'>
                        <Transition show={!signingIn}
                                    enter="transition-opacity duration-500"
                                    enterFrom="opacity-0"
                                    enterTo="opacity-100"
                                    leave="transition-opacity duration-500"
                                    leaveFrom="opacity-100"
                                    leaveTo="opacity-0"
                        >
                            <input type="email" value={email} onChange={(e) => setEmail(e.target.value)}
                                   className="block text-black border-gray-500 border-2 focus:border-0 w-full px-4 py-4 rounded-lg shadow-md focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-opacity-50"
                                   placeholder="Email"/>
                        </Transition>
                        <input type="text" value={username} onChange={(e) => setUsername(e.target.value)}
                               className="block text-black border-gray-500 border-2 focus:border-0 w-full px-4 py-4 rounded-lg shadow-md focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-opacity-50"
                               placeholder="Username"/>
                        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)}
                               className="block text-black border-gray-500 border-2 focus:border-0 w-full px-4 py-4 rounded-lg shadow-md focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-opacity-50"
                               placeholder="Password"/>
                        <Transition show={!signingIn}
                                    enter="transition-opacity duration-500"
                                    enterFrom="opacity-0"
                                    enterTo="opacity-100"
                                    leave="transition-opacity duration-500"
                                    leaveFrom="opacity-100"
                                    leaveTo="opacity-0"
                        >
                            <input type="password" value={confirmPassword}
                                   onChange={(e) => setConfirmPassword(e.target.value)}
                                   className="block text-black border-gray-500 border-2 focus:border-0 w-full px-4 py-4 rounded-lg shadow-md focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-opacity-50"
                                   placeholder="Confirm Password"/>

                        </Transition>
                    </div>

                    <div className='flex justify-end w-full'>
                        <button onClick={
                            (e) => {
                                setSigningIn(!signingIn);
                            }
                        } className="text-blue-600 bg-white mr-6 self-end text-black px-4 py-2 mb-4">
                            {signingIn ? 'Create an account' : 'Already have an account'}
                        </button>
                        <button onClick={(e) => {
                            console.log(username);
                        }} className="bg-[#0e4eb5] self-end text-white px-4 py-2 mb-4 mr-4 rounded-3xl shadow-md">
                            {signingIn ? 'Sign in' : 'Sign up'}
                        </button>
                    </div>
                </div>
            </div>
        </div>)
}
