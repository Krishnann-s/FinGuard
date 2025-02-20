package com.main.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.main.service.JwtService;

import jakarta.transaction.Transactional;

import com.main.dto.UserProfileUpdateRequest;
import com.main.dto.UserRegistrationRequest;
import com.main.entity.User;
import com.main.exception.InvalidRequestException;
import com.main.exception.NotificationServiceException;
import com.main.exception.UserNotFoundException;
import com.main.repository.UserRepository;


@Service 
@Transactional
public class UserServiceImpl implements UserService{
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	private static final String USER_NOT_FOUND_MSG = "User profile not found with id: ";
	
	@Autowired
    private JwtService jwtService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepo;
	
	
	@Override
//	@CircuitBreaker(name = "notificationServiceCircuitBreaker", fallbackMethod = "myFallBackMethod")
	public User addNewUsers(UserRegistrationRequest addUsers) 
	{
		logger.info("Adding a new user with username: {}", addUsers.getUserName());
		
		// Check if user already exists
		User user = new User();
		user.setUserName(addUsers.getUserName());
		user.setPassword(passwordEncoder.encode(addUsers.getPassword()));
        user.setEmail(addUsers.getEmail());
        user.setRole(addUsers.getRole());
        
        User savedUser = userRepo.save(user);
        
        logger.debug("{} saved successfully with id: {}", savedUser.getRole(),savedUser.getUserId());
        

        return savedUser; 
	}

	@Override
	public List<User> getAllUsers() 
	{	
		logger.info("Fetching all users from the database.");
		List<User> allUsers = userRepo.findAll();
		if(allUsers.isEmpty())
		{
			throw new UserNotFoundException("No users found");
		}
		return allUsers;
	}

	@Override
	public User updateUserProfile(int userId, UserProfileUpdateRequest u) {
		
		logger.info("Updating profile for user with id: {}", userId);
		
		// first checking is user present
		User existingUser = userRepo.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MSG + userId));
		
		existingUser.setUserName(u.getUserName());
		existingUser.setEmail(u.getEmail());
		existingUser.setRole(u.getRole());
		existingUser.setWallet(u.getWallet());
		existingUser.setUpdatedAt(LocalDate.now()); // updating the date
		// save the updates and return it
		User updatedUser = userRepo.save(existingUser);
		logger.info("User profile updated successfully for userId: {}", userId);
			return updatedUser;
		}

	@Override
	public User getUserById(int userId) {
	    logger.info("Fetching Profile by id: {}", userId);
	    
	    // Negative flow for null or negative ID
	    if (userId <= 0) {
	        logger.error("Invalid user ID: {}", userId);
	        throw new IllegalArgumentException("User ID must be a positive integer.");
	    }
	    
	    User userEntity = userRepo.findById(userId)
	        .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MSG + userId));
	    
	    logger.debug("Found user with Id: {} and wallet balance: {}", userEntity.getUserId(), userEntity.getWallet());
	    
	    return userEntity;
	}

	@Override
	public boolean deleteUser(int userId) {
		
		logger.info("Deleting user with id: {}", userId);
		 
		Optional<User> user = userRepo.findById(userId);
		
		if (user.isPresent()) {
            userRepo.delete(user.get());
            logger.info("User profile with Id: {} deleted successfully", userId);
            return true; // Deleted the user successfully and return true
        } 
		else 
		{
			logger.error("User profile with Id: {} not found for deletion", userId);
            throw new UserNotFoundException(USER_NOT_FOUND_MSG + userId);
		}
		
	}

	@Override
//	@CircuitBreaker(name = "notificationServiceCircuitBreaker", fallbackMethod = "myFallBackMethod")
	public User updateUserWallet(int userId, BigDecimal amount) 
	{
		
		logger.info("Updating Wallet for user with id: {}", userId);
		
		User existingUser = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MSG + userId));
		
		logger.debug("Current wallet balance for userId {}: {}",userId, existingUser.getWallet());
		
		BigDecimal newWalletAmount = existingUser.getWallet().add(amount);
		
		if(newWalletAmount.compareTo(BigDecimal.ZERO) < 0)
		{
			logger.error("Attempted to set negative wallet balance for UserId: {}. \nCurrent Balance: {}, attempted amount: {}", userId, existingUser.getWallet(), amount);
			
			throw new IllegalArgumentException("Cannot set wallet to a negative amount");
		}
		
		existingUser.setWallet(newWalletAmount);
		existingUser.setUpdatedAt(LocalDate.now());
		
		// saving the updated wallet
		User updatedUser= userRepo.save(existingUser);
		logger.info("Wallet updated successfully for userId: {}", userId);
		return updatedUser;

	}

	@Override
	public String generateToken(String userName, String role) {
		// Validate inputs for null or empty values
	    if (userName == null || userName.trim().isEmpty()) {
	        throw new InvalidRequestException("Username cannot be null or empty");
	    }
	    if (role == null || role.trim().isEmpty()) {
	        throw new InvalidRequestException("Role cannot be null or empty");
	    }
	    return jwtService.generateToken(userName, role);
	}

	@Override
	public void validateToken(String token) {
		// Check if the token is null or empty
	    if (token == null || token.trim().isEmpty()) {
	        throw new InvalidRequestException("Token cannot be null or empty");
	    }
	    // Call the JWT service to validate the token
	    try {
	        jwtService.validateToken(token);
	    } catch (InvalidRequestException e) {
	        throw new InvalidRequestException("Invalid token provided: " + e.getMessage());
	    }
	}
	
	public User myFallBackMethod(UserRegistrationRequest addUsers, Throwable throwable) {
	    logger.error("Notification service is unavailable. Falling back. Error: {}", throwable.getMessage());
	    // You can still save the user without sending the email, for example
	    User user = new User();
	    user.setUserName(addUsers.getUserName());
	    user.setPassword(passwordEncoder.encode(addUsers.getPassword()));
	    user.setEmail(addUsers.getEmail());
	    user.setRole(addUsers.getRole());
	    
	    return userRepo.save(user);
	}

}
