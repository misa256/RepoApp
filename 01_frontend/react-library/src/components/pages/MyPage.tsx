import { Button, Flex, Wrap, WrapItem, Text, Box } from "@chakra-ui/react";
import userEvent from "@testing-library/user-event";
import axios from "axios";
import { memo, useEffect, useState } from "react";
import ReactPaginate from "react-paginate";
import { getLoggedInUserEmail, getLoggedInUserId, getLoggedInUserName, getLoggedInUserRoles } from "../../hooks/AuthService";
import { Report } from "../../types/api/Report";
import { MyPageUserEditButton } from "../atoms/user/MyPageUserEditButton";
import { MyPageRepoResultCard } from "../molecules/report/ MyPageRepoResultCard";
import { Header } from "../organisms/Header";

export const MyPage = memo(() => {
    const [userId, setUserId] = useState<number | undefined>(Number(getLoggedInUserId()));
    const [userName, setUserName] = useState<string>();
    const [userEmail, setUserEmail] = useState<string>();
    const [userRoles, setUserRoles] = useState<string[]>();
    const [reports, setReports] = useState<Report[]>([]);
    const [reportErrorMessage, setReportErrorMessage] = useState<string | undefined>(undefined);
    
    // 初回マウント時にやること
    useEffect(()=>{
    if(userId != undefined){
        axios.get(`http://localhost:8080/repoApi/user/${userId}`)
        .then((res)=>{
            setUserName(res.data.name);
            setUserEmail(res.data.email);
            setUserRoles(res.data.roles);
        })
        .catch((e)=>{console.log(e);});
        axios.get(`http://localhost:8080/repoApi/repo/user/${userId}/reports`)
        .then((res)=>{setReports(res.data)})
        .catch((e)=>{setReportErrorMessage(e.response.data.message)})
    }
    }, []);


    return(
        <>
        <Header />
        <Box>
        <Box
          p = '4'
          pl = '7'
          mt='1'
          fontWeight='semibold'
          as='h4'
          lineHeight='tight'
          noOfLines={1}
        >
          💛 {userName}
        </Box>
        <MyPageUserEditButton 
        userId = {userId} userName = {userName} userEmail = {userEmail} userRoles = {userRoles}/>
        </Box>
        {reportErrorMessage && <Text color="tomato">{reportErrorMessage}</Text>}
        <Wrap p={7} spacing="30px">
        {reports?.map((report, index)=>{
           return (
           <WrapItem key={index}>
           <MyPageRepoResultCard  report = {report}/>
           </WrapItem>
           )
        })}
        </Wrap>
        </>       
    )
})