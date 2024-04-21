import {useState} from 'react';
import docimg from '../assets/doc_image.png';
import {Transition} from '@headlessui/react';
import {useNavigate} from "react-router-dom";

export default function Sign() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [email, setEmail] = useState('');
    const [signingIn, setSigningIn] = useState(true);
    const navigate = useNavigate();

    return (<div className='bg-[#f0f4f9] h-screen flex justify-center items-center'>
        <div
            className={`transition-all duration-[600ms] bg-white w-9/12 rounded-3xl flex space-between ${signingIn ? 'h-3/6' : 'h-4/6'}`}>
            <div className='w-1/2 p-8 flex flex-col'>
                <img src={docimg} alt="Docs" width={50} height={50} className='mb-6'/>
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
                        <div className='relative'>
                            <input id='email' type="email" value={email} onChange={(e) => setEmail(e.target.value)}
                                   className="peer block text-black border-gray-500 border-2 w-full px-4 py-4 rounded-lg shadow-md focus:outline-none focus:border-blue-600 focus:border-opacity-50"
                                   placeholder=" "/>
                            <label htmlFor="email"
                                   className='absolute text-[17px] text-opacity-80 peer-focus:text-opacity-80 text-sm text-gray-500 duration-300 transform -translate-y-4 scale-75 top-2 z-10 origin-[0] px-4 peer-focus:px-4 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:-translate-y-1/2 peer-placeholder-shown:top-7 cursor-text peer-focus:top-2 peer-focus:scale-75 peer-focus:-translate-y-4 bg-white rounded-sm start-1'>Email</label>
                        </div>
                    </Transition>
                    <div className='relative'>
                        <input id='Username' type="text" value={username} onChange={(e) => setUsername(e.target.value)}
                               className="peer block text-black border-gray-500 border-2 w-full px-4 py-4 rounded-lg shadow-md focus:outline-none focus:border-blue-600 focus:border-opacity-50"
                               placeholder=" "/>
                        <label htmlFor="Username"
                               className='absolute text-[17px] text-opacity-80 peer-focus:text-opacity-80 text-sm text-gray-500 duration-300 transform -translate-y-4 scale-75 top-2 z-10 origin-[0] px-4 peer-focus:px-4 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:-translate-y-1/2 peer-placeholder-shown:top-7 cursor-text peer-focus:top-2 peer-focus:scale-75 peer-focus:-translate-y-4 bg-white rounded-sm start-1'>Username</label>
                    </div>
                    <div className='relative'>
                        <input id='Password' type="password" value={password}
                               onChange={(e) => setPassword(e.target.value)}
                               className="peer block text-black border-gray-500 border-2 w-full px-4 py-4 rounded-lg shadow-md focus:outline-none focus:border-blue-600 focus:border-opacity-50"
                               placeholder=" "/>
                        <label htmlFor="Password"
                               className='absolute text-[17px] text-opacity-80 peer-focus:text-opacity-80 text-sm text-gray-500 duration-300 transform -translate-y-4 scale-75 top-2 z-10 origin-[0] px-4 peer-focus:px-4 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:-translate-y-1/2 peer-placeholder-shown:top-7 cursor-text peer-focus:top-2 peer-focus:scale-75 peer-focus:-translate-y-4 bg-white rounded-sm start-1'>Password</label>
                    </div>
                    <Transition show={!signingIn}
                                enter="transition-opacity duration-500"
                                enterFrom="opacity-0"
                                enterTo="opacity-100"
                                leave="transition-opacity duration-500"
                                leaveFrom="opacity-100"
                                leaveTo="opacity-0"
                    >
                        <div className='relative'>
                            <input type="password" value={confirmPassword}
                                   id={'confirmPassword'}
                                   onChange={(e) => setConfirmPassword(e.target.value)}
                                   className="peer block text-black border-gray-500 border-2 w-full px-4 py-4 rounded-lg shadow-md focus:outline-none focus:border-blue-600 focus:border-opacity-50"
                                   placeholder=" "/>
                            <label htmlFor="confirmPassword"
                                   className='absolute text-[17px] text-opacity-80 peer-focus:text-opacity-80 text-sm text-gray-500 duration-300 transform -translate-y-4 scale-75 top-2 z-10 origin-[0] px-4 peer-focus:px-4 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:-translate-y-1/2 peer-placeholder-shown:top-7 cursor-text peer-focus:top-2 peer-focus:scale-75 peer-focus:-translate-y-4 bg-white rounded-sm start-1'>Confirm
                                Password</label>
                        </div>
                    </Transition>
                </div>

                <div className='flex justify-end w-full'>
                    <button onClick={() => {
                        setSigningIn(!signingIn);
                    }}
                            className="text-blue-600 bg-white mr-6 self-end px-4 py-2 mb-4 rounded-3xl hover:bg-slate-100">
                        {signingIn ? 'Create an account' : 'Already have an account'}
                    </button>
                    <button onClick={() => {
                        console.log(username);
                        navigate('/view')
                        // router.push('/view');
                    }}
                            className="hover:bg-[#0e4eb5] self-end text-white px-4 py-2 mb-4 mr-4 rounded-3xl shadow-md bg-[#0b57d0]">
                        {signingIn ? 'Sign in' : 'Sign up'}
                    </button>
                </div>
            </div>
        </div>
    </div>)
}
