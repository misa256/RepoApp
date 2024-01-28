import { Box } from '@chakra-ui/react'
import { Input } from '@chakra-ui/react'
import { Text } from '@chakra-ui/react'
import { ChangeEvent, FC, useEffect, useState } from 'react'
import { Flex, Spacer } from '@chakra-ui/react'
import { Stack, HStack, VStack } from '@chakra-ui/react'
import { Select } from '@chakra-ui/react'
import axios from 'axios'
import { Button, ButtonGroup } from '@chakra-ui/react'
import { error } from 'console'
import { Artist } from '../../../types/api/Artist'
import { ArtistSearchResultCard } from '../../molecules/artist/ArtistSearchResultCard'

export const ArtistSearch : FC = ()=>{  
    const [countryOptions, setCountryOptions] = useState<string[]>([]);
    const [agencyOptions, setAgencyOptions] = useState<string[]>([]);
    const [artistName, setArtistName] = useState<string>("");
    const [country, setCountry] = useState<string>("");
    const [agency, setAgency] = useState<string>("");
    const [resultArtists, setResultArtists] = useState<Artist[]>([]);
    const [errorMessage, setErrorMessage] = useState<string>("");

    const artistNameChange = (e: ChangeEvent<HTMLInputElement>) =>
    setArtistName(e.target.value);
    const countryChange = (e: ChangeEvent<HTMLSelectElement>) =>
    setCountry(e.target.value);
    const agencyChange = (e: ChangeEvent<HTMLSelectElement>) =>
    setAgency(e.target.value);
    // 検索ボタンを押した時に行う処理(非同期処理)
    const handleSearch = () => {
      axios.get(`http://localhost:8080/api/repo/artist/search?name=${artistName}&agencyName=${agency}&country=${country}&pageNo=&pageSize=&sortBy=&sortDir=`)
      .then(res=>{
        setResultArtists(res.data.content);
        setErrorMessage("");
      })
      .catch(error=>setErrorMessage(error.response.data.message));
    }
// 初回マウント時に行う処理
    useEffect(()=>{
      axios.get("http://localhost:8080/api/repo/agency/getAllCountries")
      .then(res=>setCountryOptions(res.data))
      .catch(error=>console.log(error));

      axios.get("http://localhost:8080/api/repo/agency/getAgencyNames")
      .then(res=>setAgencyOptions(res.data))
      .catch(error=>console.log(error));
    },[])

    
    return(
        <>
        <HStack spacing='40px' mt='60px' mr='20px' ml='20px'>Ï
        {/* アーティスト名入力 */}
        <Box w='30%'>
      <Input
        value={artistName}
        onChange={artistNameChange}
        placeholder='アーティスト名'
      />
        </Box>
        {/* 国名入力 */}
        <Select placeholder='国名' onChange={countryChange} w='30%'>
          {
            countryOptions.map((option,index)=>{
          return(<option key={index} value={option}>{option}</option>)
            })
          }
　　　　　</Select>Ï
          {/* 所属事務所名入力 */}
          <Select placeholder='所属事務所名' onChange={agencyChange} w='30%'>
          {
            agencyOptions.map((option,index)=>{
          return(<option key={index} value={option}>{option}</option>)
            })
          }
　　　　　</Select>Ï
        </HStack>
        <Flex　align="center" justify="center">
        <Button colorScheme='whatsapp' mt="30px" onClick={handleSearch}>検索</Button>
        </Flex>
        {(errorMessage !== "") ?
          <Flex　align="center" justify="center">
          <Text mt="30px" color='tomato'>{errorMessage}</Text>
          </Flex>
        :
        <VStack mt='10px' spacing='10px'>
        {resultArtists.map(artist => {
          return( 
           <Box key={artist.id}>
           <ArtistSearchResultCard 
           id={artist.id}
           name={artist.name} 
           country={artist.talentAgency.country} 
           agencyName={artist.talentAgency.agencyName}
            />
           </Box>
          );
      })}
      </VStack> 
        }
        </>
    )
}