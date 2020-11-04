package com.parking.controllers;

import com.parking.models.security.login_msg.request.Login;
import com.parking.models.security.login_msg.response.JwtResponse;
import com.parking.models.security.user.User;
import com.parking.models.security.user.UserDTO;
import com.parking.models.security.utils.UserPrincipal;
import com.parking.services.security.JwtProvider;
import com.parking.services.security.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserRestController {
    @Autowired
    UserService userService;

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    AuthenticationManager authManager;

    private PasswordEncoder encoder;
    @Autowired
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody Login loginRequest) throws AuthenticationException {

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generatingJwt(authentication);
        System.out.println("Token is generated: " + token);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        System.out.println("UserDetails: " + userPrincipal.getUsername());

        JwtResponse response = new JwtResponse(
                userPrincipal.getId().toString(),
                token,
                userPrincipal.getUsername(),
                userPrincipal.getEmail(),
                userPrincipal.getAuthorities()
        );
        System.out.println(response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list-user")
    public ResponseEntity<List<User>> getListUser() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/add-user")
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDto, UriComponentsBuilder builder) {
        userService.save(userDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/user/{id}").buildAndExpand(userDto.getUserId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping("/count-user")
    public ResponseEntity<Integer> getBidderMax() {
        return new ResponseEntity<>(userService.countAllBy(), HttpStatus.OK);
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<User> findByIdDto(@PathVariable Integer id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }
}