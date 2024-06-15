import {Dialog, Transition} from '@headlessui/react'
import {Fragment, useState} from 'react'
import InputField from "../../utils/InputField.jsx";

export default function CreateDoc({open, setOpen, files, setFiles}) {
    const [title, setTitle] = useState('')


    function closeModal() {
        setOpen(false)
        setTitle('')
    }

    function createFile() {
        fetch('https://docscrdt.azurewebsites.net/api/docs/create', {
            method: 'POST', headers: {
                'Content-Type': 'application/json', "Authorization": localStorage.getItem('jwtKey')
            },
            body: JSON.stringify({
                title: title
            }),
            credentials: 'include',
        }).then(res => res.json()).then(data => {
            setFiles(oldState => [data, ...oldState]);
        }).catch(err => {
            console.log(err);
        });
        setOpen(false)
    }

    return (<Transition appear show={open} as={Fragment}>
        <Dialog as="div" className="relative z-10" onClose={closeModal}>
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
                                Create Document
                            </Dialog.Title>
                            <div className="mt-2">
                                <p className="text-sm text-gray-500">
                                    Create a new document
                                </p>
                                <InputField value={title} setValue={setTitle} label='Title' type='text'/>

                            </div>
                            <div className="mt-4 flex w-full justify-end">
                                <button
                                    type="button"
                                    className="inline-flex justify-center px-4 py-2 text-sm font-medium text-white bg-blue-500 border border-transparent rounded-md hover:bg-blue-600 focus:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-blue-500"
                                    onClick={createFile}
                                >
                                    Create
                                </button>
                            </div>
                        </Dialog.Panel>
                    </Transition.Child>
                </div>
            </div>
        </Dialog>
    </Transition>)
}
