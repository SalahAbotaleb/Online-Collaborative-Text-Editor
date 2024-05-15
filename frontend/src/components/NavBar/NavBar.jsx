import docsIcon from '../../assets/doc_image.png';
import {useNavigate} from "react-router-dom";


export default function NavBar({title, signedin, setsignedin}) {
    const username = localStorage.getItem('username');
    const navigate = useNavigate();
    return (<>
        <div className="sticky top-0 shadow-md z-40 flex justify-between items-center p-4 bg-white">
            <div className="flex items-center gap-4 text-black">
                <img src={docsIcon} alt="Docs" width={40} height={40}/>
                <h1 style={{color: '#5f6368', fontFamily: 'Product Sans'}} className="text-2xl">
                    {title}
                </h1>
            </div>
            <div className="flex items-center gap-4">
                <button
                    onClick={() => {
                        setsignedin(true);
                        fetch('http://localhost:3000/api/auth/logout', {
                            credentials: 'include',
                        }).then(res => {
                            localStorage.removeItem('username');
                            localStorage.removeItem('jwtKey');
                            navigate('/');
                        }).catch(err => {
                            console.log(err);
                        })
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
