import {
  Button,
  FormControl,
  FormLabel,
  Input,
  Textarea,
  VStack,
  Text,
  useToast,
} from "@chakra-ui/react";
import { FC, memo } from "react";
import { useForm } from "react-hook-form";
import emailjs from '@emailjs/browser';

type InputData = {
  user: string;
  contactContent: string;
};

export const ContactSubmit: FC = memo(() => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<InputData>({
    mode: "onChange",
  });

  const toast = useToast();

  const contactSubmit = (data: InputData) => {
    const serviceId  = process.env.REACT_APP_EMAILJS_SERVICE_ID;
    const templateId = process.env.REACT_APP_EMAILJS_TEMPLATE_CONTACT_ID;
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
    }
  };
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
        <FormControl width="50%" height="100%">
          <FormLabel fontWeight="bold">問い合わせ内容</FormLabel>
          <Textarea
            {...register("contactContent", {
              required: "問い合わせ内容を入力してください",
              maxLength: {
                value: 500,
                message: "500文字以内で入力してください",
              },
            })}
          />
          <Text color="tomato">{errors.contactContent?.message}</Text>
        </FormControl>
        <Button
          onClick={handleSubmit(contactSubmit)}
          mt={10}
          colorScheme="teal"
        >
          問い合わせ内容を送信する
        </Button>
      </VStack>
      {/* </VStack> */}
    </>
  );
});
