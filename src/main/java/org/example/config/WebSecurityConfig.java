package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.security.UserDetailsServiceImpl;
import org.example.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .authorizeRequests()
            .antMatchers(
                "/css/**",
                "/img/**",
                "/js/**",
                "/webjars/**"
            ).permitAll()
            .antMatchers(
                "/lessons"
            ).authenticated()
            .antMatchers(
                "/lessons/**",
                "/buildings/**",
                "/rooms/**",
                "/courses/**",
                "/groups/**",
                "/subjects/**",
                "/teachers/**",
                "/students/**",
                "/administrators/**"
            ).hasRole("ADMIN")
            .anyRequest().authenticated()
            .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/")
            .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login");

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(userService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
