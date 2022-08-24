package ru.bfad.handbook.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.bfad.handbook.beans.JwtTokenUtil;
import ru.bfad.handbook.dto.JwtRequest;
import ru.bfad.handbook.dto.JwtResponse;
import ru.bfad.handbook.exceptions.MarketError;
import ru.bfad.handbook.services.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest, Principal principal) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException ex) {
            return new ResponseEntity<>(new MarketError(HttpStatus.UNAUTHORIZED.value(), "Incorrect username or password"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        System.out.println("[" + authRequest.getUsername() + "]" + "[" + authRequest.getPassword() + "]");
//        System.out.println(token.toString());
//        System.out.println(Arrays.toString(userDetails.getAuthorities().toArray()));
        String admin =  null;
        List<String> list = new ArrayList<>();
        for (GrantedAuthority ga: userDetails.getAuthorities()) {
            System.out.println(ga.getAuthority());
            if(ga.getAuthority().equals("ROLE_ADMIN"))
                admin = "admin";
            return ResponseEntity.ok(new JwtResponse(token, admin));
        }
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
