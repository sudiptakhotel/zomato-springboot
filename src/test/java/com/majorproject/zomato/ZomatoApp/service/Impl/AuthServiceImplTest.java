package com.majorproject.zomato.ZomatoApp.service.Impl;

import com.majorproject.zomato.ZomatoApp.TestContainerConfiguration;
import com.majorproject.zomato.ZomatoApp.dto.*;
import com.majorproject.zomato.ZomatoApp.entity.PartnerEntity;
import com.majorproject.zomato.ZomatoApp.entity.UserEntity;
import com.majorproject.zomato.ZomatoApp.entity.enums.Role;
import com.majorproject.zomato.ZomatoApp.exception.ResourceNotFoundException;
import com.majorproject.zomato.ZomatoApp.exception.RuntimeConflictException;
import com.majorproject.zomato.ZomatoApp.exception.UserAlreadyExistsException;
import com.majorproject.zomato.ZomatoApp.repository.UserRepository;
import com.majorproject.zomato.ZomatoApp.security.JwtService;
import com.majorproject.zomato.ZomatoApp.service.CustomerService;
import com.majorproject.zomato.ZomatoApp.service.PartnerService;
import com.majorproject.zomato.ZomatoApp.service.UserWalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Import(TestContainerConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private PasswordEncoder passwordEncoder;

    @Spy
    private ModelMapper modelMapper = new ModelMapper(); // Spy allows partial mocking with real instance

    @Mock
    private CustomerService customerService;

    @Mock
    private UserWalletService userWalletService;

    @Spy
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private PartnerService partnerService;

    @InjectMocks
    private  AuthServiceImpl authService;

    private SignUpDTO signUpDTO;
    private UserEntity userEntity;

    private String email;
    private String password;

    private Long userId;
    private PointDTO pointDTO;
    private PartnerEntity partnerEntity;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        // Initialize test data

        email = "test@example.com";
        password = "password123";
        signUpDTO = new SignUpDTO();
        signUpDTO.setEmail(email);
        signUpDTO.setPassword(password);

        userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setRole(Set.of(Role.CUSTOMER));

        userDTO = new UserDTO();
    }
    @Test
    void testSignUp_UserAlreadyExists() {
        // Given
        String email = "test@example.com";
        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setEmail(email);
        signUpDTO.setPassword("password123");

        UserEntity existingUser = new UserEntity();
        existingUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        // When & Then
        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class,
                () -> authService.signUp(signUpDTO));

        assertEquals("can not sign up , because user is already present with email : test@example.com", exception.getMessage());
        verify(userRepository, never()).save(any(UserEntity.class));
        verify(customerService, never()).createCustomer(any(UserEntity.class));
        verify(userWalletService, never()).createWalletForUser(any(UserEntity.class));
    }

    @Test
    void testSignUp_Success() {
        // Given
        String email = "test@example.com";
        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setEmail(email);
        signUpDTO.setPassword("password123");

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setPassword("encodedPassword");
        userEntity.setRole(Set.of(Role.CUSTOMER));

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(modelMapper.map(signUpDTO, UserEntity.class)).thenReturn(userEntity);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(modelMapper.map(userEntity, UserDTO.class)).thenReturn(new UserDTO());

        // When
        UserDTO result = authService.signUp(signUpDTO);

        // Then
        assertNotNull(result);
        verify(userRepository).findByEmail(email);
        verify(userRepository).save(userEntity);
        verify(customerService).createCustomer(userEntity);
        verify(userWalletService).createWalletForUser(userEntity);
    }

    @Test
    void testLogin_Success() {
        // Given
        String email = "test@example.com";
        String password = "password123";

        // Create a mock UserEntity
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        // Create a mock Authentication object
        Authentication authentication = new UsernamePasswordAuthenticationToken(userEntity, password);

        // Set up mocks
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(jwtService.generateAccessToken(userEntity)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(userEntity)).thenReturn("refreshToken");

        // When
        LoginResponseDTO response = authService.login(email, password);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateAccessToken(userEntity);
        verify(jwtService).generateRefreshToken(userEntity);
    }
    @Test
    void testOnBoardPartner_PartnerAlreadyExists() {
        // Given
        Long userId = 1L;
        PointDTO pointDTO = new PointDTO();
        PartnerEntity existingPartner = new PartnerEntity();
        existingPartner.setId(2L);

        when(partnerService.getPartnerByUserId(userId)).thenReturn(existingPartner);

        // When & Then
        RuntimeConflictException exception = assertThrows(RuntimeConflictException.class,
                () -> authService.onBoardPartner(userId, pointDTO));

        assertEquals("Partner is already onboarded with partnerId : 2", exception.getMessage());
        verify(userRepository, never()).findById(userId);
        verify(userRepository, never()).save(any(UserEntity.class));
        verify(partnerService, never()).createPartner(any(UserEntity.class), any(PointDTO.class));
    }

    @Test
    void testOnBoardPartner_UserNotFound() {
        // Given
        Long userId = 1L;
        PointDTO pointDTO = new PointDTO();

        when(partnerService.getPartnerByUserId(userId)).thenReturn(null);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> authService.onBoardPartner(userId, pointDTO));

        assertEquals("User not found with id : 1", exception.getMessage());
        verify(partnerService).getPartnerByUserId(userId);
        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any(UserEntity.class));
        verify(partnerService, never()).createPartner(any(UserEntity.class), any(PointDTO.class));
    }

    @Test
    void testOnBoardPartner_Success() {
        // Given
        Long userId = 1L;
        PointDTO pointDTO = new PointDTO();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setRole(Set.of(Role.CUSTOMER));

        PartnerEntity newPartner = new PartnerEntity();

        when(partnerService.getPartnerByUserId(userId)).thenReturn(null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(partnerService.createPartner(userEntity, pointDTO)).thenReturn(newPartner);
        when(modelMapper.map(newPartner, PartnerDTO.class)).thenReturn(new PartnerDTO());

        // When
        PartnerDTO result = authService.onBoardPartner(userId, pointDTO);

        // Then
        assertNotNull(result);
        assertEquals(new PartnerDTO(), result);
        verify(partnerService).getPartnerByUserId(userId);
        verify(userRepository).findById(userId);
        verify(userRepository).save(userEntity);
        verify(partnerService).createPartner(userEntity, pointDTO);
        verify(modelMapper).map(newPartner, PartnerDTO.class);
    }

    @Test
    void testOnBoardAdmin_Success() {
        // Given
        Long userId = 1L;
        userEntity.setRole(new HashSet<>(Set.of(Role.CUSTOMER))); // Ensure user is not already an admin
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(modelMapper.map(userEntity, UserDTO.class)).thenReturn(userDTO);

        // When
        UserDTO result = authService.onBoardAdmin(userId);

        // Then
        assertNotNull(result);
        assertTrue(userEntity.getRole().contains(Role.ADMIN));
        verify(userRepository).findById(userId);
        verify(userRepository).save(userEntity);
        verify(modelMapper).map(userEntity, UserDTO.class);
    }

    @Test
    void testOnBoardAdmin_UserNotFound() {
        // Given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            authService.onBoardAdmin(userId);
        });

        assertEquals("User not found with userId : " + userId, thrown.getMessage());
        verify(userRepository).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testOnBoardAdmin_UserAlreadyAdmin() {
        // Given
        Long userId = 1L;
        userEntity.setRole(new HashSet<>(Set.of(Role.ADMIN))); // User is already an admin
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        // When & Then
        RuntimeConflictException thrown = assertThrows(RuntimeConflictException.class, () -> {
            authService.onBoardAdmin(userId);
        });

        assertEquals("User is already an admin , with userId : " + userId, thrown.getMessage());
        verify(userRepository).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }








}