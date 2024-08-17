import { ChakraProvider } from '@chakra-ui/react';
import React, { ReactNode } from 'react';
import { BrowserRouter, Navigate, Route, Routes, useLocation } from 'react-router-dom';
import { ArtistPage } from './components/pages/ArtistPage';
import { HomePage } from './components/pages/HomePage';
import { NotFound } from './components/pages/NotFound';
import { LoginPage } from './components/pages/LoginPage';
import { theme } from './theme/theme';
import { AgencyPage } from './components/pages/AgencyPage';
import { ReportPage } from './components/pages/ReportPage';
import { PostReportPage } from './components/pages/PostReportPage';
import { ContactPage } from './components/pages/ContactPage';
import { AdminPage } from './components/pages/AdminPage';
import { isUserLoggedIn} from './hooks/AuthService';
import { UserRegisterPage } from './components/pages/UserRegisterPage';
import { MyPage } from './components/pages/MyPage';



function App() {

  const AuthenticatedRoute = ({children} : any) => {
    const location = useLocation();
    const isAuth = isUserLoggedIn();
    if(isAuth){
      return children;
    }else{
      return <Navigate to = '/Login'　state={{ from: location }} replace/>
    }
  }

  return (
    <ChakraProvider theme={theme}>
      <BrowserRouter>
      <Routes>
        <Route path = '/' element = {<HomePage />} />
        <Route path = '/artist/:artistId' element = {<ArtistPage />} />
        <Route path = '/artist/:artistId/reports/:reportId' element = {<ReportPage />}/>
        <Route path = '/artist/:artistId/reports' element = {
        <AuthenticatedRoute>
        <PostReportPage />
        </AuthenticatedRoute>}/>
        <Route path = '/Login' element = {<LoginPage />}/>
        <Route path = '/agency' element = {
        <AuthenticatedRoute>
        <AgencyPage />
        </AuthenticatedRoute>}/>
        <Route path = '/contact' element = {
        <AuthenticatedRoute>
        <ContactPage />
        </AuthenticatedRoute>}/>
        <Route path = '/myPage' element = {
        <AuthenticatedRoute>
          <MyPage />
        </AuthenticatedRoute>
        } />
        <Route path = '/admin' element = {<AdminPage />}/>
        <Route path = '/user/register' element={<UserRegisterPage />}/>
        <Route path = '*' element={<NotFound />}/>
      </Routes>
      </BrowserRouter>
    </ChakraProvider>
  );
}

export default App;
