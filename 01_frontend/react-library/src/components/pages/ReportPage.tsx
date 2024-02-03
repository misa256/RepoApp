import { DeleteIcon, EditIcon } from "@chakra-ui/icons";
import { Button, Center, Flex, FormControl, FormLabel, HStack, Input, Stack, Textarea, VStack } from "@chakra-ui/react";
import { useLocation } from "react-router-dom";
import { Report } from "../../types/api/Report";
import { Header } from "../organisms/Header";

export const ReportPage = () => {
    const location = useLocation();
    const report : Report = location.state;
    const { id, date, place, title, text } = report
    // デバッグ用
    // console.log(report);
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
                value={title}
                isReadOnly
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
            value={date}
            isReadOnly
              />
        </FormControl>
        <FormControl>
            <FormLabel fontWeight='bold'>📍 場所</FormLabel>
            <Input
            p = {4}
            w = '600px'
            value={place}
            isReadOnly
              />
        </FormControl>
        </Flex>
        <FormControl>
            <FormLabel fontWeight='bold'>📚 本文</FormLabel>
            <Textarea
                p = {2}
                value={text}
                isReadOnly
                h = '350px'
              />
        </FormControl> 
        <HStack
        spacing = {100}
        >
            <Button 
            colorScheme = "teal"
            leftIcon = {<EditIcon/>}
            >レポートを更新</Button>
            <Button colorScheme = "red"
            leftIcon = {<DeleteIcon />}
            >レポートを削除</Button>
         </HStack> 
        </VStack>
        </VStack>
        </>
    )
}