package com.space.starship.Security;

import com.space.starship.utils.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JWTAuthorization extends BasicAuthenticationFilter {
    public JWTAuthorization(AuthenticationManager authenticationManager){
        super(authenticationManager);
    }

    private static UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(Constants.JWT_HEADER);

        if (token != null) {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Constants.KEY.getBytes())
                    .build()
                    .parseClaimsJws(token.replace(Constants.JWT_PREFIX_TOKEN, ""))
                    .getBody();

            String user = claims.getSubject();
            List<String> authorities = (List<String>) claims.get("authorities");
            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, authorities.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()));
            }
            return null;
        }
        return null;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader(Constants.JWT_HEADER);
        if(header == null || !header.startsWith(Constants.JWT_PREFIX_TOKEN)){
            chain.doFilter(request, response);
            return;
        }

        // Obtain data users throught token
        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);

        // User information is stored in context security
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }
}
