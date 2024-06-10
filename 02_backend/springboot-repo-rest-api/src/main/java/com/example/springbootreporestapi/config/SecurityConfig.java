package com.example.springbootreporestapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMethodSecurity
public class SecurityConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
//        全てのエンドポイントに対してCORSを設定
        registry.addMapping("/**")
//                アクセス許可するoriginを設定
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable())
//        .formLogin(login -> login
//                        .permitAll())
                .authorizeHttpRequests(auth -> auth
//                  ログイン必要なし
                        .requestMatchers(HttpMethod.GET, "/repoApi/artist/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/repoApi/repo/artist/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/repoApi/agency/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/repoApi/user/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
        ;
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
