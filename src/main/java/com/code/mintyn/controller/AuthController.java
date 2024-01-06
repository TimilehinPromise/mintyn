package com.code.mintyn.controller;


import com.code.mintyn.models.LoginResponseModel;
import com.code.mintyn.models.ResponseMessage;
import com.code.mintyn.models.dto.LoginDTO;
import com.code.mintyn.models.dto.UserCreateDTO;
import com.code.mintyn.service.abstracts.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequestMapping(value = "v1/api/auth", produces = APPLICATION_JSON_VALUE)
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public LoginResponseModel login(@Valid @RequestBody LoginDTO loginForm) {
        return authenticationService.login(loginForm);
    }

    @PostMapping("/signup")
    public ResponseMessage signUp(@Valid @RequestBody UserCreateDTO userCreate) {
        log.info("customer signup ".concat( userCreate.toString()));
        return authenticationService.signUp(userCreate);
    }

}
