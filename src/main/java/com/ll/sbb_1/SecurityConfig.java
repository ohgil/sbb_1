package com.ll.sbb_1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
//스프링의 환경설정 파일임을 의미하는 어노테이션이다. 여기서는 스프링 시큐리티의 설정을 위해 사용되었다.
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
//모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만드는 어노테이션이다.
public class SecurityConfig {
    @Autowired
    private OAuth2UserService oAuth2UserService;
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
                .formLogin((formLogin) -> formLogin
                        .loginPage("/user/login")
                        .defaultSuccessUrl("/"))
                .oauth2Login(
                        oauth2Login -> oauth2Login
                                .loginPage("/user/login")
                                .userInfoEndpoint(
                                        userInfoEndpoint -> userInfoEndpoint
                                                .userService(oAuth2UserService)
                                )
                )
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true))
        ;
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}