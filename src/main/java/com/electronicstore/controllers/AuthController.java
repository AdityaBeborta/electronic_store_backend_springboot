package com.electronicstore.controllers;

import com.electronicstore.dtos.UserDto;
import com.electronicstore.jwt.JWTHelper;
import com.electronicstore.jwt.JwtRequest;
import com.electronicstore.jwt.JwtResponse;
import com.electronicstore.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/electronicstore/auth/v1")
@Tag(name = "Authentication Controller", description = "consists APIs which are used for authentication")
@SecurityRequirement(name = "scheme default")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTHelper jwtHelper;

    @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/generate-token")
    public ResponseEntity<JwtResponse> doLogin(@RequestBody JwtRequest jwtRequest) {
        logger.info("doLogin method is triggered");
        this.doAuthenticate(jwtRequest.getEmail(), jwtRequest.getPassword());
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtRequest.getEmail());
        //find user details
        UserDto userByEmail = this.userService.getUserByEmail(userDetails.getUsername());
        //generate token
        String generatedToken = jwtHelper.generateToken(userDetails);
        JwtResponse jwtResponse = JwtResponse.builder().jwtToken(generatedToken).user(userByEmail).build();
        return ResponseEntity.ok(jwtResponse);
    }

    public void doAuthenticate(String username, String password) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
            authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("invalid username or password !!");
        }
    }
}
