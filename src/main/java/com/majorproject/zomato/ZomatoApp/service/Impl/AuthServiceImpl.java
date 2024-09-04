package com.majorproject.zomato.ZomatoApp.service.Impl;

import com.majorproject.zomato.ZomatoApp.dto.*;
import com.majorproject.zomato.ZomatoApp.entity.PartnerEntity;
import com.majorproject.zomato.ZomatoApp.entity.UserEntity;
import com.majorproject.zomato.ZomatoApp.entity.enums.Role;
import com.majorproject.zomato.ZomatoApp.exception.ResourceNotFoundException;
import com.majorproject.zomato.ZomatoApp.exception.RuntimeConflictException;
import com.majorproject.zomato.ZomatoApp.exception.UserAlreadyExistsException;
import com.majorproject.zomato.ZomatoApp.repository.UserRepository;
import com.majorproject.zomato.ZomatoApp.security.JwtService;
import com.majorproject.zomato.ZomatoApp.service.AuthService;
import com.majorproject.zomato.ZomatoApp.service.CustomerService;
import com.majorproject.zomato.ZomatoApp.service.PartnerService;
import com.majorproject.zomato.ZomatoApp.service.UserWalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final CustomerService customerService;
    private final UserWalletService userWalletService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PartnerService partnerService;

    @Override
    @Transactional
    public UserDTO signUp(SignUpDTO signUpDTO) {

        //check if user is already present in database
        UserEntity user = userRepository.findByEmail(signUpDTO.getEmail())
                .orElse(null);

        if(user != null)
            throw new UserAlreadyExistsException("can not sign up , because " +
                    "user is already present with email : "+user.getEmail());

        UserEntity mappedUser = modelMapper.map(signUpDTO , UserEntity.class);

        //encode password before save

        String givenPass = signUpDTO.getPassword();

        String password = passwordEncoder.encode(givenPass);
        mappedUser.setPassword(password);

        //by default every user is a customer
        mappedUser.setRole(Set.of(Role.CUSTOMER));

        UserEntity savedUser = userRepository.save(mappedUser);

        //create user related entity -> customer , Wallet
        customerService.createCustomer(savedUser);
        userWalletService.createWalletForUser(savedUser);

        return modelMapper.map(savedUser , UserDTO.class);

    }

    @Override
    public LoginResponseDTO login(String email, String password) {

        //1 -> authenticate user by Spring security authentication manager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email , password)
        );

        //2 -> generate accessToken and refreshToken
        UserEntity user = (UserEntity) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponseDTO(user.getId() , accessToken , refreshToken);
    }

    @Override
    public PartnerDTO onBoardPartner(Long userId, PointDTO pointDTO) {

        //check if this user is already a partner
        PartnerEntity partner = partnerService.getPartnerByUserId(userId);

        if(partner != null)
            throw new RuntimeConflictException("Partner is already onboarded with " +
                    "partnerId : "+partner.getId());

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found " +
                        "with id : "+userId));
        Set<Role> roles = new HashSet<>(user.getRole());
        roles.add(Role.PARTNER);

        UserEntity savedUser = userRepository.save(user);

        //create partner entity
        PartnerEntity newPartner = partnerService.createPartner(savedUser , pointDTO);

        return modelMapper.map(newPartner , PartnerDTO.class);


    }

    @Override
    public LoginResponseDTO getNewAccessToken(String refreshToken) {

        Long userId = jwtService.getUserIdFromToken(refreshToken);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found" +
                        " with id : "+userId));

        String accessToken = jwtService.generateAccessToken(user);
        return new LoginResponseDTO(userId , accessToken , refreshToken);
    }

    @Override
    public UserDTO onBoardAdmin(Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with " +
                        "userId : "+userId));

        //check if it is already an admin or not
        boolean isAdmin = user.getRole().stream()
                .anyMatch(role -> role.equals(Role.ADMIN));

        if(isAdmin)
            throw new RuntimeConflictException("User is already an admin , with " +
                    "userId : "+userId);

        user.getRole().add(Role.ADMIN);
        UserEntity savedUser = userRepository.save(user);

        return modelMapper.map(savedUser , UserDTO.class);

    }
}
