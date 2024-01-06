package com.code.mintyn.service.abstracts;

import com.code.mintyn.models.LoginResponseModel;
import com.code.mintyn.models.ResponseMessage;
import com.code.mintyn.models.dto.LoginDTO;
import com.code.mintyn.models.dto.UserCreateDTO;

public interface AuthenticationService {
    LoginResponseModel login(LoginDTO loginForm);


    ResponseMessage signUp(UserCreateDTO userCreate);
}
