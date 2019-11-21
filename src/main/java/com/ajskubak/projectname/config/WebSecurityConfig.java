package com.ajskubak.projectname.config;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private UserDetailsService jwtUserDetailsService;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
        // Use BCryptPasswordEncoder
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // @Override
    // protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //     auth.inMemoryAuthentication()
    //         .withUser("aj").password("{noop}password").roles("USER")
    //         .and()
    //         .withUser("abbie").password("{noop}passitword").roles("ADMIN");
    // }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                //dont authenticate this particular request
            .authorizeRequests().antMatchers("/authenticate").permitAll()
            // .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            //all other requests need to be authenticated
            .anyRequest().authenticated().and()
            //make sure we use stateless session; session won't be used
            // to store user's state
            .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //add filter to validate tokens with every request
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }
    // @Autowired
    // public CustomFilter customFilter() {
    //     return new CustomFilter();
    // }

    // @Bean
    // @Override
    // public UserDetailsService userDetailsService() {
    //     PasswordEncoder encoder = new BCryptPasswordEncoder();
    //     System.out.println(encoder.encode("password"));
    //     UserDetails user = 
    //         User.withDefaultPasswordEncoder()
    //             .username("user")
    //             .password("password")
    //             .roles("USER")
    //             .build();

    //     return new InMemoryUserDetailsManager(user);
    // }
}