package com.example.springbootreporestapi.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Spring Security Filterの前に実行される
//JWTトークンのバリデーションを行い、userDetailsをSpring Securityに提供する
//1リクエストごとに一度だけ実行されるフィルターとして動作
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
//        JWTトークンをHTTPリクエストから取得する
        String token = getTokenFromRequest(request);

        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
//         JWTトークンからユーザーネームを取得
            String email = jwtTokenProvider.getEmail(token);

//　　　　　ログインユーザーのuserDetailsを取得し、SecurityContextHolderに認証情報を登録する
            UserDetails loginUserDetails = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginUserDetails,
                    null,
                    loginUserDetails.getAuthorities()
            );
//           現在のHTTPリクエストの詳細情報を取得して設定している。主にIPアドレスやセッションIDなどが含まれる。
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

//        次のフィルターにリクエストを渡す
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7, bearerToken.length());
        } else {
            return null;
        }
    }
}
