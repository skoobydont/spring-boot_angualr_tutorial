package com.ajskubak.projectname.security;

import static com.ajskubak.projectname.security.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static com.ajskubak.projectname.security.SecurityConstants.HEADER_STRING;
import static com.ajskubak.projectname.security.SecurityConstants.SECRET;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain fc) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if(header == null || !header.startsWith(TOKEN_PREFIX)) {
            fc.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        fc.doFilter(req,res);
    }
    //get authentication from http request
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
        String token = req.getHeader(HEADER_STRING);

        if(token!=null){
            //parse token
            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                                .build()
                                .verify(token.replace(TOKEN_PREFIX, ""))
                                .getSubject();
            if(user!=null){
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}