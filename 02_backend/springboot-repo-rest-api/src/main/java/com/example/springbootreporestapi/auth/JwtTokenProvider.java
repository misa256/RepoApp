package com.example.springbootreporestapi.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    //    プロパティファイルに定義した値を参照する場合、@Value("${プロパティキー}")アノテーションをつける。
    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    //    JWTトークンを作成する
    public String generateToken(Authentication authentication) {
        String email = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
        return token;
    }

    //    JWTトークンからemail(ユーザーネーム)を取得する
    public String getEmail(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String email = claims.getSubject();
        return email;
    }

    //  JWTトークンのバリデーション
    public boolean validateToken(String token) {
        Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parse(token);
        return true;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }
}
