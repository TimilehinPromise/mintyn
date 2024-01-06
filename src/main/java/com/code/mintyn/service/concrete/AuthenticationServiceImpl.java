package com.code.mintyn.service.concrete;


import com.code.mintyn.config.security.TokenAuthenticationService;
import com.code.mintyn.exception.MintynMartRuntimeException;
import com.code.mintyn.models.LoginResponseModel;
import com.code.mintyn.models.ResponseMessage;
import com.code.mintyn.models.dto.LoginDTO;
import com.code.mintyn.models.dto.UserCreateDTO;
import com.code.mintyn.persistence.entity.TokenStore;
import com.code.mintyn.persistence.entity.User;
import com.code.mintyn.persistence.repository.UserRepository;
import com.code.mintyn.service.abstracts.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final static String ERROR_MSG = "Invalid credentials";

    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    private final TokenAuthenticationService tokenService;

    @Override
    public LoginResponseModel login(LoginDTO loginForm) {
        User user = getUser(loginForm.getUserName());
        matchPassword(loginForm.getPassword(), user.getPassword(),user);
        userRepository.save(user);

        TokenStore store = tokenService.generatorToken(user);
        return  LoginResponseModel.builder()
                .token(store.getToken())
                .expires(store.getExpiredAt())
                .userName(user.getUsername())
                .build();
    }

    @Override
    public ResponseMessage signUp(UserCreateDTO userCreate){
        try {

            if (!userRepository.existsUserByUsername(userCreate.getUserName())){
          User savedCustomer =   userRepository.save(User.builder().
                    username(userCreate.getUserName())
                    .password(passwordEncoder.encode(userCreate.getPassword()))
                    .build());
                userRepository.save(savedCustomer);
        return ResponseMessage.builder().message("User Signed Up Successfully").build();

        }}

        catch (Exception e){
            log.info("error creating a customer"+ e);
        }

        return null;
    }




    private User getUser(String username) {
        Optional<User> optionalUser = userRepository.findFirstByUsername(username);
               if (optionalUser.isEmpty()){
                   throw invalidCredentialException();
               }
        User user = optionalUser.get();

        return user;
    }

    private MintynMartRuntimeException invalidCredentialException() {
        return new MintynMartRuntimeException(ERROR_MSG);
    }

    private void matchPassword(String plainPassword, String encryptedPassword,User user) {
        if (!passwordEncoder.matches(plainPassword, encryptedPassword)) {
            userRepository.save(user);
            throw invalidCredentialException();
        }
    }


}
