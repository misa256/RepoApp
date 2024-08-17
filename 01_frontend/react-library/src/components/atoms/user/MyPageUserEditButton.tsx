import { Button, FormControl, FormLabel, Input, Modal, ModalBody, ModalCloseButton, ModalContent, ModalFooter, ModalHeader, ModalOverlay, useDisclosure, Text, CheckboxGroup, Checkbox, useToast } from "@chakra-ui/react"
import { type } from "@testing-library/user-event/dist/type"
import axios from "axios"
import { memo, useEffect, useState } from "react"
import { Controller, useForm } from "react-hook-form"
import { getLoggedInUserRoles, isUserLoggedIn } from "../../../hooks/AuthService"

type UserInfo = {
    userId : number | undefined;
    userName : string | undefined;
    userEmail : string | undefined;
    userRoles : string[] | undefined
}

type RequestUserInfo = {
  newUserName : string;
  newUserEmail : string;
  newUserRoles : string[]
}

export const MyPageUserEditButton = memo((props : UserInfo) => {
    const { userId, userName ,userEmail, userRoles } = props; 
    console.log(userId);
    console.log(typeof(userId));
     console.log(userName);
     console.log(userEmail);
     console.log(userRoles);
    const { isOpen, onOpen, onClose } = useDisclosure();
    const rolesOptions = ["ADMIN", "USER"];
    // フォーム管理
  const { register, handleSubmit, formState : {errors}, control, reset} = useForm<RequestUserInfo>({
    mode : "onChange",
    // defaultValuesの値は初回レンダリング時のみに表示される
    defaultValues : { newUserName : userName, 
      newUserEmail : userEmail, 
      newUserRoles : userRoles }
  });

  const toast = useToast();
  const [errorMessage, setErrorMessage] = useState<string | undefined>(undefined);

  // propsが更新された時にdefaultValuesを更新
  useEffect(()=>{
    console.log("useEffect実行")
    reset({ newUserName : userName, 
      newUserEmail : userEmail, 
      newUserRoles : userRoles })
  }, [userId, userName, userEmail, userRoles, reset])

  const onClickUpdateButton = (requestUserInfo : RequestUserInfo)=>{
    axios.put(`http://localhost:8080/repoApi/user/${userId}`, {
      "email" : requestUserInfo.newUserEmail,
      "name" : requestUserInfo.newUserName,
      "roles" : requestUserInfo.newUserRoles
    })
    .then(()=>{
      toast({
        title: "更新が完了しました!",
        description : "ユーザーID(emailアドレス)を更新した場合、一度ログアウトし、ログインし直してください。",
        status: "success",
        duration: 5000,
        isClosable: true,
        position: "top",
      });
    })
    .catch((e)=>{
       setErrorMessage(e.response.data.message);
    })

  }
    return (
        <>
        <Button m = '4' ml = '7' borderRadius='md' colorScheme='blue' onClick={onOpen}>ユーザー情報の編集</Button>
        <Modal
        isOpen={isOpen}
        onClose={onClose}
      >
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>ユーザー情報</ModalHeader>
          <ModalCloseButton />
          <ModalBody pb={6}>
            <FormControl>
              <FormLabel>ユーザーID(emailアドレス)</FormLabel>
              <Input 
              type="email"
              {...register("newUserEmail", {
                required : "ユーザーID（emailアドレス）を入力してください。",
                pattern : {value : /^[a-zA-Z0-9_.+-]+@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]*\.)+[a-zA-Z]{2,}$/, message : "email形式で入力してください。"}
              })}
              />
               <Text color="tomato">{errors.newUserEmail?.message}</Text>
            </FormControl>

            <FormControl mt={4}>
              <FormLabel>ユーザー名</FormLabel>
              <Input 
               type="text"
               {...register("newUserName", {
                 required : "ユーザーネームを入力してください。",
                 maxLength : {value : 255, message : "255文字以内で入力してください"}
               })}
              />
              <Text color="tomato">{errors.newUserName?.message}</Text>
            </FormControl>

            {isUserLoggedIn() && getLoggedInUserRoles()?.includes("ADMIN") &&
            <FormControl mt={4}>
              <FormLabel>権限</FormLabel>
              <Controller name = "newUserRoles" control = {control}
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
           <Text color="tomato">{errors.newUserRoles?.message}</Text>
            </FormControl>
            }
          {errorMessage && <Text color="tomato">{errorMessage}</Text>}
          </ModalBody>

          <ModalFooter>
            <Button colorScheme='blue' mr={3} onClick={handleSubmit(onClickUpdateButton)}>
              更新する
            </Button>
            <Button onClick={onClose}>閉じる</Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
        </>
    )
})