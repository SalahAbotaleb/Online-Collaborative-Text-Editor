import docsIcon from '../assets/doc_image.png';
import Image from 'next/image';


export default function NavBar({title}) {
    return (<>
        <div className="flex justify-between items-center p-4 bg-white">
            <div className="flex items-center gap-4 text-black">
                <Image src={docsIcon} alt="Docs" width={40} height={40}/>
                <h1 style={{color: '#5f6368', fontFamily: 'Product Sans'}} className="text-2xl">
                    {title}
                </h1>
            </div>
        </div>
    </>)
}
