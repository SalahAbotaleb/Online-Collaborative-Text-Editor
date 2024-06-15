import {Dialog, Transition} from '@headlessui/react'
import {Fragment, useState} from 'react'
import InputField from "../../utils/InputField.jsx";

export default function Rename({open, setOpen, file, files, setFiles}) {
    const [newName, setNewName] = useState(file.title)

    function setName() {
        fetch(`https://docscrdt.azurewebsites.net/api/docs/rename/${file.id}`, {
            method: 'PATCH', headers: {
               'Content-Type': 'application/json',
                "Authorization": localStorage.getItem('jwtKey')
            }, body: JSON.stringify({title: newName}),
        }).then(() => {
            setFiles(oldState => oldState.map(f => f.id === file.id ? {...f, title: newName} : f));
        }).catch(err => {
            console.log(err);
        });
        closeModal();
    }

    function closeModal() {
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
                                    Rename
                                </Dialog.Title>
                                <div className="mt-2">
                                    <p className="text-sm text-gray-500">
                                        Please enter a new name for the item:
                                    </p>
                                </div>
                                <InputField value={newName} setValue={setNewName} label='New Name' type='text'/>
                                <div className="mt-4 flex w-full justify-end">
                                    <button
                                        type="button"
                                        className="inline-flex w-20 justify-center rounded-md border border-transparent bg-blue-500 px-4 py-2 text-sm font-medium text-white hover:bg-blue-200 focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-500 focus-visible:ring-offset-2"
                                        onClick={setName}
                                    >
                                        OK
                                    </button>
                                </div>
                            </Dialog.Panel>
                        </Transition.Child>
                    </div>
                </div>
            </Dialog>
        </Transition>)
}
