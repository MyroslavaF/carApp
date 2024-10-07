package com.mfarion.carregistry.config;

import com.mfarion.carregistry.filter.JwtAuthenticationFilter;
import com.mfarion.carregistry.services.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



 //La clase gestiona el acceso a los recursos

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    //filtrar dentro del token
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;

    //un bean adictional para autentificar los usuarios(AuthentificationProvider)
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    //bean para que la configuracion actual se pueda utilizar(AuthenticationManager)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //SecurityFilterChain para generar un entorno seguro
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/signup").permitAll()
                        .requestMatchers(HttpMethod.POST, "/cars/addCar").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/cars/updateCar/{id}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/cars/deleteCar/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/cars/getCars").permitAll()
                        .requestMatchers(HttpMethod.GET, "/cars/getCarById/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/userImage/{id}/add").permitAll()
                        .requestMatchers(HttpMethod.GET, "/userImage/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/uploadCsv").permitAll()
                        .requestMatchers(HttpMethod.GET, "/downloadCars").permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

}