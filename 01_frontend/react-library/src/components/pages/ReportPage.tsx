import { ArrowBackIcon, DeleteIcon, EditIcon } from "@chakra-ui/icons";
import {
  Button,
  Flex,
  FormControl,
  FormLabel,
  HStack,
  Input,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalOverlay,
  Text,
  Textarea,
  useDisclosure,
  useToast,
  VStack,
} from "@chakra-ui/react";
import axios from "axios";
import { useForm } from "react-hook-form";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { getLoggedInUserEmail } from "../../hooks/AuthService";
import { Report } from "../../types/api/Report";
import { Header } from "../organisms/Header";

type updatedData = {
  newTitle : string;
  newDate : string;
  newPlace : string;
  newText : string
}

export const ReportPage = () => {
  // report情報を取得
  const location = useLocation();
  const report: Report = location.state;
  const { id, date, place, title, text, userName, userEmail } = report;
  // アーティストIDを取得
  const { artistId } = useParams();
  const numberArtistId: number = Number(artistId);
  // トースト
  const toast = useToast();
  // 画面遷移
  const navigate = useNavigate();
  // モーダル用
  const { onOpen, isOpen, onClose } = useDisclosure();
  // フォーム管理
  const { register, handleSubmit, formState : {errors}} = useForm<updatedData>({
    mode : "onChange",
    defaultValues : {newTitle : title, newDate : date, newPlace : place, newText : text}
  });
  const onClickUpdateButton = (updatedData : updatedData) => {
    axios
      .put(
        `http://localhost:8080/repoApi/repo/artist/${numberArtistId}/reports/${id}`,
        { place: updatedData.newPlace, 
          date: updatedData.newDate, 
          title: updatedData.newTitle, 
          text: updatedData.newText }
      )
      .then((res) => {
        toast({
          title: "更新が完了しました!",
          status: "success",
          duration: 3000,
          isClosable: true,
          position: "top",
        });
      })
      .catch((err) => {
        console.log(err);
        toast({
          title: "更新できません",
          description: err.response.data.message,
          status: "error",
          duration: 9000,
          isClosable: true,
          position: "top",
        });
      });
  };
  const onClickDeleteButton = () => {
    axios
      .delete(
        `http://localhost:8080/repoApi/repo/artist/${numberArtistId}/reports/${id}`
      )
      .then((res) => {
        onClose();
        toast({
          title: "削除されました",
          status: "success",
          duration: 3000,
          isClosable: true,
          position: "top",
        });
      })
      .catch((err) => {
        onClose();
        console.log(err);
        toast({
          title: "削除できません",
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
              {...register("newTitle", {
              required : "タイトルは必須です", 
              maxLength : {value : 20, message : "20文字以内で入力してください"}})}
            />
            <Text color = 'tomato'>{errors.newTitle?.message}</Text>
          </FormControl>
          <Flex>
            <FormControl mr="30px">
              <FormLabel fontWeight="bold">🗓 日付</FormLabel>
              <Input
                p={4}
                w="600px"
                type = 'date'
                {...register("newDate", {
                  required : "日付は必須です"
                })}
              />
              <Text color = 'tomato'>{errors.newDate?.message}</Text>
            </FormControl>
            <FormControl>
              <FormLabel fontWeight="bold">📍 場所</FormLabel>
              <Input
                p={4}
                w="600px"
                {...register("newPlace", {
                  required : "場所は必須です"
                })}
              />
              <Text color = 'tomato'>{errors.newPlace?.message}</Text>
            </FormControl>
          </Flex>
          <FormControl>
            <FormLabel fontWeight="bold">📚 本文</FormLabel>
            <Textarea
              p={2}
              h="350px"
              {...register("newText", {
                required : "本文は必須です", 
                minLength : {value : 50, message : "50文字以上入力してください"},
                maxLength : {value : 2000, message : "2000文字以内で入力してください"}
              })}
            />
            <Text color = 'tomato'>{errors.newText?.message}</Text>
          </FormControl>
          <HStack spacing={100}>
          <Button
              colorScheme="facebook"
              leftIcon={<ArrowBackIcon />}
              onClick={() => {
                navigate(-1);
              }}
            >
              戻る
            </Button>
            {/* ログインユーザーとレポートを作成したユーザーが同じ場合、表示する */}
            {userEmail === getLoggedInUserEmail() &&
            <>
            <Button
            colorScheme="teal"
            leftIcon={<EditIcon />}
            onClick={handleSubmit(onClickUpdateButton)}
          >
            レポートを更新
          </Button>
           <Button
           colorScheme="red"
           leftIcon={<DeleteIcon />}
           onClick={onOpen}
         >
           レポートを削除
         </Button>
         <Modal isOpen={isOpen} onClose={onClose} motionPreset='slideInTop'>
           <ModalOverlay />
           <ModalContent>
             <ModalBody>
               <Text fontWeight='bold' m = {4}>本当に削除しますか？</Text>
             </ModalBody>
             <ModalFooter>
               <Button colorScheme="gray" mr={3} onClick={onClose}>
                 キャンセル
               </Button>
               <Button colorScheme="red" onClick={onClickDeleteButton}>削除する</Button>
             </ModalFooter>
           </ModalContent>
         </Modal>
         </>
            }           
        </HStack>
        </VStack>
      </VStack>
    </>
  );
};
