package com.algo.auth.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import com.algo.auth.application.CustomUserDetailsService;
import com.algo.auth.infrastructure.JwtAuthorizationFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


/**
 * @author : iyeong-gyo
 * @package : com.sportedu.config
 * @since : 07.11.23
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = "algomi.security.enable", havingValue = "true")
public class AuthenticationConfig {

  @Value("${cors.allowed-origins}")
  private String allowedOrigins;
  private final CustomUserDetailsService userDetailsService;
  private final JwtAuthorizationFilter jwtAuthorizationFilter;

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder encoder)
      throws Exception {
    AuthenticationManagerBuilder authenticationBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationBuilder.userDetailsService(userDetailsService).passwordEncoder(encoder);
    return authenticationBuilder.build();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .cors((cors) -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests((authorize) -> authorize
            .requestMatchers(
                antMatcher("/api/rest/auth/**"),
                antMatcher("/api/actuator/**")
            ).permitAll()
            .requestMatchers(antMatcher("/api/questions/**")).hasRole("USER")
            .requestMatchers(antMatcher("/api/recovery/**")).hasRole("USER")
            .anyRequest()
            .authenticated()
        )
        .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of(allowedOrigins));
    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTION"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
