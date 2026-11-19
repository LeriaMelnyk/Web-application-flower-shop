package floristry.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // порядок важливий: специфічні правила йдуть першими
                        .requestMatchers(HttpMethod.GET, "/api/v1/flowers/hello/user").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/flowers/hello/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/flowers/hello/superadmin").hasRole("SUPERADMIN")

                        .requestMatchers("/api/v1/audit/**").hasRole("SUPERADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/flowers", "/api/v1/flowers/**")
                        .hasAnyRole("USER", "ADMIN", "SUPERADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/flowers")
                        .hasAnyRole("ADMIN", "SUPERADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/flowers/*")
                        .hasAnyRole("ADMIN", "SUPERADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/flowers/*")
                        .hasRole("SUPERADMIN")

                        .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}