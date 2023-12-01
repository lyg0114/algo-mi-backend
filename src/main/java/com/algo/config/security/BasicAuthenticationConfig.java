package com.algo.config.security;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


/**
 * @author : iyeong-gyo
 * @package : com.sportedu.config
 * @since : 07.11.23
 */
@Slf4j
@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = "algomi.security.enable", havingValue = "true")
public class BasicAuthenticationConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
      throws Exception {
    http
        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/main-dashboard")
        )
    ;
    http
        .authorizeHttpRequests((authorize) -> authorize
            .requestMatchers(antMatcher("/login")).permitAll()
            .requestMatchers(antMatcher("/customer/**")).hasAnyRole("USER", "ADMIN")
            .requestMatchers(antMatcher("/admin/**")).hasRole("ADMIN")
            .anyRequest()
            .authenticated()
        );
    return http.build();
  }
}