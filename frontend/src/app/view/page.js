import NavBar from "../NavBar/NavBar";
import DocumnetView from "./DocumentView";

export default function View() {
    return (<>
        <NavBar title='Docs'/>
        <DocumnetView/>
        {/*<NavBar title='document2'/>*/}
    </>);
}
