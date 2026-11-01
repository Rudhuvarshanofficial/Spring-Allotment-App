
package com.tnea.counselling.configuration;

import com.tnea.counselling.model.Users;
import com.tnea.counselling.repository.UsersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(customizer -> customizer.disable());

        http.authorizeHttpRequests(request -> request
                .requestMatchers("/api/v1/students/register", "/api/v1/colleges").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/students/**").hasRole("STUDENT")
                .anyRequest().authenticated()
        );

        http.httpBasic(Customizer.withDefaults());

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider dao =
                new DaoAuthenticationProvider(userDetailsService);

        dao.setPasswordEncoder(new BCryptPasswordEncoder(12));

        return dao;
    }

    @Bean
    CommandLineRunner createAdmin(
            UsersDao usersDao,
            @Value("${app.admin.email:}") String adminEmail,
            @Value("${app.admin.password:}") String adminPassword) {

        return args -> {

            // Validate admin credentials.
            if (adminEmail.isBlank() || adminPassword.isBlank()) {
                throw new IllegalStateException(
                        "Admin credentials are not configured."
                );
            }

            // Create admin only if the account does not already exist.
            if (usersDao.findByUsername(adminEmail) == null) {

                BCryptPasswordEncoder encoder =
                        new BCryptPasswordEncoder(12);

                Users admin = new Users(
                        adminEmail,
                        encoder.encode(adminPassword),
                        "ADMIN"
                );

                usersDao.save(admin);
            }
        };
    }
}
