package com.algo.auth.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.unit.DataSize;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.algo.auth.application.CustomUserDetailsService;
import com.algo.auth.infrastructure.JwtAuthorizationFilter;

import jakarta.servlet.MultipartConfigElement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

	private final CustomUserDetailsService userDetailsService;
	private final JwtAuthorizationFilter jwtAuthorizationFilter;
	@Value("${cors.allowed-origins}")
	private String allowedOrigins;

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
				.requestMatchers( // 모두 접근 가능한 영역
					antMatcher("/api/rest/auth/**"),
					antMatcher("/api/actuator/**"),
					antMatcher("/static/images/**") // static 자원은 바로 접근할 수 있도록
				).permitAll()
				.requestMatchers(antMatcher("/api/questions/**")).hasRole("USER") // 권한이 필요한 영역
				.requestMatchers(antMatcher("/api/recovery/**")).hasRole("USER")
				.requestMatchers(antMatcher("/api/profile/**")).hasRole("USER")
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

	// 파일 업로드 관련 설정
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(DataSize.ofBytes(1024L * 1024L * 300)); // 파일 크기 제한 설정 (500MB로 설정)
		factory.setMaxRequestSize(DataSize.ofBytes(1024L * 1024L * 300)); // 파일 크기 제한 설정 (500MB로 설정)
		return factory.createMultipartConfig();
	}
}
