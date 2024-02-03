import { EditIcon } from "@chakra-ui/icons"
import { Button, Flex, FormControl, FormLabel, Input, Textarea, VStack } from "@chakra-ui/react"
import axios from "axios";
import { ChangeEvent, useState } from "react";
import { useLocation, useParams } from "react-router-dom";
import { Report } from "../../types/api/Report";
import { Header } from "../organisms/Header"

export const PostReportPage = () => {
    const {artistId} = useParams();
    const id : number = Number(artistId);
    const [title, setTitle] = useState<string>();
    const [date, setDate] = useState<string>();
    const [place, setPlace] = useState<string>();
    const [text, setText] = useState<string>();

    const onClickPostButton = () => {
        axios.post(`http://localhost:8080/api/repo/artist/${id}/reports`, {title, date, place, text})
        .then( res => console.log(res))
        .catch( error => console.log(error))
    }
    return(
        <>
        <Header />
        <VStack>
        <VStack 
         spacing = {6}
         p = {6}
        >
            <FormControl>
            <FormLabel fontWeight='bold'>✍️ タイトル</FormLabel>
            <Input
                 p = {4}
                 onChange = {(e: ChangeEvent<HTMLInputElement>) => setTitle(e.target.value)}
              />
        </FormControl>
            <Flex>
        <FormControl
        mr = '30px'
        >
            <FormLabel fontWeight='bold'>🗓 日付</FormLabel>
            <Input
            p = {4}
            w = '600px'
            onChange = {(e: ChangeEvent<HTMLInputElement>) => setDate(e.target.value)}
              />
        </FormControl>
        <FormControl>
            <FormLabel fontWeight='bold'>📍 場所</FormLabel>
            <Input
            p = {4}
            w = '600px'
            onChange = {(e: ChangeEvent<HTMLInputElement>) => setPlace(e.target.value)}
              />
        </FormControl>
        </Flex>
        <FormControl>
            <FormLabel fontWeight='bold'>📚 本文</FormLabel>
            <Textarea
                p = {2}
                h = '300px'
                onChange = {(e : ChangeEvent<HTMLTextAreaElement>) => setText(e.target.value)}
              />
        </FormControl> 
        <Button
        colorScheme = "teal"
        leftIcon = {<EditIcon />}
        onClick = {onClickPostButton}
        >レポを投稿する</Button> 
        </VStack>
        </VStack>
        </>
    )
}