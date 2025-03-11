package com.twitter.Controller;

import com.twitter.Dto.RegisterRequest;
import com.twitter.Dto.RegisterResponse;
import com.twitter.Entity.User;
import com.twitter.Service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody RegisterRequest request) {
        String welcomeMessage = authenticationService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(welcomeMessage);
    }

    @PostMapping("/register")
    public RegisterResponse register(@Validated @RequestBody RegisterRequest request){

        User user = authenticationService.register(request.getEmail(), request.getPassword());

        return new RegisterResponse(user.getEmail(), "User successfully regitered!");
    }
}
