package com.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;


import com.main.controller.UserController;
import com.main.dto.UserProfileUpdateRequest;
import com.main.dto.UserRegistrationRequest;
import com.main.entity.User;
import com.main.exception.UserNotFoundException;
import com.main.repository.UserRepository;
import com.main.service.JwtService;
import com.main.service.UserService;
import com.main.service.UserServiceImpl;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceApplicationTests {

	@InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepo;
    
    @Mock
    private UserController userController;

    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtService jwtservice;

    @Test
    public void testAddNewUsers_success() {
        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest("testUser", "testPassword", "test@example.com", "USER");

        User user = new User();
        user.setUserId(1);
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        // Mocking password encoding
        Mockito.when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // Mocking repository save
        Mockito.when(userRepo.save(Mockito.any(User.class))).thenReturn(user);

       
        // Act
        User savedUser = userService.addNewUsers(request);

        // Assert
        assertEquals("testUser", savedUser.getUserName());
        assertEquals("test@example.com", savedUser.getEmail());
        Mockito.verify(userRepo, Mockito.times(1)).save(Mockito.any(User.class));
    }
    
    @Test
    public void testAddNewUsers_emailSendingFails() {
        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest("testUser", "testPassword", "test@example.com", "USER");

        User user = new User();
        user.setUserId(1);
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        // Mocking password encoding
        Mockito.when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // Mocking repository save
        Mockito.when(userRepo.save(Mockito.any(User.class))).thenReturn(user);

        // Act
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.addNewUsers(request);
        });

        // Assert
        assertEquals("Failed to send account creation email to test@example.com", exception.getMessage());
        Mockito.verify(userRepo, Mockito.times(1)).save(Mockito.any(User.class));
    }
    
    // Positive testcase for all users
    @Test
    public void testGetAllUsers_success() {
        // Arrange
        User user1 = new User();
        user1.setUserId(1);
        user1.setUserName("user1");
        user1.setEmail("user1@example.com");
        user1.setRole("USER");

        User user2 = new User();
        user2.setUserId(2);
        user2.setUserName("user2");
        user2.setEmail("user2@example.com");
        user2.setRole("USER");

        List<User> userList = Arrays.asList(user1, user2);

        // Mocking repository to return a list of users
        Mockito.when(userRepo.findAll()).thenReturn(userList);

        // Act
        List<User> allUsers = userService.getAllUsers();

        // Assert
        assertEquals(2, allUsers.size());
        assertEquals("user1", allUsers.get(0).getUserName());
        assertEquals("user2", allUsers.get(1).getUserName());
        Mockito.verify(userRepo, Mockito.times(1)).findAll();
    }

    // Negative for All users
    @Test
    public void testGetAllUsers_noUsersFound() {
        // Arrange
        List<User> emptyList = Collections.emptyList();

        // Mocking repository to return an empty list
        Mockito.when(userRepo.findAll()).thenReturn(emptyList);

        // Act & Assert
        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getAllUsers();
        });

        // Assert
        assertEquals("No users found", exception.getMessage());
        Mockito.verify(userRepo, Mockito.times(1)).findAll();
    }
    
    // Positive test case for updating user data
    @Test
    public void testUpdateUserProfile_success() {
        // Arrange
        int userId = 1;
        UserProfileUpdateRequest updateRequest = new UserProfileUpdateRequest("updatedUser", "updated@example.com", "ADMIN", BigDecimal.valueOf(1000.00));

        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setUserName("testUser");
        existingUser.setEmail("test@example.com");
        existingUser.setRole("USER");
        existingUser.setWallet(BigDecimal.valueOf(500.00));

        // Mocking repository methods
        Mockito.when(userRepo.findById(userId)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepo.save(Mockito.any(User.class))).thenReturn(existingUser);

        // Act
        User updatedUser = userService.updateUserProfile(userId, updateRequest);

        // Assert
        assertEquals("updatedUser", updatedUser.getUserName());
        assertEquals("updated@example.com", updatedUser.getEmail());
        assertEquals("ADMIN", updatedUser.getRole());
        assertEquals(BigDecimal.valueOf(1000.00), updatedUser.getWallet());
        Mockito.verify(userRepo, Mockito.times(1)).findById(userId);
        Mockito.verify(userRepo, Mockito.times(1)).save(existingUser);
    }
    
    // Negative test case for updating user data
    @Test
    public void testUpdateUserProfile_userNotFound() {
        // Arrange
        int userId = 1;
        UserProfileUpdateRequest updateRequest = new UserProfileUpdateRequest("updatedUser", "updated@example.com", "ADMIN", BigDecimal.valueOf(1000.00));

        // Mocking repository methods
        Mockito.when(userRepo.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.updateUserProfile(userId, updateRequest);
        });

        assertEquals("User profile not found with id: " + userId, exception.getMessage()); // Updated assertion
        Mockito.verify(userRepo, Mockito.times(1)).findById(userId);
        Mockito.verify(userRepo, Mockito.never()).save(Mockito.any(User.class)); // save should not be called
    }
    
    // positive test case for user by id
    @Test
    public void testGetUserById_success() {
        // Arrange
        int userId = 1;
        User user = new User();
        user.setUserId(userId);
        user.setUserName("testUser");
        user.setEmail("test@example.com");
        user.setRole("USER");
        user.setWallet(BigDecimal.valueOf(500.00));

        // Mocking repository method to return the user
        Mockito.when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        // Act
        User retrievedUser = userService.getUserById(userId);

        // Assert
        assertEquals(userId, retrievedUser.getUserId());
        assertEquals("testUser", retrievedUser.getUserName());
        assertEquals("test@example.com", retrievedUser.getEmail());
        assertEquals("USER", retrievedUser.getRole());
        assertEquals(BigDecimal.valueOf(500.00), retrievedUser.getWallet());
        Mockito.verify(userRepo, Mockito.times(1)).findById(userId);
    }

    // Negative test case for user by id
    @Test
    public void testGetUserById_userNotFound() {
        // Arrange
        int userId = 1;

        // Mocking repository method to return empty
        Mockito.when(userRepo.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(userId);
        });

        // Assert the exception message
        assertEquals("User profile not found with id: " + userId, exception.getMessage());
        Mockito.verify(userRepo, Mockito.times(1)).findById(userId);
    }

    // Positive Test Case for Deleting user
    @Test
    public void testDeleteUser_success() {
        // Arrange
        int userId = 1;
        User user = new User();
        user.setUserId(userId);
        user.setUserName("testUser");

        // Mocking repository method to return the user
        Mockito.when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.deleteUser(userId);

        // Assert
        assertTrue(result); // Ensure the return value is true
        Mockito.verify(userRepo, Mockito.times(1)).findById(userId);
        Mockito.verify(userRepo, Mockito.times(1)).delete(user); // Verify delete was called
    }

    // Negative Test Case for deleting user
    @Test
    public void testDeleteUser_userNotFound() {
        // Arrange
        int userId = 1;

        // Mocking repository method to return empty
        Mockito.when(userRepo.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(userId);
        });

        // Assert the exception message
        assertEquals("User profile not found with id: " + userId, exception.getMessage());
        Mockito.verify(userRepo, Mockito.times(1)).findById(userId);
        Mockito.verify(userRepo, Mockito.never()).delete(Mockito.any(User.class)); // Ensure delete was not called
    }

    // positive Test case for updating user
    @Test
    public void testUpdateUserWallet_success() {
        // Arrange
        int userId = 1;
        BigDecimal amount = BigDecimal.valueOf(100.00);

        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setUserName("testUser");
        existingUser.setEmail("test@example.com");
        existingUser.setWallet(BigDecimal.valueOf(500.00));

        // Mocking repository methods
        Mockito.when(userRepo.findById(userId)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepo.save(Mockito.any(User.class))).thenReturn(existingUser);
        
        
        // Act
        User updatedUser = userService.updateUserWallet(userId, amount);

        // Assert
        assertEquals(BigDecimal.valueOf(600.00), updatedUser.getWallet()); // Ensure the wallet was updated correctly
        Mockito.verify(userRepo, Mockito.times(1)).findById(userId);
        Mockito.verify(userRepo, Mockito.times(1)).save(existingUser); // Verify save was called
    }

    // Negative Test case for updating user
    @Test
    public void testUpdateUserWallet_negativeAmount() {
        // Arrange
        int userId = 1;
        BigDecimal amount = BigDecimal.valueOf(-600.00);

        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setUserName("testUser");
        existingUser.setEmail("test@example.com");
        existingUser.setWallet(BigDecimal.valueOf(500.00));

        // Mocking repository methods
        Mockito.when(userRepo.findById(userId)).thenReturn(Optional.of(existingUser));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUserWallet(userId, amount);
        });

        // Assert the exception message
        assertEquals("Cannot set wallet to a negative amount", exception.getMessage());
        Mockito.verify(userRepo, Mockito.times(1)).findById(userId);
        Mockito.verify(userRepo, Mockito.never()).save(Mockito.any(User.class)); // Ensure save was not called
    }
    
    @Test
    public void testGenerateToken_success() {
        // Arrange
        String userName = "testUser";
        String role = "USER";
        String expectedToken = "generatedToken";

        // Mocking the jwtService to return the expected token
        Mockito.when(jwtservice.generateToken(userName, role)).thenReturn(expectedToken);

        // Act
        String actualToken = userService.generateToken(userName, role);

        // Assert
        assertEquals(expectedToken, actualToken); // Check if the generated token matches the expected token
        Mockito.verify(jwtservice, Mockito.times(1)).generateToken(userName, role); // Verify that jwtService's generateToken was called once
    }
    
    // Negative test case for token generation
    @Test
    public void testValidateToken_invalidToken() {
        // Arrange
        String token = "invalidToken";

        // Mocking the jwtService to throw an exception when validating an invalid token
        Mockito.doThrow(new RuntimeException("Invalid token")).when(jwtservice).validateToken(token);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.validateToken(token);
        });

        // Assert the exception message
        assertEquals("Invalid token", exception.getMessage());
        Mockito.verify(jwtservice, Mockito.times(1)).validateToken(token); // Verify that validateToken was called once
    }




}
