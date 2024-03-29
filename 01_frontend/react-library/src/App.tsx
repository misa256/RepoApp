import { ChakraProvider } from '@chakra-ui/react';
import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { ArtistPage } from './components/pages/ArtistPage';
import { HomePage } from './components/pages/HomePage';
import { NotFound } from './components/pages/NotFound';
import { LoginPage } from './components/pages/LoginPage';
import { theme } from './theme/theme';
import { LogoutPage } from './components/pages/LogoutPage';
import { AgencyPage } from './components/pages/AgencyPage';
import { ReportPage } from './components/pages/ReportPage';
import { PostReportPage } from './components/pages/PostReportPage';
import { ContactPage } from './components/pages/ContactPage';
import { AdminPage } from './components/pages/AdminPage';



function App() {
  return (
    <ChakraProvider theme={theme}>
      <BrowserRouter>
      <Routes>
        <Route path = '/' element = {<HomePage />} />
        <Route path = '/artist/:artistId' element = {<ArtistPage />} />
        <Route path = '/artist/:artistId/reports/:reportId' element = {<ReportPage />}/>
        <Route path = '/artist/:artistId/reports' element = {<PostReportPage />}/>
        <Route path = '/Login' element = {<LoginPage />}/>
        <Route path = '/Logout' element = {<LogoutPage />}/>
        <Route path = '/agency' element = {<AgencyPage />}/>
        <Route path = '/contact' element = {<ContactPage />}/>
        <Route path = '/admin' element = {<AdminPage />}/>
        <Route path = '*' element={<NotFound />}/>
      </Routes>
      </BrowserRouter>
    </ChakraProvider>
  );
}

export default App;
