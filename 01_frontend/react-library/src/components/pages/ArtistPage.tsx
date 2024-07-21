import { Box, Button, Flex, HStack, Text } from "@chakra-ui/react"
import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {Artist} from "../../types/api/Artist"
import { Header } from "../organisms/Header";
import { EditIcon } from "@chakra-ui/icons";
import { RepoSearch } from "../organisms/report/RepoSearch";

export const ArtistPage = () => {
    const { artistId } = useParams();
    const id : number = Number(artistId);
    const navigate = useNavigate();
    const [artist, setArtist] = useState<Artist>();
    const [errorMessage, setErrorMessage] = useState<string>("");
    const onClickPostButton = () => {
        navigate(`/artist/${id}/reports`)
    }
    useEffect(()=>{
        axios.get(`http://localhost:8080/repoApi/artist/${id}`)
        .then(res=>setArtist(res.data))
        .catch(error => setErrorMessage(error.response.data.message));
    }, []);
    
    return (
        <>
        <Header />
        {(errorMessage !== "") ? 
        <Flex　align="center" justify="center">
        <Text mt="30px" color='tomato'>{errorMessage}</Text>
        </Flex>
        :
        <>
        <Flex
        align = 'center'
        justify = 'space-between'
        m = {80}
        mt = {10}
        mb = {10}
        bg="white"
        borderRadius="10px"
        shadow="md"
         p = {4}
        >
            <Box>
    <Text fontSize="lg" fontWeight="bold">{artist?.name}</Text>
            </Box>
            <Button 
            colorScheme = "teal"
            leftIcon = {<EditIcon />}
            onClick = {onClickPostButton}
            >
                レポを投稿する
                </Button>
        </Flex>
        <RepoSearch id={id}/>
        </>
       }
         </>
    )
}