package com.tnea.counselling.configuration;

import com.tnea.counselling.model.Users;
import com.tnea.counselling.repository.UsersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Autowired
    private UserDetailsService userDetailsService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        http.csrf(customizer -> customizer.disable());
        http.authorizeHttpRequests(request -> request
                .requestMatchers("/student/register").permitAll()
                .requestMatchers("/student/**").hasRole("STUDENT")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated());
        http.httpBasic(Customizer.withDefaults());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider(userDetailsService);
        dao.setPasswordEncoder(new BCryptPasswordEncoder(12));
        return dao;
    }

    @Bean
    CommandLineRunner createAdmin(UsersDao usersDao) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

        return args -> {
            if (usersDao.findByUsername("admin@gmail.com") == null) {
                Users admin = new Users("admin@gmail.com",encoder.encode("123"),"ADMIN");
                usersDao.save(admin);
            }
        };
    }
}
