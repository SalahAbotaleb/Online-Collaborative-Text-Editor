import {Dialog, Transition, Listbox} from '@headlessui/react'
import {Fragment, useState} from 'react'
import ArrowDropDownRoundedIcon from '@mui/icons-material/ArrowDropDownRounded';


export default function Share({open, setOpen, title}) {
    const [user, setUser] = useState('');
    const [permission, setPermission] = useState('view');

    function closeModal() {
        setOpen(false)
    }

    function shareFile() {
        console.log(user, permission)
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
                                className="w-full max-w-md h-80 transform overflow-hidden rounded-2xl bg-white p-6 text-left align-middle shadow-xl transition-all">
                                <Dialog.Title
                                    as="h3"
                                    className="text-lg font-medium leading-6 text-gray-900"
                                >
                                    Share &apos;{title}&apos;
                                </Dialog.Title>
                                <input type="text" value={user} onChange={(e) => setUser(e.target.value)}
                                       className="block text-black border-gray-500 border-2 focus:border-0 w-full px-4 py-4 rounded-lg shadow-md focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-opacity-50 mt-4"
                                       placeholder="username"/>
                                <div className="mt-4 mb-2">
                                    <p className="text-sm text-gray-500">
                                        Permission
                                    </p>
                                </div>
                                <Listbox as='div' value={permission} onChange={setPermission}>
                                    <Listbox.Button className="bg-white ring-1 pl-4 pr-3 py-2 rounded-lg shadow-md">
                                        <span
                                            className="text-[#5f6368] font-['Product_sans'] text-lg">{permission}</span>
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
                                            className="text-[#5f6368] font-['Product_sans'] absolute z-10 mt-1 w-48 p-2 bg-white rounded-md shadow-md text-md">
                                            <Listbox.Option value="view"
                                                            className="rounded-md cursor-pointer p-2 hover:bg-gray-200">
                                                view</Listbox.Option>
                                            <Listbox.Option value="edit"
                                                            className="rounded-md cursor-pointer p-2 hover:bg-gray-200">
                                                Edit
                                            </Listbox.Option>
                                        </Listbox.Options>
                                    </Transition>
                                </Listbox>
                                <div className="mt-10 flex w-full justify-end">
                                    <button
                                        type="button"
                                        className="inline-flex w-20 justify-center rounded-md border border-transparent bg-blue-500 px-4 py-2 text-sm font-medium text-white hover:bg-blue-200 focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-500 focus-visible:ring-offset-2"
                                        onClick={shareFile}
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
