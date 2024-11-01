package de.joshuatoepfer.bruno_demo;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // CSRF deaktivieren, da wir eine stateless REST API haben
        .csrf(csrf -> csrf.disable())
        // Session Management auf STATELESS setzen
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // Autorisierungsregeln festlegen
        .authorizeHttpRequests(
            authz -> authz.requestMatchers("/todos/**").authenticated().anyRequest().permitAll())
        // Form Login deaktivieren
        .formLogin(form -> form.disable())
        // HTTP Basic Auth deaktivieren
        .httpBasic(basic -> basic.disable())
        // Exception Handling anpassen, um 401 zurückzugeben
        .exceptionHandling(
            exception ->
                exception.authenticationEntryPoint(
                    (request, response, authException) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
        // OAuth2 Resource Server mit JWT-Unterstützung aktivieren
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

    return http.build();
  }

  @Bean
  public JwtDecoder jwtDecoder(@Value("${keycloak.base-url}")String keycloakBaseUrl) {
    return NimbusJwtDecoder
        .withJwkSetUri(keycloakBaseUrl + "/realms/todo/protocol/openid-connect/certs")
        .jwsAlgorithm(SignatureAlgorithm.RS256)
        .build();
  }
}
