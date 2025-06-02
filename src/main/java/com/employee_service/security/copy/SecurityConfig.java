package com.employee_service.security.copy;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${ADMIN_USERNAME}")
    private String adminUsername;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword; // must be bcrypt encoded

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
            .username(adminUsername)
            .password(adminPassword) // already encoded
            .roles("ADMIN")
            .build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/employee/admin", "/css/**", "/js/**", "/images/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/employee/admin")
                .loginProcessingUrl("/employee/admin")
                .defaultSuccessUrl("/employee/allemp", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/employee/logout")
                .logoutSuccessUrl("/employee/admin?logout")
                .permitAll()
            );

        return http.build();
    }
}
