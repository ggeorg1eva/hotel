package com.example.bookhotel.config;

import com.example.bookhotel.model.entity.enumeration.UserRoleEnum;
import com.example.bookhotel.repository.UserRepository;
import com.example.bookhotel.service.impl.UserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/rooms/add", "/rooms/delete/{id}", "/categories/add", "/categories/all", "/categories/delete/{id}",
                        "/activities/add", "/activities/delete/{id}", "/rooms/reservations/all", "/rooms/reservations/deny/{id}", "/rooms/reservations/approve/{id}").hasRole(UserRoleEnum.OPERATOR.name())
                .antMatchers("/admin/operators", "/admin/guests", "/stats", "/operators/remove-role/{id}").hasRole(UserRoleEnum.ADMIN.name())
                .antMatchers("/", "/users/login", "/users/register", "/rooms/all",
                        "/reviews/all", "/activities/all", "/rooms/{id}","/contacts").permitAll()
                .anyRequest().authenticated()
            .and()
                .formLogin()
                .loginPage("/users/login")
                .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
                .defaultSuccessUrl("/")
                .failureUrl("/users/login-error")
            .and()
                .logout()
                .logoutUrl("/users/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new UserDetailsServiceImpl(userRepository);
    }
}
