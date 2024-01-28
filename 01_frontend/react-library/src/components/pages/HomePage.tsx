import { Header } from "../organisms/Header"
import { ArtistSearch } from "../organisms/artist/ArtistSearch"
import { Box, Image } from '@chakra-ui/react'
import { Outlet } from "react-router-dom"

export const HomePage = ()=>{
    return(
        <Box>
           <Header />
           <ArtistSearch />
        </Box>
    )
}