package com.space.starship.Controller;

import com.space.starship.Model.ErrorMessage;
import com.space.starship.utils.Constants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.stream.Collectors;

@RestController
@RequestMapping("auth")
@CrossOrigin(origins = "*")
public class LoginController {
    AuthenticationManager authenticationManager;
    public LoginController(AuthenticationManager authenticationManager){
        super();
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestParam(value = "user", required = true) String user,
                                   @RequestParam(value = "password", required = true) String password){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user, password));
            return ResponseEntity.status(HttpStatus.OK)
                    .body(getToken(authentication));
        } catch(AuthenticationException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized");
        }
    }

    private String getToken(Authentication authentication){
        String jwtToken =
                Jwts.builder()
                        .setIssuedAt(new Date())
                        .setSubject(authentication.getName())
                        .claim("authorities", authentication.getAuthorities()
                                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                        .setExpiration(new Date(System.currentTimeMillis() + Constants.EXPEDITION_TIME))
                        .signWith(Keys.hmacShaKeyFor(Constants.KEY.getBytes()))
                        .compact();
        System.out.println("Token generated!");
        return jwtToken;
    }
}
