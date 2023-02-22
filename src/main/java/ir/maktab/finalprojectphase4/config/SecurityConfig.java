package ir.maktab.finalprojectphase4.config;

import ir.maktab.finalprojectphase4.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final AccountService accountService;
    private final BCryptPasswordEncoder passwordEncoder;

    public SecurityConfig(AccountService accountService, BCryptPasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/signup/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/expert/**").hasRole("EXPERT")
                .requestMatchers("/customer/**").hasRole("CUSTOMER")
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic()
                .and()
                .logout()
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .permitAll();
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(
                        username -> accountService
                                .findByUsername(username)
                                .orElseThrow(
                                        () -> new UsernameNotFoundException(
                                                String.format("there is no user with this %s!", username))))
                .passwordEncoder(passwordEncoder);
    }
}
