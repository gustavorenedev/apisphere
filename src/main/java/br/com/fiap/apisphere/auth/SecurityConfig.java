package br.com.fiap.apisphere.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // O @Bean é uma anotação que indica que o método é responsável por criar um objeto que será gerenciado pelo Spring
    @Bean
    // Fillter chain é um pattern que permite que você defina uma sequência de filtros que serão aplicados a uma requisição
    // O método config é responsável por configurar o filtro de segurança
    // o método config configura as regras para a requisição /login liberando o acesso para todos
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        //desabilita o csrf para permitir outras requisições
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(auth -> 
                auth
                    .requestMatchers(HttpMethod.POST, "/login").permitAll()
                    .requestMatchers(HttpMethod.GET, "/users").permitAll()
                    .requestMatchers(HttpMethod.POST, "/users").permitAll())
        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
