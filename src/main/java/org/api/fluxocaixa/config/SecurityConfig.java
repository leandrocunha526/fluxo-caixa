package org.api.fluxocaixa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Desabilita CSRF porque é API
            .cors(cors -> cors.configure(http)) // Ativa CORS conforme CorsConfig
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Libera TODAS as rotas sem autenticação
            );

        return http.build();
    }
}
