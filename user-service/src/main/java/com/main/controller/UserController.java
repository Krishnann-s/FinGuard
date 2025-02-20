package com.main.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/finguard")
public class UserController { 
	 private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	 
	@Autowired
	private UserService userservice;
	
	@Autowired
	private UserRepository userRepo;
	 
	@Autowired
    private AuthenticationManager authenticationManager;
	
	// registering new users
	@PostMapping("/user/register")
	public ResponseEntity<User> registerUsers(@RequestBody @Valid UserRegistrationRequest request)
	{
		 logger.info("Registering new user with username: {}", request.getUserName());
	        try {
	            User newUser = userservice.addNewUsers(request);
	            logger.info("added new User");
	            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
	        } catch (Exception e) {
	            logger.error("Failed to register user: {}", e.getMessage());
	            throw new InvalidRequestException("User registration failed. Please check the details and try again.");
	        }
	}
	
	// Login for existing user
	@PostMapping("/user/login")
    public ResponseDto getToken(@RequestBody AuthRequest authRequest) 
	{
		logger.info("Attempting to log in user: {}", authRequest.getUserName());
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
            if (authenticate.isAuthenticated()) {
                User user = userRepo.findByUserName(authRequest.getUserName()).orElseThrow(() -> new UserNotFoundException("User not found with username: " + authRequest.getUserName()));
                String token = userservice.generateToken(user.getUserId() + "", user.getRole());
                ResponseDto resDto = new ResponseDto();
                resDto.setToken(token);
                resDto.setRole(user.getRole());
                logger.info("User logged in successfully: {}", authRequest.getUserName());
                return resDto;
            } else {
                throw new InvalidRequestException("Invalid login credentials");
            }
        } catch (Exception e) {
            logger.error("Login failed: {}", e.getMessage());
            throw new InvalidRequestException("Login failed. Please check your credentials.");
        }
    }
	
	// Checking the token is valid
	@GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam("token") String token) {
		logger.info("Token validation attempt");
        userservice.validateToken(token);
        return new ResponseEntity<String>("Token is valid", HttpStatus.OK);
    }
	
	// Getting All Users
	@GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() 
	{
		logger.info("Fetching all users.");
        List<User> users = userservice.getAllUsers();
        if (users.isEmpty()) {
            throw new UserNotFoundException("No users found.");
        }
        logger.info("Found {} users.", users.size());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Get a user by ID 
	@GetMapping("/user/{userId}")
	public ResponseEntity<Object> getUserById(@PathVariable int userId) {
	    logger.info("Fetching user by ID: {}", userId);
	    User userById = userservice.getUserById(userId);
	    
	    if (userById == null) {
	        logger.error("User with ID {} not found", userId);
	        throw new UserNotFoundException("User with ID " + userId + " not found.");
	    }
	    
	    return new ResponseEntity<>(userById, HttpStatus.OK);
	}

    // Update a user by ID 
	@PutMapping("/user/{userId}")
	public ResponseEntity<Object> updateUser(@PathVariable int userId, @RequestBody UserProfileUpdateRequest request) {
	    logger.info("Updating user with ID: {}", userId);
	    User updatedUser = userservice.updateUserProfile(userId, request);

//	    if (updatedUser == null) {
//	        logger.error("User with ID {} not found, cannot update", userId);
//	        throw new UserNotFoundException("User with ID " + userId + " not found.");
//	    }
	    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}
    
    // Update Users Wallet after he Logins
	@PutMapping("/user/wallet/{userId}")
	public ResponseEntity<Object> updateUserWallet(@PathVariable int userId, @RequestBody UpdateWallet walletUpdateRequest) {
	    logger.info("Updating wallet for user with ID: {}", userId);
	    User updatedUser = userservice.updateUserWallet(userId, walletUpdateRequest.getAmount());
 
//	    if (updatedUser == null) {
//	        logger.error("User with ID {} not found, cannot update wallet", userId);
//	        throw new UserNotFoundException("User with ID " + userId + " not found.");
//	    }

	    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}

    // Delete a user by ID
	@DeleteMapping("/user/{userId}")
	public ResponseEntity<Object> deleteUser(@PathVariable int userId) {
	    logger.info("Deleting user with ID: {}", userId);
	    
	    boolean deleted = userservice.deleteUser(userId);
	    if (!deleted) {
	        logger.error("User with ID {} not found, cannot delete", userId);
	        throw new UserNotFoundException("User with ID " + userId + " not found.");
	    }

	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
