package com.electronicstore.security;

import com.electronicstore.helper.ApplicationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfigurations {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //configuration for URL's
        http.authorizeHttpRequests(httpReq -> {
            httpReq
                    .requestMatchers(HttpMethod.GET, ApplicationConstants.PUBLIC_URLS_GET).permitAll()
                    .requestMatchers(HttpMethod.POST, ApplicationConstants.PROTECTED_URLS_POST).permitAll()
                    .requestMatchers(HttpMethod.POST, ApplicationConstants.PROTECTED_URLS_POST_ADMIN_USER).hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, ApplicationConstants.PROTECTED_URLS_PUT_ADMIN_USER).hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, ApplicationConstants.PROTECTED_URLS_DELETE_ADMIN_USER).hasRole("ADMIN")
                    .anyRequest().authenticated();
        });

        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        http.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();

    }

}
