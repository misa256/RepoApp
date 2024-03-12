import { Header } from "../organisms/Header"
import { ArtistSearch } from "../organisms/artist/ArtistSearch"
import { Box, Image } from '@chakra-ui/react'
import { Outlet } from "react-router-dom"
import { Footer } from "../organisms/Footer"

export const HomePage = ()=>{
    return(
        <Box
        // display = "grid"
        // gridTemplateRows = "auto 1fr auto"
        >
           <Header />
           <ArtistSearch />
           <Footer />
        </Box>
    )
}