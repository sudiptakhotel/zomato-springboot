package com.majorproject.zomato.ZomatoApp.service;

import com.majorproject.zomato.ZomatoApp.dto.*;

public interface AuthService {

    UserDTO signUp(SignUpDTO signUpDTO);
    LoginResponseDTO login(String email , String password);
    PartnerDTO onBoardPartner(Long userId , PointDTO pointDTO);
    LoginResponseDTO getNewAccessToken(String refreshToken);

    UserDTO onBoardAdmin(Long userId);
}
