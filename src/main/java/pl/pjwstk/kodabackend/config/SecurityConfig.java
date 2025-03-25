package pl.pjwstk.kodabackend.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import pl.pjwstk.kodabackend.security.audit.ApplicationAuditAware;
import pl.pjwstk.kodabackend.security.token.JwtAuthenticationFilter;
import pl.pjwstk.kodabackend.security.user.persistance.repository.AppUserRepository;

import java.util.UUID;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
class SecurityConfig {

    private static final String[] WHITE_LIST_URL = {
            "/**", //TODO WYJEBAC TO POD KONIEC INZYNIERKI
            "/api/v1/external/users/**",
            "/api/v1/categories/**",
            "/api/v1/internal/users/**",
            "/api/v1/external/users/**",
            "/api/v1/auth/**",
            "/resources/**",
            "/actuator/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };

    @Bean
    @SneakyThrows
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            AuthenticationProvider authenticationProvider,
                                            LogoutHandler logoutHandler,
                                            JwtAuthenticationFilter jwtAuthFilter) {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request ->
                                request.requestMatchers(WHITE_LIST_URL)
                                        .permitAll()
//                                    .requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name(), MANAGER.name())
//                                    .requestMatchers(GET, "/api/v1/management/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
//                                    .requestMatchers(POST, "/api/v1/management/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
//                                    .requestMatchers(PUT, "/api/v1/management/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
//                                    .requestMatchers(DELETE, "/api/v1/management/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())
                                        .anyRequest()
                                        .authenticated()
                ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @SneakyThrows
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }

    @Bean
    UserDetailsService userDetailsService(AppUserRepository appUserRepository) {
        return username -> appUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    AuditorAware<UUID> auditorAware() {
        return new ApplicationAuditAware();
    }

}
