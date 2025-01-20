package org.example.exercisespringboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // 스프링의 환경 설정 파일임을 의미
@EnableWebSecurity // 모든 요청 URL 이 스프링 시큐리티의 제어를 받도록 만드는 애너테이션
/*
프링 시큐리티를 활성화하는 역할을 한다.
내부적으로 SecurityFilterChain 클래스가 동작하여 모든 요청 URL 에 이 클래스가 필터로 적용되어 URL 별로 특별한 설정을 할 수 있게 된다.
스프링 시큐리티의 세부 설정은 @Bean 애너테이션을 통해 SecurityFilterChain 빈을 생성하여 설정할 수 있다.
 */
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 인증되지 않은 모든 페이지의 요청을 허락한다는 의미
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
                // H2 콘솔 페이지의 요청은 CSRF 보호를 비활성화한다는 의미 -> CSRF 보호를 비활성화하지 않으면 H2 콘솔 페이지에 접근할 수 없다.(이유는 Obsidian 참조)
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
                // H2 콘솔 페이지의 요청은 프레임을 허용한다는 의미 -> 위 코들로 인해 H2 콘솔 페이지에 접근할 수 있게 되면 화면이 깨져 보임. 이걸 해결하게 해준다. // https://wikidocs.net/162150
                .headers((headers) -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
                // .formLogin 메서드는 스프링 시큐리티의 로그인 설정을 담당하는 부분으로, 설정 내용은 로그인 페이지의 URL 은 /user/login 이고 로그인 성공 시에 이동할 페이지는 루트 URL(/)임을 의미
                .formLogin((formLogin) -> formLogin
                        .loginPage("/user/login")
                        .defaultSuccessUrl("/"))
                // 로그아웃
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")) // AntPathRequestMatcher 클래스는 URL 패턴을 지정하여 요청을 처리하는 클래스
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true))
        ;
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    /*
    @Bean: 이 애너테이션은 메서드가 스프링 컨텍스트에 빈으로 등록된다는 것을 의미합니다.
    AuthenticationManager: 스프링 시큐리티에서 인증을 처리하는 주요 인터페이스입니다.
    AuthenticationConfiguration: 스프링 시큐리티의 인증 설정을 담고 있는 클래스입니다.
    authenticationManager(AuthenticationConfiguration authenticationConfiguration): 이 메서드는 AuthenticationConfiguration 객체를 받아서 AuthenticationManager 를 반환합니다.
    authenticationConfiguration.getAuthenticationManager(): AuthenticationConfiguration 객체에서 AuthenticationManager 를 가져옵니다.
     */
}