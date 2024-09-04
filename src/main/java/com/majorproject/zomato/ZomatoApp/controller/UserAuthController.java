package com.majorproject.zomato.ZomatoApp.controller;

import com.majorproject.zomato.ZomatoApp.dto.LoginRequestDTO;
import com.majorproject.zomato.ZomatoApp.dto.LoginResponseDTO;
import com.majorproject.zomato.ZomatoApp.dto.SignUpDTO;
import com.majorproject.zomato.ZomatoApp.dto.UserDTO;
import com.majorproject.zomato.ZomatoApp.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping(path = "/user/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final AuthService authService;

    @PostMapping(path = "/signUp")
    public ResponseEntity<UserDTO> signUp(@RequestBody SignUpDTO signUpDTO) {
        return new ResponseEntity<>(authService.signUp(signUpDTO) , HttpStatus.CREATED);
    }

    @PostMapping(path ="/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginRequestDTO loginRequestDTO,
                                                      HttpServletResponse httpServletResponse) {

        LoginResponseDTO loginResponse = authService.login(loginRequestDTO.getEmail(),
                loginRequestDTO.getPassword());

        //set the refreshToken in client's cookie
        Cookie cookie = new Cookie("refreshToken" , loginResponse.getRefreshToken());
        cookie.setHttpOnly(true);
        httpServletResponse.addCookie(cookie);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping(path = "/refreshToken")
    public ResponseEntity<LoginResponseDTO> refreshToken(HttpServletRequest httpServletRequest) {

        //extract the refreshToken from client's cookie
        Cookie[] cookies = httpServletRequest.getCookies();

        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        LoginResponseDTO loginResponse = authService.getNewAccessToken(refreshToken);

        return ResponseEntity.ok(loginResponse);
    }

}
