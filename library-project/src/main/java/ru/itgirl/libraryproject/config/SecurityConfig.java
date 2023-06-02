package ru.itgirl.libraryproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.itgirl.libraryproject.model.Users;
import ru.itgirl.libraryproject.repository.UsersRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UsersRepository usersRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/book").hasRole("reader")
                                .requestMatchers("/book/v2").hasRole("reader")
                                .requestMatchers("/books").hasRole("reader")
                                .requestMatchers("/authors").hasRole("admin")
                                .anyRequest().authenticated())
                .httpBasic();
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return login -> {
            Users user = usersRepository.findByLogin(login);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }

            UserDetails userDetails = User.builder()
                    .username(user.getLogin())
                    .password(passwordEncoder().encode(user.getPassword()))
                    .roles(user.getRoles())
                    .build();

            return userDetails;
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
