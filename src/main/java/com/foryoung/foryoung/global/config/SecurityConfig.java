package com.foryoung.foryoung.global.config;

import com.foryoung.foryoung.global.jwt.JwtAuthenticationFilter;
import com.foryoung.foryoung.global.jwt.JwtTokenProvider;
import com.foryoung.foryoung.auth.userdetails.CustomUserDetailsService;
import com.foryoung.foryoung.auth.service.TokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtTokenProvider tokenProvider;
    private final TokenService tokenService;
    private final CustomUserDetailsService customUserDetailsService;


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource())
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests

                        .requestMatchers(
                                "/",
                                "/error/**"
                        ).permitAll()

                        .requestMatchers(
                                "/auth/**",
                                "/oauth2/**",
                                "/members/login"
                        ).permitAll()


                        .requestMatchers(HttpMethod.PATCH, "/members/me/nickname").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/members/me").hasRole("USER")

                        .requestMatchers(HttpMethod.GET, "/performances/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/performances").hasRole("USER")

                        .requestMatchers(HttpMethod.GET, "/performances/*/schedules").permitAll()

                        .requestMatchers("/performance-records/**").hasRole("USER")

                        .requestMatchers(HttpMethod.GET, "/performance-reviews/public").permitAll()
                        .requestMatchers(HttpMethod.GET, "/performance-reviews/liked").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/performance-reviews/*").permitAll()
                        .requestMatchers("/performance-reviews").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/performance-reviews/comments/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/performance-reviews/comments/**").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/performance-reviews/comments/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/performance-reviews/comments/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/performance-reviews/*/likes").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/performance-reviews/*/likes").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/performance-reviews/liked").hasRole("USER")

                        .requestMatchers(HttpMethod.GET, "/venues/**").permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/venue-views/*",
                                "/venue-views/*/views"
                        ).permitAll()

                        .anyRequest().authenticated()
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(
                                (request, response, authException) -> {
                                    response.sendError(
                                            HttpServletResponse.SC_UNAUTHORIZED,
                                            "Unauthorized"
                                    );
                                }
                        )
                )

                .addFilterBefore(
                        new JwtAuthenticationFilter(
                                tokenProvider,
                                tokenService,
                                customUserDetailsService
                        ),
                        UsernamePasswordAuthenticationFilter.class
                );

        return httpSecurity.build();

    }


    private CorsConfigurationSource corsConfigurationSource() {

        return request -> {

            CorsConfiguration configuration = new CorsConfiguration();

            configuration.setAllowedOrigins(List.of("http://localhost:5173"));
            configuration.setAllowedMethods(List.of("*"));
            configuration.setAllowedHeaders(List.of("*"));
            configuration.setAllowCredentials(true);
            configuration.setMaxAge(3600L);
            configuration.setExposedHeaders(List.of("Authorization"));

            return configuration;

        };

    }


}