import { type } from "@testing-library/user-event/dist/type";
import { createContext, useState } from "react";

// contextの型を定義する。ここで、undefinedが入ることを考慮することが必要。（undefined消すと、コンパイルエラー出る、、）
type UserContextType = {
    accessToken : string | undefined;
    setAccessToken : React.Dispatch<React.SetStateAction<string | undefined>>; 
    userId : number | undefined;
    setUserId : React.Dispatch<React.SetStateAction<number | undefined>>;
    email : string | undefined;
    setEmail : React.Dispatch<React.SetStateAction<string | undefined>>;
    name : string | undefined;
    setName : React.Dispatch<React.SetStateAction<string | undefined>>;
    roles : string | undefined;
    setRoles : React.Dispatch<React.SetStateAction<string | undefined>>  
}
// contextを作成。この時、初期値も設定する。
export const UserContext = createContext<UserContextType>({
    accessToken : '',
    setAccessToken : (email)=>{},
    userId : 0,
    setUserId : (userId)=>{},
    email : '',
    setEmail : (email)=>{},
    name : '',
    setName : (name)=>{},
    roles : '',
    setRoles : (roles)=>{}
});

export const UserProvider = (props : any) => {
 const {children} = props;
 const [accessToken, setAccessToken] = useState<string>();
 const [userId, setUserId] = useState<number>();
 const [email, setEmail] = useState<string>();
 const [name, setName] = useState<string>();
 const [roles, setRoles] = useState<string>();

 return (
     <UserContext.Provider value = {{
         accessToken, setAccessToken, 
         userId, setUserId, 
         email, setEmail, 
         name, setName, 
         roles, setRoles }}>
         {children}
    </ UserContext.Provider>
 )

}