import {Fragment} from "react";
import {Dialog, Listbox, Transition} from "@headlessui/react";
import ClearRoundedIcon from '@mui/icons-material/ClearRounded';

export default function SharedWith({open, setOpen, shares, docId, setFiles, isViewer}) {
    return <Transition appear show={open} as={Fragment}>
        <Dialog as="div" className="relative z-10" onClose={() => setOpen(false)}>
            <Transition.Child
                as={Fragment}
                enter="ease-out duration-300"
                enterFrom="opacity-0"
                enterTo="opacity-100"
                leave="ease-in duration-200"
                leaveFrom="opacity-100"
                leaveTo="opacity-0"
            >
                <div className="fixed inset-0 bg-black/25"/>
            </Transition.Child>

            <div className="fixed inset-0 overflow-y-auto">
                <div className="flex min-h-full items-center justify-center p-4 text-center">
                    <Transition.Child
                        as={Fragment}
                        enter="ease-out duration-300"
                        enterFrom="opacity-0 scale-95"
                        enterTo="opacity-100 scale-100"
                        leave="ease-in duration-200"
                        leaveFrom="opacity-100 scale-100"
                        leaveTo="opacity-0 scale-95"
                    >
                        <Dialog.Panel
                            className="w-full max-w-md transform overflow-hidden rounded-2xl bg-white p-6 text-left align-middle shadow-xl transition-all">
                            <Dialog.Title
                                as="h3"
                                className="text-lg font-medium leading-6 text-gray-900"
                            >
                                Shared With
                            </Dialog.Title>
                            <div className="flex">
                                {shares.map((share) => {
                                    return <div key={share.username} className={'flex mt-3 mx-1 flex-col items-center'}>
                                        <p className='text-xs text-white bg-blue-500 px-3 rounded-t-xl'>{share.permission}</p>
                                        <div className='bg-blue-500 min-w-24 rounded-3xl p-2 flex justify-between items-center space-x-2'>
                                            <p className='text-md text-white text-center flex-grow'>{share.username}</p>
                                            {!isViewer && <button onClick={() => {
                                                fetch(`https://docscrdt.azurewebsites.net/api/docs/users/remove/${docId}`, {
                                                    method: 'DELETE', headers: {
                                                        'Content-Type': 'application/json',
                                                        "Authorization": localStorage.getItem('jwtKey')
                                                    }, body: JSON.stringify({
                                                        username: share.username, permission: share.permission
                                                    }),
                                                }).then(data => {
                                                    setFiles(oldState => oldState.map(file => {
                                                        if (file.id === docId) {
                                                            return {
                                                                ...file,
                                                                sharedWith: file.sharedWith.filter(s => s.username !== share.username)
                                                            }
                                                        }
                                                        return file;
                                                    }));
                                                }).catch(err => {
                                                    console.log(err);
                                                })
                                            }}
                                                    className='bg-white rounded-2xl w-5 h-5 flex items-center justify-center'>
                                                <ClearRoundedIcon sx={{color: '#5f6368', fontSize: '15px'}}/>
                                            </button>}
                                        </div>
                                    </div>
                                })}
                            </div>

                        </Dialog.Panel>
                    </Transition.Child>
                </div>
            </div>
        </Dialog>
    </Transition>
}
