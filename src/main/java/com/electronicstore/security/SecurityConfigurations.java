package com.electronicstore.security;
import com.electronicstore.helper.ApplicationConstants;
import com.electronicstore.jwt.CustomAccessDeniedHandler;
import com.electronicstore.jwt.JwtAuthenticationEntryPoint;
import com.electronicstore.jwt.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfigurations {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //configuration for URL's
        http.authorizeHttpRequests(httpReq -> {
            httpReq

                    .requestMatchers(ApplicationConstants.SWAGGER_URLS).permitAll()
                    .requestMatchers(HttpMethod.GET, ApplicationConstants.PUBLIC_URLS_GET).permitAll()
                    .requestMatchers(HttpMethod.POST, ApplicationConstants.PROTECTED_URLS_POST).permitAll()
                    .requestMatchers(HttpMethod.POST, ApplicationConstants.PROTECTED_URLS_POST_ADMIN_USER).hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, ApplicationConstants.PROTECTED_URLS_PUT_ADMIN_USER).hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, ApplicationConstants.PROTECTED_URLS_DELETE_ADMIN_USER).hasRole("ADMIN")
                    .anyRequest().authenticated();
        });
        //handling exception
        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint));
        //access denied
        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(new CustomAccessDeniedHandler()));
        //adding the jwt authentication filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        //handling CSRF
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        //handling Session
        http.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //cors
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOriginPatterns(List.of("*"));
                corsConfiguration.setAllowedMethods(List.of("*"));
                corsConfiguration.setAllowCredentials(true);
                corsConfiguration.setAllowedHeaders(List.of("*"));
                //preflight which client can keep it in cache memory
                corsConfiguration.setMaxAge(3000L);
                return corsConfiguration;
            }
        }));
        return http.build();

    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
      return authenticationConfiguration.getAuthenticationManager();
    }

}
