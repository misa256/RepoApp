import { Box, Flex, Heading, HStack, Text, Link } from "@chakra-ui/react";
import { FC, memo, useCallback } from "react";
import { useNavigate } from "react-router-dom";

export const Header:FC = memo(() => {
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
  const onClickLogout = useCallback(()=>{
    navigate('/logout', {replace : false})
  }, []);
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
  <Link onClick={onClickLogin}>ログイン</Link>
  <Link onClick={onClickLogout}>ログアウト</Link>
</HStack>
  </Flex>

)
})