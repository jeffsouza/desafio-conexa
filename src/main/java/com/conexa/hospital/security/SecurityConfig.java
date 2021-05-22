package com.conexa.hospital.security;

import com.conexa.hospital.security.filters.JwtRequestFilter;
import com.conexa.hospital.services.MedicoAuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MedicoAuthService medicoAuthService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Value("${application.version}")
    private String applicationVersion;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(medicoAuthService);
    }

    protected void configure(HttpSecurity http) throws Exception {
        String applicationVersion = "/" + this.applicationVersion;

        http.csrf().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, applicationVersion + "/pacientes")
            .permitAll()
            .antMatchers(applicationVersion + "/medicos/authenticate")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}