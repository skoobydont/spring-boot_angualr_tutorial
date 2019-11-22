package com.ajskubak.projectname.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajskubak.projectname.model.UserModel;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.ajskubak.projectname.security.SecurityConstants.EXPIRATION_TIME;
import static com.ajskubak.projectname.security.SecurityConstants.HEADER_STRING;
import static com.ajskubak.projectname.security.SecurityConstants.SECRET;
import static com.ajskubak.projectname.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authManager = authenticationManager;
    }

	@Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws RuntimeException {
        try {
            UserModel creds = new ObjectMapper().readValue(req.getInputStream(), UserModel.class);
            return authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    creds.getUsername(),
                    creds.getPassword(),
                    new ArrayList<>())
                );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain fc, Authentication auth) {
        String token = JWT.create()
                        .withSubject(((User) auth.getPrincipal()).getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                        .sign(HMAC512(SECRET.getBytes()));
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}