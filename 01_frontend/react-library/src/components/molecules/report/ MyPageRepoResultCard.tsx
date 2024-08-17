import { Box, Stack, Text} from "@chakra-ui/react";
import { type } from "@testing-library/user-event/dist/type";
import { FC } from "react"
import { Link, useNavigate } from "react-router-dom";
import { Report } from "../../../types/api/Report"

type props = {
    report : Report
}

export const MyPageRepoResultCard : FC<props> = (props) => {
    const { id, date, place, title, text, artistId, artistName } = props.report
    const navigate = useNavigate();   
    return(
        <Box
      w="240px"
      h="200px"
      bg="white"
      borderRadius="10px"
      shadow="md"
      p={4}
      _hover={{ cursor: "pointer", opacity: 0.8 }}
      onClick={()=>navigate(`/artist/${artistId}/reports/${id}`,{state:props.report})}
    >
      <Stack textAlign="center">
        <Text fontSize="lg" fontWeight="bold">
          {title}
        </Text>
        <Text fontSize="sm" color="gray">
          👼 {artistName}
        </Text>
        <Text fontSize="sm" color="gray">
          🗓 {date}
        </Text>
        <Text fontSize="sm" color="gray">
          📍 {place}
        </Text>
        <Text fontSize="sm" color="gray">
          {text.substring(0,20)}
        </Text>
      </Stack>
    </Box>
    )
}