package com.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;

import com.main.controller.UserController;
import com.main.dto.AuthRequest;
import com.main.dto.ResponseDto;
import com.main.dto.UpdateWallet;
import com.main.dto.UserProfileUpdateRequest;
import com.main.dto.UserRegistrationRequest;
import com.main.entity.User;
import com.main.exception.InvalidRequestException;
import com.main.exception.UserNotFoundException;
import com.main.repository.UserRepository;
import com.main.service.UserService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserControllerApplicationTests {

	@InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;
    
    @Mock
    private UserRepository userRepo;
    
    @Mock
    private AuthenticationManager authenticationManager;

    private UserRegistrationRequest userRequest;
    
    private AuthRequest authRequest;
    
    private String validToken;
    
    private List<User> userList;
    
    private User updatedUser;
    private UserProfileUpdateRequest updateRequest;
    
    private UpdateWallet walletUpdateRequest;
    private User user;

    @BeforeEach
    public void setUp() {
        userRequest = new UserRegistrationRequest("testUser", "Password1!", "test@example.com", "user");
        authRequest = new AuthRequest("testUser", "Password1!");
        validToken = "validToken123";
        userList = Arrays.asList(
        		new User(1, "user1", "password1", "user1@example.com", "user", BigDecimal.ZERO, LocalDate.now(), LocalDate.now()),
                new User(2, "user2", "password2", "user2@example.com", "admin", BigDecimal.ZERO, LocalDate.now(), LocalDate.now())
        );
        user = new User(1, "user1", "password1", "user1@example.com", "user", BigDecimal.ZERO, LocalDate.now(), LocalDate.now());
        updatedUser = new User(1, "updatedUser", "newPassword", "updated@example.com", "user", BigDecimal.ZERO, LocalDate.now(), LocalDate.now());
        updateRequest = new UserProfileUpdateRequest("updatedUser", "updated@example.com", "user", BigDecimal.ZERO);
        walletUpdateRequest = new UpdateWallet(BigDecimal.valueOf(50));  // Assuming amount is BigDecimal
        
    }

    // positive test case for register user
    @Test
    public void registerUsers_Success() {
        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setUserName(userRequest.getUserName());
        mockUser.setEmail(userRequest.getEmail());
        mockUser.setRole(userRequest.getRole());

        when(userService.addNewUsers(any(UserRegistrationRequest.class))).thenReturn(mockUser);

        ResponseEntity<User> response = userController.registerUsers(userRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
    }
    
    // Negative test case for register user
    @Test
    public void registerUsers_Failure() {
        when(userService.addNewUsers(any(UserRegistrationRequest.class)))
            .thenThrow(new RuntimeException("User registration failed."));

        assertThrows(InvalidRequestException.class, () -> {
            userController.registerUsers(userRequest);
        });
    }
    
    // Positive test case for token
    @Test
    public void getToken_Success() {
        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setUserName(authRequest.getUserName());
        mockUser.setRole("user");

        // Mock AuthenticationManager and Authentication
        Authentication mockAuth = Mockito.mock(Authentication.class);
        when(mockAuth.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);

        when(userRepo.findByUserName(authRequest.getUserName())).thenReturn(Optional.of(mockUser));
        when(userService.generateToken("1", "user")).thenReturn("mockToken");

        ResponseDto responseDto = userController.getToken(authRequest);

        assertEquals("mockToken", responseDto.getToken());
        assertEquals("user", responseDto.getRole());
    }
    
    // Negative Test Case
    @Test
    public void getToken_Failure_InvalidCredentials() {
        // Mock AuthenticationManager to throw an exception when authentication fails
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Invalid login credentials"));

        assertThrows(InvalidRequestException.class, () -> {
            userController.getToken(authRequest);
        });
    }

    @Test
    public void getToken_Failure_UserNotFound() {
        // Mock the AuthenticationManager to return a valid authentication object
        Authentication mockAuth = Mockito.mock(Authentication.class);
        when(mockAuth.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);

        // Mock the userRepo to return an empty Optional when the username is not found
        when(userRepo.findByUserName(authRequest.getUserName())).thenReturn(Optional.empty());

        // Assert that an InvalidRequestException is thrown when the user is not found
        assertThrows(InvalidRequestException.class, () -> {
            userController.getToken(authRequest);
        });
    }
    
    // positive test case for token validation
    @Test
    public void validateToken_Success() {
        // Mocking the validateToken method to do nothing for valid tokens
        doNothing().when(userService).validateToken(validToken);

        ResponseEntity<String> response = userController.validateToken(validToken);

        // Assert that the response status is OK and the body is as expected
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Token is valid", response.getBody());
    }
    
    // Negative Test case for token validation
    @Test
    public void validateToken_Failure_InvalidToken() {
        // Mocking the validateToken method to throw an exception for invalid tokens
        doThrow(new InvalidRequestException("Invalid token")).when(userService).validateToken(anyString());

        // Assert that an InvalidRequestException is thrown when an invalid token is validated
        assertThrows(InvalidRequestException.class, () -> {
            userController.validateToken("invalidToken");
        });
    }
    
    // Positive ALl users checking
    @Test
    public void getAllUsers_Success() {
        // Mocking the getAllUsers method to return a list of users
        when(userService.getAllUsers()).thenReturn(userList);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        // Assert that the response status is OK and the body matches the user list
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userList, response.getBody());
    }
    
    // Negative
    @Test
    public void getAllUsers_Failure_NoUsersFound() {
        // Mocking the getAllUsers method to return an empty list
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        // Assert that a UserNotFoundException is thrown when no users are found
        assertThrows(UserNotFoundException.class, () -> {
            userController.getAllUsers();
        });
    }
    
    @Test
    public void getUserById_Success() {
        // Mocking the getUserById method to return a user
        when(userService.getUserById(1)).thenReturn(user);

        ResponseEntity<Object> response = userController.getUserById(1);

        // Assert that the response status is OK and the body matches the user
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }
    
    @Test
    public void getUserById_Failure_UserNotFound() {
        // Mocking the getUserById method to return null
        when(userService.getUserById(1)).thenReturn(null);

        // Assert that a UserNotFoundException is thrown when the user is not found
        assertThrows(UserNotFoundException.class, () -> {
            userController.getUserById(1);
        });
    }
    
    // positive
    @Test
    public void updateUser_Success() {
        // Mocking the updateUserProfile method to return an updated user
        when(userService.updateUserProfile(anyInt(), any(UserProfileUpdateRequest.class))).thenReturn(updatedUser);

        ResponseEntity<Object> response = userController.updateUser(1, updateRequest);

        // Assert that the response status is OK and the body matches the updated user
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }
    
    // Negative
    @Test
    public void updateUser_Failure_UserNotFound() {
        // Mocking the updateUserProfile method to return null
        when(userService.updateUserProfile(anyInt(), any(UserProfileUpdateRequest.class))).thenReturn(null);

        // Assert that a UserNotFoundException is thrown when the user is not found
        assertThrows(UserNotFoundException.class, () -> {
            userController.updateUser(1, updateRequest);
        });
    }
    
    @Test
    public void updateUserWallet_Success() {
        // Mocking the updateUserWallet method to return an updated user
        when(userService.updateUserWallet(anyInt(), any(BigDecimal.class))).thenReturn(updatedUser);

        ResponseEntity<Object> response = userController.updateUserWallet(1, walletUpdateRequest);

        // Assert that the response status is OK and the body matches the updated user
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }
    
    @Test
    public void updateUserWallet_Failure_UserNotFound() {
        // Mocking the updateUserWallet method to return null
        when(userService.updateUserWallet(anyInt(), any(BigDecimal.class))).thenReturn(null);

        // Assert that a UserNotFoundException is thrown when the user is not found
        assertThrows(UserNotFoundException.class, () -> {
            userController.updateUserWallet(1, walletUpdateRequest);
        });
    }
    
    @Test
    public void deleteUser_Success() {
        // Mocking the deleteUser method to return true
        when(userService.deleteUser(anyInt())).thenReturn(true);

        ResponseEntity<Object> response = userController.deleteUser(1);

        // Assert that the response status is NO_CONTENT
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    
    @Test
    public void deleteUser_Failure_UserNotFound() {
        // Mocking the deleteUser method to return false
        when(userService.deleteUser(anyInt())).thenReturn(false);

        // Assert that a UserNotFoundException is thrown when the user is not found
        assertThrows(UserNotFoundException.class, () -> {
            userController.deleteUser(1);
        });
    }
}
