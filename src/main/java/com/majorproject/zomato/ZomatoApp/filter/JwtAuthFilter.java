package com.majorproject.zomato.ZomatoApp.filter;

import com.majorproject.zomato.ZomatoApp.entity.UserEntity;
import com.majorproject.zomato.ZomatoApp.security.JwtService;
import com.majorproject.zomato.ZomatoApp.service.UserService;
import jakarta.persistence.Column;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {

            String token = request.getHeader("Authorization");
            //check if the token is valid
            if (token == null || !token.startsWith("Bearer ")) {

                //move to next filter available in filterChain
                filterChain.doFilter(request , response);
                return;
            }

            //extract the accessToken
            String accessToken = token.split("Bearer ")[1];

            //validate if this is a valid token
            Long userId = jwtService.getUserIdFromToken(accessToken);

            UserEntity user = userService.getUserById(userId);

            //check if user is not null and security context holder is empty
            if(user != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(user , null , user.getAuthorities());

                //set the user in security context holder
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request , response , null , exception);
        }

        //move ahead with next filter
        filterChain.doFilter(request , response);

    }
}
