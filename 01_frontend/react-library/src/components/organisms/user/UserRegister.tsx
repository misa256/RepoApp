import { ViewIcon } from "@chakra-ui/icons";
import { Box, FormControl, FormLabel, Input, VStack, Text, Stack, Checkbox, Flex, Button, CheckboxGroup, InputGroup, InputRightElement, useToast } from "@chakra-ui/react";
import axios from "axios";
import { useState } from "react";
import { Controller, useForm } from "react-hook-form"
import { getLoggedInUserRoles, isUserLoggedIn } from "../../../hooks/AuthService";

type RegisterUser = {
    email : string;
    name : string;
    password : string;
    roles : string[]
}


export const UserRegister = () => {
  // フォーム管理
    const {register,
        formState: { errors },
        handleSubmit,
        control} = useForm<RegisterUser>({
            mode: "onChange",
            defaultValues : {
                email : '',
                name : '',
                password : '',
                roles : ["USER"]
            }
          });
    // エラーメッセージ
    const [errorMessage, setErrorMessage] = useState<string>();
    // トースト
    const toast = useToast();
    // 登録ボタン押下
    const onClickRegisterButton = (data : RegisterUser) => {
        axios.post('http://localhost:8080/repoApi/user', data)
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
        .catch((e)=>{
            setErrorMessage(e.response.data.message);
        })
    };
    // 権限
    const rolesOptions = ["ADMIN", "USER"];
    // パスワード表示・非表示切り替え
    const [showPassword, setShowPassword] = useState<boolean>(false);
    const onClickShowPasswordButton = () => setShowPassword(!showPassword);
    

    return (
        <Box maxW="md" mx="auto" mt={10} p={5} borderWidth="1px" borderRadius="md" boxShadow="md">
        <VStack spacing={4}>
          <FormControl id="email">
            <FormLabel>ユーザーID(emailアドレス)</FormLabel>
            <Input
              type="email"
              {...register("email", {
                required : "ユーザーID（emailアドレス）を入力してください。",
                pattern : {value : /^[a-zA-Z0-9_.+-]+@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]*\.)+[a-zA-Z]{2,}$/, message : "email形式で入力してください。"}
              })}
            />
            <Text color="tomato">{errors.email?.message}</Text>
          </FormControl>
          <FormControl id="name">
            <FormLabel>ユーザーネーム</FormLabel>
            <Input
              type="text"
              {...register("name", {
                required : "ユーザーネームを入力してください。",
                maxLength : {value : 255, message : "255文字以内で入力してください"}
              })}
            />
            <Text color="tomato">{errors.name?.message}</Text>
          </FormControl>
          <FormControl id="password">
            <FormLabel>パスワード</FormLabel>
            <InputGroup>
            <Input
              type = {showPassword ? "text" : "password"}
              {...register("password", {
                required : "パスワードを入力してください。",
                minLength : {value : 8, message : "8文字以上で入力してください"},
                maxLength : {value : 24, message : "24文字以内で入力してください"}
              })}
                />
                <InputRightElement>
                <ViewIcon onClick = {onClickShowPasswordButton}/>
                </InputRightElement>
                </InputGroup>
                <Text color="tomato">{errors.password?.message}</Text>
          </FormControl>
           {isUserLoggedIn() && getLoggedInUserRoles()?.includes("ADMIN") &&
           <FormControl id="roles">
           <FormLabel>権限</FormLabel>
           <Controller name = "roles" control = {control}
           render = {
               ({field : { onChange, value }}) => {
                   return(
                      <CheckboxGroup onChange = {onChange} value = {value}>
                     {rolesOptions.map((role)=>{
                       return(<Checkbox value = {role} key = {role}>
                         {role}
                         </Checkbox>)   
                     })}
                      </CheckboxGroup>
                   )
               }
           }
           rules = {{required : "少なくとも１つは選択してください。"}}
           />
           <Text color="tomato">{errors.roles?.message}</Text>
         </FormControl>
           }   
          {errorMessage && 
        <Flex align="center" justify="center">
        <Text mt="30px" color="tomato">
          {errorMessage}
        </Text>
      </Flex>
      }
          <Button onClick = {handleSubmit(onClickRegisterButton)} colorScheme="teal" width="full">
            登録する
          </Button>
        </VStack>
    </Box>
    )
}