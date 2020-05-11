package com.kangmin.security.config.security;

import com.kangmin.security.config.jwt.JwtAuthenticationEntryPoint;
import com.kangmin.security.config.jwt.JwtProvider;
import com.kangmin.security.config.jwt.JwtTokenVerificationFilter;
// import com.kangmin.security.config.jwt.JwtLoginAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.kangmin.security.model.security.WebUserPermission.ACCOUNT_READ;
import static com.kangmin.security.model.security.WebUserRole.ADMIN;
import static com.kangmin.security.model.security.WebUserRole.SUPER_ADMIN;

@Import({
        PasswordConfig.class,
})
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService webUserDetailsService;
    private final JwtProvider jwtProvider;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    public WebSecurityConfig(final PasswordEncoder passwordEncoder,
                             final UserDetailsService webUserDetailsService,
                             final JwtProvider jwtProvider,
                             final JwtAuthenticationEntryPoint unauthorizedHandler) {
        this.passwordEncoder = passwordEncoder;
        this.webUserDetailsService = webUserDetailsService;
        this.jwtProvider = jwtProvider;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Override
    protected void configure(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                    .disable()
                .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler)
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .headers()
                    .frameOptions().disable()
                    .and()
                // alternative way to configure using two filters
                // .addFilter(new JwtLoginAuthenticationFilter(authenticationManager(), jwtProvider))
                // .addFilterAfter(jwtTokenVerificationFilterBean(), JwtLoginAuthenticationFilter.class)
                // this is the way to separate the filter
                .addFilterBefore(jwtTokenVerificationFilterBean(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                    .antMatchers(
                            "/favicon.ico",
                            "/", "/index",
                            "/css/*", "/js/*",
                            "/login", "/auth/login",
                            "/register", "/auth/register",
                            "/devTest",
                            "/h2-console", "/h2-console/*"
                    )
                        .permitAll()
                    .antMatchers("/api/auth/*")
                        .permitAll()
                    .antMatchers("/api/v1/todolists/**")
                        .authenticated()
                    .antMatchers("/api/v1/accounts/**")
                        .hasAnyAuthority(ACCOUNT_READ.getName())
                    .antMatchers("/admin/**")
                        .hasAnyRole(ADMIN.name(), SUPER_ADMIN.name())
                .anyRequest()
                    .authenticated();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public JwtTokenVerificationFilter jwtTokenVerificationFilterBean() {
        return new JwtTokenVerificationFilter(jwtProvider);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(webUserDetailsService);
        return provider;
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
