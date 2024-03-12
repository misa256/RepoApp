import {
  Button,
  FormControl,
  FormLabel,
  Input,
  VStack,
  Text,
  useToast,
} from "@chakra-ui/react";
import emailjs from '@emailjs/browser';
import { FC, memo, useState } from "react";
import { useForm } from "react-hook-form";

type InputData = {
  user: string;
  artistName: string;
  agencyName: string;
};

export const ArtistRegister: FC = memo(() => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<InputData>({
    mode: "onChange",
  });
  
  const toast = useToast();

  const artistRegister = (data: InputData) => {
    const serviceId  = process.env.REACT_APP_EMAILJS_SERVICE_ID;
    const templateId = process.env.REACT_APP_EMAILJS_TEMPLATE_ARTISTREG_ID;
    const publicId = process.env.REACT_APP_EMAILJS_PUBLIC_ID;
    if(serviceId == undefined || templateId == undefined || publicId == undefined){
      console.log("serviceId, templateId, publicIdのいずれかがundefinedになっています。環境変数を設定してください。 ");
    }else{
      // elseの中は非同期処理についてちゃんと学んでから書き直す！カスタムフックにもしたい。
      const promise = new Promise((resolve, reject)=>{
        resolve(emailjs.send(serviceId, templateId, data, publicId));
      });
      toast.promise(promise, {
        success: { title: 'Promise resolved', description: 'Looks great' },
        error: { title: 'Promise rejected', description: 'Something wrong' },
        loading: { title: 'Promise pending', description: 'Please wait' },
      });
  }};

  return (
    <>
      {/* <VStack> */}
      <VStack spacing={6} p={6}>
        <FormControl width="50%">
          <FormLabel fontWeight="bold">ユーザー</FormLabel>
          <Input
            type="text"
            {...register("user", {
              required: "ユーザー名を入力してください",
            })}
          />
          <Text color="tomato">{errors.user?.message}</Text>
        </FormControl>
        <FormControl width="50%">
            <Text>👇登録を依頼したいアーティスト名と所属事務所を入力してください。</Text>
          　<FormLabel fontWeight="bold">アーティスト名</FormLabel>
          <Input
            type="text"
            {...register("artistName", {
              required: "アーティスト名を入力してください",
            })}
          />
          <Text color="tomato">{errors.artistName?.message}</Text>
        </FormControl>
        <FormControl width="50%">
          <FormLabel fontWeight="bold">事務所名</FormLabel>
          <Input
            type="text"
            {...register("agencyName", {
              required: "事務所名を入力してください",
            })}
          />
          <Text color="tomato">{errors.agencyName?.message}</Text>
        </FormControl>
        <Button
          onClick={handleSubmit(artistRegister)}
          mt={10}
          colorScheme="teal"
        >
          アーティスト登録依頼を送信する
        </Button>
      </VStack>
      {/* </VStack> */}
    </>
  );
});
