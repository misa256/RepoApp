import { Box, Button, FormControl, FormLabel, Input, VStack, Text, Flex } from "@chakra-ui/react"
import { type } from "@testing-library/user-event/dist/type";
import axios from "axios";
import { useContext, useState } from "react";
import { useForm } from "react-hook-form";
import { Link, NavLink, useLocation, useNavigate } from "react-router-dom";
import { saveLoggedInUserEmail, saveLoggedInUserId, saveLoggedInUserName, saveLoggedInUserRoles, storeToken } from "../../../hooks/AuthService";


type LoginUser = {
  "loginEmail" : string;
  "loginPassword" : string
}

export const LoginForm = () => {
  // フォーム管理
    const {
      register,
      formState: { errors },
      handleSubmit
    } = useForm<LoginUser>({
      mode: "onChange",
    })
    // 画面遷移
    const navigate = useNavigate();　
    const location = useLocation();
    const from = location?.state?.from?.pathname || '/';
    const linkStyle = {
      textDecoration: 'underline'
    };
    // エラーメッセージ
    const [errorMessage, setErrorMessage] = useState<string>();
    //ログインボタン押した時の動き
    const onClickLoginButton = async (data : LoginUser) => {
       await axios.post('http://localhost:8080/repoApi/auth/login', data)
        .then((res)=>{
            const token = 'Bearer ' + res.data.accessToken;
            //  作成したtokenをローカルストレージに保存
             storeToken(token);
            //  取得したユーザー情報をセッションストレージに保存
             saveLoggedInUserId(res.data.userId);
             saveLoggedInUserEmail(res.data.email);
             saveLoggedInUserName(res.data.name);
             saveLoggedInUserRoles(res.data.roles);
            // 元いたページに戻る
            navigate(from, { replace: true });
       })
       .catch((e)=>{
        setErrorMessage(e.response.data.message);
            console.log(e)
       })
    }


    return(
        <Box maxW="md" mx="auto" mt={10} p={5} borderWidth="1px" borderRadius="md" boxShadow="md">
          <VStack spacing={4}>
            <FormControl id="email">
              <FormLabel>ユーザーネーム</FormLabel>
              <Input
                type="email"
                {...register("loginEmail", {
                  required : "ユーザーID（emailアドレス）を入力してください。"
                })}
              />
              <Text color="tomato">{errors.loginEmail?.message}</Text>
            </FormControl>
            <FormControl id="password">
              <FormLabel>パスワード</FormLabel>
              <Input
                type="password"
                {...register("loginPassword", {
                  required : "パスワードを入力してください。"
                })}
              />
              <Text color="tomato">{errors.loginPassword?.message}</Text>
            </FormControl>
            {errorMessage && 
          <Flex align="center" justify="center">
          <Text mt="30px" color="tomato">
            {errorMessage}
          </Text>
        </Flex>
        }
        <Link to = {'/user/register'} style = {linkStyle}>新規登録はこちら</Link>
            <Button onClick = {handleSubmit(onClickLoginButton)} colorScheme="teal" width="full">
              ログイン
            </Button>
          </VStack>
      </Box>
  
    )
}