import { ArrowBackIcon, EditIcon } from "@chakra-ui/icons";
import {
  Button,
  Flex,
  FormControl,
  FormLabel,
  HStack,
  Input,
  Text,
  Textarea,
  useToast,
  VStack,
} from "@chakra-ui/react";
import axios from "axios";
import { ChangeEvent, useState } from "react";
import { useForm } from "react-hook-form";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { Report } from "../../types/api/Report";
import { Header } from "../organisms/Header";

type InputData = {
  title : string;
  date : string;
  place : string;
  text : string
}

export const PostReportPage = () => {
  // アーティストID取得
  const { artistId } = useParams();
  const id: number = Number(artistId);
  // トースト
  const toast = useToast();
  // 画面遷移
  const navigate = useNavigate();
  // フォーム管理
  const { register, handleSubmit, formState : {errors}} = useForm<InputData>({mode : "onChange"});
  const onClickPostButton = (inputData : InputData ) => {
    console.log(inputData)
    axios
      .post(`http://localhost:8080/repoApi/repo/artist/${id}/reports`, inputData)
      .then((res) => {
        console.log(res);
        toast({
          title: "登録が完了しました!",
          status: "success",
          duration: 3000,
          isClosable: true,
          position: "top",
        });
      })
      .catch((err) => {
        console.log(err);
        toast({
          title: "登録できません",
          description: err.response.data.message,
          status: "error",
          duration: 9000,
          isClosable: true,
          position: "top",
        });
      });
  };
  return (
    <>
      <Header />
      <VStack>
        <VStack spacing={6} p={6}>
          <FormControl>
            <FormLabel fontWeight="bold">✍️ タイトル</FormLabel>
            <Input
              p={4}
              {...register("title", {
                required : "タイトルは必須です", 
                maxLength : {value : 20, message : "20文字以内で入力してください"}
              })}
            />
            <Text color = 'tomato'>{errors.title?.message}</Text>
          </FormControl>
          <Flex>
            <FormControl mr="30px">
              <FormLabel fontWeight="bold">🗓 日付</FormLabel>
              <Input
                p={4}
                w="600px"
                type = 'date'
                {...register("date", {
                  required : "日付は必須です"
                })}
              />
              <Text color = 'tomato'>{errors.date?.message}</Text>
            </FormControl>
            <FormControl>
              <FormLabel fontWeight="bold">📍 場所</FormLabel>
              <Input
                p={4}
                w="600px"
                {...register("place", {
                  required : "場所は必須です"
                })}
              />
              <Text color = 'tomato'>{errors.place?.message}</Text>
            </FormControl>
          </Flex>
          <FormControl>
            <FormLabel fontWeight="bold">📚 本文</FormLabel>
            <Textarea
              p={2}
              h="300px"
              {...register("text", {
                required : "本文は必須です", 
                minLength : {value : 50, message : "50文字以上入力してください"},
                maxLength : {value : 2000, message : "2000文字以内で入力してください"}
              })}
            />
            <Text color = 'tomato'>{errors.text?.message}</Text>
          </FormControl>
          <HStack spacing={100}>
            <Button
              colorScheme="facebook"
              leftIcon={<ArrowBackIcon />}
              onClick={() => {
                navigate(`/artist/${id}`);
              }}
            >
              戻る
            </Button>
            <Button
              colorScheme="teal"
              leftIcon={<EditIcon />}
              onClick={handleSubmit(onClickPostButton)}
            >
              レポを投稿する
            </Button>
          </HStack>
        </VStack>
      </VStack>
    </>
  );
};
