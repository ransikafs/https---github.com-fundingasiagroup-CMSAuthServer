package com.fundingsocieties.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class AuthServerAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource ds;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private AuthenticationEntryPoint authEntryPoint;

    @Bean
    public UserDetailsManager jdbcUserDetailsManager() {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
        jdbcUserDetailsManager.setDataSource(ds);
        return jdbcUserDetailsManager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        RequestCache requestCache = new HttpSessionRequestCache();
        CustomAuthFilter authFilter = new CustomAuthFilter(requestCache);
        authFilter.setAuthenticationManager(authManager);
        http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .antMatchers("/login", "/").permitAll()
                .antMatchers("/**").authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)
                .and().requestCache().requestCache(requestCache);
    }
}
