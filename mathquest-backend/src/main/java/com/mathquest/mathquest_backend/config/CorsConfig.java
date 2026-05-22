package com.mathquest.mathquest_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        //Permite envio de cookies ou credenciais de sessão. (se for necessário)
        config.setAllowCredentials(true);
        //Vai definir e permitir as origens abaixo
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:5000",
                "http://127.0.0.1:5000",
                "https://mathquest-nu.vercel.app"));
        // Permite todos os headers comuns e os customizados
        config.setAllowedHeaders(Collections.singletonList("*"));
        // Garante uso de todos os métodos http abaixo
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Aplica essa mesma config pra todas as rotas
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);

    }





}
