import { Box, HStack, List, ListItem, VStack, Text } from "@chakra-ui/react";
import { FC, memo } from "react";
import { NavLink } from "react-router-dom";

export const Footer: FC = memo(() => {
  return (
    <>
      <Box
        as="footer"
        bgColor = "teal.100"
        textAlign="center"
        // position = "fixed"
        // bottom = {0}
        // width = "100%"
        py={4}
        position = "sticky"
        top = "100vh"
      >
        <HStack 
        spacing = {100}
        justify = "center"
        alignItems = "center"
        >
          <VStack>
            <Text as="b">ABOUT</Text>
            <List>
              <ListItem>
                <NavLink to="/admin">管理者について</NavLink>
              </ListItem>
            </List>
          </VStack>
          <VStack>
            <Text as="b">CONTACT</Text>
            <List>
              <ListItem>
                <NavLink to="/contact">お問合せ</NavLink>
              </ListItem>
            </List>
          </VStack>
        </HStack>
        <Text pt={5} pb={5}>&copy; 2024 Repopo</Text>
      </Box>
    </>
  );
});
