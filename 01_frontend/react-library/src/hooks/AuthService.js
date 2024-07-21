import axios from "axios";



  // ブラウザのローカルストレージにtokenを保管
  export const storeToken = (token) => localStorage.setItem("token", token);
  
  // ブラウザのローカルストレージからtokenを取得
  export const getToken = () => localStorage.getItem("token");

  // ログインユーザーIDをセッションストレージに保管
  export const saveLoggedInUserId = (userId) => {
    sessionStorage.setItem("authenticatedUserId", userId);
} 

　// ログインユーザーIDをセッションストレージから取得　
export const getLoggedInUserId = () => sessionStorage.getItem("authenticatedUserId");
  
  // ログインユーザーのemailをセッションストレージに保管
  export const saveLoggedInUserEmail = (email) => {
      sessionStorage.setItem("authenticatedUserEmail", email);
  } 

　// ログインユーザーのemailをセッションストレージから取得　
  export const getLoggedInUserEmail = () => sessionStorage.getItem("authenticatedUserEmail");

  // ログインユーザーのnameをセッションストレージに保管
  export const saveLoggedInUserName = (name) => {
    sessionStorage.setItem("authenticatedUserName", name);
} 

　// ログインユーザーのnameをセッションストレージから取得　
export const getLoggedInUserName = () => sessionStorage.getItem("authenticatedUserName");

// ログインユーザーのrolesをセッションストレージに保管
export const saveLoggedInUserRoles = (roles) => {
  sessionStorage.setItem("authenticatedUserRoles", roles);
} 

　// ログインユーザーのrolesをセッションストレージから取得　
export const getLoggedInUserRoles = () => sessionStorage.getItem("authenticatedUserRoles")
  
  //  ユーザーがログインしているかどうかを判定
export const isUserLoggedIn = () =>{
      const email = sessionStorage.getItem('authenticatedUserEmail');
      if(email == null){
        return false;
      }else{
        return true;
      }
  }

  // ログインしているユーザーのemailを返却
  // export const getLoggedInUser = () => {
  //   const email = sessionStorage.getItem('authenticatedUser');
  //   return email;
  // }
  
  export const logout = () => {
    localStorage.clear();
    sessionStorage.clear();
    window.location.reload(false);
}

 
// リクエストする時、ヘッダー（Authorization）に作成したtokenを追加
axios.interceptors.request.use(function (config) {
  config.headers['Authorization'] = getToken();
  return config;
}, function (error) {
  // Do something with request error
  return Promise.reject(error);
});


