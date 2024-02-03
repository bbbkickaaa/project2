package com.configuration;

import java.util.Arrays;   

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.API.User.Oauth2.CustomOAuth2UserService;
import com.API.User.Oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.API.User.Oauth2.JwtAuthenticationFilter;
import com.API.User.Oauth2.JwtTokenProvider;
import com.API.User.Oauth2.OAuth2AuthenticationFailureHandler;
import com.API.User.Oauth2.OAuth2AuthenticationSuccessHandler;

import jakarta.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
		
	 
	    public SecurityConfig(JwtTokenProvider jwtTokenProvider, CustomOAuth2UserService customOAuth2UserService, OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler, OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler) {
	        this.jwtTokenProvider = jwtTokenProvider;
	        this.customOAuth2UserService = customOAuth2UserService;
	        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
	        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
	    }
	 
	    @Bean
	    public HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository() {
	        return new HttpCookieOAuth2AuthorizationRequestRepository();
	    }
	 
	    @Bean
	    public BCryptPasswordEncoder encoder() {
	        return new BCryptPasswordEncoder();
	    }
	    
	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	    
	    @Bean
	    public AuthenticationManager authenticationManager(
	            AuthenticationConfiguration authenticationConfiguration) throws Exception {
	        return authenticationConfiguration.getAuthenticationManager();
	    }
	    
		@Bean
	    public CorsConfigurationSource configurationSource () {
	    
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(false);
        config.setAllowedOrigins(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setMaxAge(3600 * 6L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
	
	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
	    // 기본 보안 설정
	    http
	        .cors(cors -> cors.configurationSource(configurationSource()))
	        .csrf(AbstractHttpConfigurer::disable)
	        .formLogin(AbstractHttpConfigurer::disable)
	        .rememberMe(AbstractHttpConfigurer::disable)
	        .httpBasic(AbstractHttpConfigurer::disable)
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

	    // HTTP 요청에 대한 권한 설정
	    http
	    	.authorizeHttpRequests(auth -> auth
	    	.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
	    	.requestMatchers("/webjars/**", "/js/**", "/image/**", "/", "/auth/**","/api/**").permitAll()
            .anyRequest().authenticated()
        )
	    	;

	    // OAuth2 로그인 설정

	  http
	    .oauth2Login(oauth2Login -> oauth2Login
	            .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
	                .baseUri("/oauth2/authorization/google")
	                .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository()))
	            .redirectionEndpoint(redirectionEndpoint -> redirectionEndpoint
	                .baseUri("/login/oauth2/code/**"))
	            .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
	                .userService(customOAuth2UserService))
	            .successHandler(oAuth2AuthenticationSuccessHandler)
	            .failureHandler(oAuth2AuthenticationFailureHandler))
	        
	        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
	    
	    return http.build();
	}
	
	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web
            .ignoring()
            .requestMatchers("/error", "/error/**"); // 에러 페이지 경로 무시
    }
	
		
}


