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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.ArgumentMatchers.any;


import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    private final static String ERROR_MSG = "Invalid credentials";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenAuthenticationService tokenService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;


    private User user;
    private TokenStore tokenStore;
    private final String validUsername = "user";
    private final String validPassword = "mintyn_password";
    private final String validToken = "token";

    @BeforeEach
    void setUp() {
        user = new User(validUsername, passwordEncoder.encode(validPassword));
        tokenStore = new TokenStore(validToken, LocalDateTime.now());
    }

    @Test
    void login_WithValidCredentials_ShouldReturnLoginResponse() {
        LoginDTO loginDTO = new LoginDTO(validUsername, validPassword);

        when(userRepository.findFirstByUsername(validUsername)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(validPassword, user.getPassword())).thenReturn(true);
        when(tokenService.generatorToken(user)).thenReturn(tokenStore);

        LoginResponseModel response = authenticationService.login(loginDTO);

        assertNotNull(response);
        assertEquals(validUsername, response.getUserName());
        assertEquals(validToken, response.getToken());
    }

    @Test
    void login_WithInvalidCredentials_UserNotFound_ShouldThrowException() {
        LoginDTO loginDTO = new LoginDTO(validUsername, validPassword);

        when(userRepository.findFirstByUsername(validUsername)).thenReturn(Optional.empty());

        assertThrows(MintynMartRuntimeException.class, () -> authenticationService.login(loginDTO));
    }

    @Test
    void signUp_WithNewUser_ShouldCreateUser() {
        UserCreateDTO userCreateDTO = new UserCreateDTO("newUser", validPassword);

        when(userRepository.existsUserByUsername("newUser")).thenReturn(false);

        ResponseMessage response = authenticationService.signUp(userCreateDTO);

        assertNotNull(response);
        assertEquals("User Signed Up Successfully", response.getMessage());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void signUp_WithExistingUser_ShouldNotCreateUser() {
        UserCreateDTO userCreateDTO = new UserCreateDTO("existingUser", validPassword);

        when(userRepository.existsUserByUsername("existingUser")).thenReturn(true);

        ResponseMessage response = authenticationService.signUp(userCreateDTO);

        assertNull(response);
        verify(userRepository, never()).save(any(User.class));
    }

}