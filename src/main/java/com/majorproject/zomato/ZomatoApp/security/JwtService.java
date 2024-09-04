package com.majorproject.zomato.ZomatoApp.security;

import com.majorproject.zomato.ZomatoApp.entity.UserEntity;


public interface JwtService {

    String generateAccessToken(UserEntity user);
    String generateRefreshToken(UserEntity user);
    Long getUserIdFromToken(String token);
}
