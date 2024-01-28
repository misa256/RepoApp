import { Box, Button, Flex, HStack, Input, Select, Text, Wrap, WrapItem } from "@chakra-ui/react"
import axios from "axios";
import { ChangeEvent, useState, FC, useEffect } from "react";
import { Report } from "../../../types/api/Report";
import { RepoSearchResultCard } from "../../molecules/report/RepoSearchResultCard";

type props = {
    id : number;
}

export const RepoSearch:FC<props> = (props) => {
    const { id } = props;
    const [date, setDate] = useState<string>("");
    const [place, setPlace] = useState<string>("");
    const [title, setTitle] = useState<string>("");
    const [places, setPlaces] = useState<string[]>([]);
    const [reports, setReports] = useState<Report[]>([]);
    const [errorMessage, setErrorMessage] = useState<string>();
    const placeChange = (e: ChangeEvent<HTMLSelectElement>) =>
    setPlace(e.target.value);
    const titleChange = (e: ChangeEvent<HTMLInputElement>) =>
    setTitle(e.target.value);
    const dateChange = (e: ChangeEvent<HTMLInputElement>) =>
    setDate(e.target.value);
    const handleSearch = ()=>{
      if(errorMessage){
        setErrorMessage(undefined);
      }
        axios
    .get(`http://localhost:8080/api/repo/artist/${id}/reports/search?pageNo=&pageSize=&sortBy=&sortDir=&place=${place}&date=${date}&title=${title}`)
    .then(res=> setReports(res.data.content))
    .catch(error=>{setErrorMessage(error.response.data.message);
    console.log(error);})
    }
    useEffect(()=>{
      // レポートの場所を取得
      axios.get(`http://localhost:8080/api/repo/artist/${id}/reports/places`)
      .then(res=>setPlaces(res.data))
    .catch(error=>console.log(error));
    // 特定のアーティストのレポート全件取得
    axios.get(`http://localhost:8080/api/repo/artist/${id}/reports`)
    .then(res=>setReports(res.data.content))
    .catch(error=>setErrorMessage(error.response.data.message))
    },[id])
    return(
        <>
        <HStack spacing='40px' mt='10px' mr='20px' ml='20px'>Ï
        {/* 日付入力 */}
        <Box w='30%'>
      <Input
      type = 'date'
        value={date}
        onChange={dateChange}
        placeholder='日付'
      />
        </Box>
        {/* 場所入力 */}
        <Select placeholder='場所' onChange={placeChange} w='30%'>
          {
            places.map((option,index)=>{
          return(<option key={index} value={option}>{option}</option>)
            })
          }
　　　　　</Select>Ï
          {/* タイトル入力 */}
          <Input 
          placeholder='タイトル' 
          value={title} 
          onChange={titleChange} 
          w='30%' />
        </HStack>
        <Flex　align="center" justify="center">
        <Button colorScheme='whatsapp' mt="30px" onClick={handleSearch}>検索</Button>
        </Flex>
        {(errorMessage) ? 
      <Flex　align="center" justify="center">
      <Text mt="30px" color='tomato'>{errorMessage}</Text>
      </Flex>
      :
      <Wrap 
      p={7}
      spacing = '30px'
      >
        {reports.map((report, index)=>{
          return(
            <WrapItem key = {index}>
              <RepoSearchResultCard
              report = {report}
              artistId = {id}
              />
            </WrapItem>
          );
        })}
      </Wrap>
      }
        </>
    )
}