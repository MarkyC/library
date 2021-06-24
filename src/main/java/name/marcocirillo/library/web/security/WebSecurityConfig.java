package name.marcocirillo.library.web.security;

import name.marcocirillo.library.auth.AuthService;
import name.marcocirillo.library.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.core.context.SecurityContextHolder.MODE_INHERITABLETHREADLOCAL;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final AuthService authService;

    @Autowired
    public WebSecurityConfig(AuthService authService) {
        super();
        this.authService = authService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SecurityContextHolder.setStrategyName(MODE_INHERITABLETHREADLOCAL);
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth").authenticated()
                .antMatchers("/checkout").authenticated()
                .anyRequest().permitAll()
                .and()
                .httpBasic()
                .and()
                .cors().disable()
                .csrf().disable();

        http.addFilterBefore(
                new JwtAuthenticationTokenFilter(
                        authService,
                        new OrRequestMatcher(
                                List.of(
                                        "/auth",
                                        "/checkout"
                                ).stream()
                                        .map(AntPathRequestMatcher::new)
                                        .collect(Collectors.toList()))),
                UsernamePasswordAuthenticationFilter.class);
    }
}
