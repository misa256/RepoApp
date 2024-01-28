import { Box, HStack } from "@chakra-ui/react";
import { Text } from '@chakra-ui/react'
import { FC, memo } from "react";
import { Link, useNavigate } from "react-router-dom";

type conditions = {
id : number;
name : string;
country : string;
agencyName : string;
}

export const ArtistSearchResultCard :FC<conditions> = memo((props)=>{
const {id, name, country, agencyName} = props;
const navigate = useNavigate();
    return(
        <Box
        w="300px"
        h="50px"
        bg="white"
        borderRadius="10px"
        shadow="md"
        p={4}
        _hover={{ cursor: "pointer", opacity: 0.8 }}
        onClick={() => navigate(`/artist/${id}`)}
      >
          <HStack>
          <Text fontSize="lg" fontWeight="bold">
          {name}
        </Text>
        <Text fontSize="sm" color="gray">
          {country}
        </Text>
        <Text fontSize="sm" color="gray">
          {agencyName}
        </Text>
          </HStack>
    </Box>
    )
});