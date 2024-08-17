import { Box, Flex, Heading, HStack, Text, Link, useDisclosure, Button, Modal, ModalOverlay, ModalContent, ModalCloseButton, ModalHeader, ModalFooter, ModalBody } from "@chakra-ui/react";
import { FC, memo, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { isUserLoggedIn, logout } from "../../hooks/AuthService";

export const Header:FC = memo(() => {
  const isAuthenticated = isUserLoggedIn();
  const { isOpen, onOpen, onClose } = useDisclosure()
  const navigate = useNavigate();
  const onClickHome = useCallback(()=>{
    navigate('/',{replace : true})
  }, []);
  const onClickAgencyPage = useCallback(()=>{
    navigate('/agency', {replace : false})
  }, []);
  const onClickLogin = useCallback(()=>{
    navigate('/login', {replace : false})
  }, []);
  const onClickMyPage = useCallback(()=>{
    navigate('/myPage', {replace : false})
  }, [])
return(
  <Flex
  as = 'header'
  justify = 'space-between'
  _hover={{ cursor: "pointer" }}
  pt = '10px'
  ml = '20px'
  mr = '20px'
  align="center"
  >
    <Flex
    as = 'a'
    align = 'center'
    onClick = {onClickHome}
    >
    <Heading as='h2'　color='red.200'>
      れぽぽ
    </Heading>
    </Flex>
<HStack 
spacing = '40px'
>
  <Link onClick={onClickAgencyPage}>事務所一覧</Link>
  {isAuthenticated && <Link onClick = {onClickMyPage}>マイページ</Link>}
  {isAuthenticated || <Link onClick={onClickLogin}>ログイン</Link>}
  {isAuthenticated &&
  <>
  <Link onClick = {onOpen}>ログアウト</Link>
  <Modal isOpen={isOpen} onClose={onClose}>
    <ModalOverlay />
    <ModalContent>
    <ModalBody>
        <Text>本当にログアウトしますか？</Text>    
    </ModalBody>
      <ModalCloseButton />
      <ModalFooter>
        <Button colorScheme='blue' mr={3} onClick={onClose}>
          閉じる
        </Button>
        <Button variant='ghost'onClick = {logout}>ログアウト</Button>
      </ModalFooter>
    </ModalContent>
  </Modal>
  </> 
  }
</HStack>
  </Flex>

)
})