import {
  Box,
  Button,
  FormControl,
  FormLabel,
  Input,
  Radio,
  RadioGroup,
  Select,
  Stack,
  Textarea,
  VStack,
  Text,
  Heading,
} from "@chakra-ui/react";
import { type } from "@testing-library/user-event/dist/type";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { ArtistRegister } from "../organisms/contact/ArtistRegister";
import { ContactSubmit } from "../organisms/contact/ContactSubmit";
import { Header } from "../organisms/Header";

export const ContactPage = () => {
  const [category, setCategory] = useState<string>("1");

  return (
    <>
      <Header />
      <Box
        display="flex"
        flexDirection="column"
        justifyContent="space-between"
        alignItems="center"
        pt={10}
      >
        <Heading>Contact</Heading>
        <Text fontWeight="bold" mt={5}>
          どちらを行いますか？
        </Text>
        <RadioGroup onChange={setCategory} value={category} mt={5} mb={10}>
          <Stack direction="row">
            <Radio value="1">アーティスト登録</Radio>
            <Radio value="2">問い合わせ</Radio>
          </Stack>
        </RadioGroup>
      </Box>
      {category === "1" ? <ArtistRegister /> : <ContactSubmit />}
    </>
  );
};
