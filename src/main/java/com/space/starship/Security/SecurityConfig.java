package com.space.starship.Security;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    AuthenticationManager authenticationManager;

    // Users definition will be in memory
    // Related to the authentication
    @Bean
    public InMemoryUserDetailsManager userDetailsManager() throws Exception {
        List<UserDetails> users = List.of(
                User.withUsername("ADMIN")
                        .password("{noop}ADMIN") // {noop} to show that the password is not encoded
                        .authorities("USER")
                        .roles("USER")
                        .build());

        return new InMemoryUserDetailsManager(users);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
        return this.authenticationManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(crs -> crs.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/starship/getStarships").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/starship/getStarshipById").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/starship/getStarshipsByName").hasRole("USER")

                        .requestMatchers(HttpMethod.POST, "/starship/createStarship").hasRole("USER")

                        .requestMatchers(HttpMethod.PUT, "/starship/modifyStarshipByName").hasRole("USER")

                        .requestMatchers(HttpMethod.DELETE, "/starship/deleteStarshipByName").hasRole("USER")

                        .requestMatchers("/starship/**").authenticated()

                        .anyRequest().permitAll()
                )

                .addFilter(new JWTAuthorization(authenticationManager));

        return http.build();
    }
}
