package hulio13.articlesApi.configuration;

import hulio13.articlesApi.web.security.JPAUserDetailsManager;
import hulio13.articlesApi.web.security.JPAUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    @Value("{secret_phrase}")
    private String secret;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .anonymous()
                    .authorities("ROLE_ANONYMUS")
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .httpBasic()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .and()
                .authorizeHttpRequests()
                    .requestMatchers("/**").permitAll()
                .and()
                .rememberMe()
                    .useSecureCookie(true)
                    .tokenValiditySeconds(60 * 60 * 24 * 7)
                    .key(secret)
        ;

        return http.build();
    }

    @Bean
    public UserDetailsManager userDetailsService(JPAUserRepository repository) {
        return new JPAUserDetailsManager(repository, passwordEncoder());
    }
}
