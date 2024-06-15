import docsIcon from '../../assets/doc_image.png';
import {useNavigate} from "react-router-dom";
import {useState} from "react";

import Popover from '@mui/material/Popover';


export default function NavBar({title, signedin, setsignedin, usernames}) {
    const username = localStorage.getItem('username');
    const navigate = useNavigate();

    const [anchorEl, setAnchorEl] = useState(null);

    const open = Boolean(anchorEl);
    const id = open ? 'simple-popover' : undefined;

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    }

    const handleClose = () => {
        setAnchorEl(null);
    }
    
    return (<>
        <div className="sticky top-0 shadow-md z-40 flex justify-between items-center p-4 bg-white">
            <div className="flex items-center gap-4 text-black">
                <img src={docsIcon} alt="Docs" width={40} height={40}/>
                <h1 style={{color: '#5f6368', fontFamily: 'Product Sans'}} className="text-2xl">
                    {title}
                </h1>
            </div>
            <div className="flex items-center gap-4">
                {usernames && <>
                    <button aria-describedby={id} onClick={handleClick} className="hover:bg-[#0e4eb5] text-white px-4 py-2 rounded-3xl shadow-md bg-[#0b57d0]">
                        Active Users {usernames.length}
                    </button>
                    <Popover
                        id={id}
                        open={open}
                        anchorEl={anchorEl}
                        onClose={handleClose}
                        anchorOrigin={{
                            vertical: 'bottom',
                            horizontal: 'left',
                        }}
                    >
                        <div className='flex flex-col p-4'>
                            {usernames.map((user, index) => {
                                return <p key={index} className='text-[#5f6368] font-["Product_sans"] text-lg font-bold'>{user}</p>

                            })}
                        </div>
                    </Popover></>}
                <button
                    onClick={() => {
                        setsignedin(true);
                        
                        localStorage.removeItem('username');
                        localStorage.removeItem('jwtKey');
                        navigate('/');
                        
                    }}
                    className="hover:bg-[#0e4eb5] text-white px-4 py-2 rounded-3xl shadow-md bg-[#0b57d0]"
                >
                    Sign out
                </button>
                <p className="text-[#5f6368] font-['Product_sans'] text-lg font-bold">{username}</p>
            </div>
        </div>
    </>)
}
