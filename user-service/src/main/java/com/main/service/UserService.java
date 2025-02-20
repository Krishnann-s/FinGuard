package com.main.service;

import java.math.BigDecimal;
import java.util.List;


import com.main.dto.UserProfileUpdateRequest;
import com.main.dto.UserRegistrationRequest;
import com.main.entity.User;


public interface UserService 
{
	User addNewUsers(UserRegistrationRequest addUsers);
	List<User> getAllUsers();
	User updateUserProfile(int userId, UserProfileUpdateRequest user);
	User getUserById(int userId);
	boolean deleteUser(int userId);
	User updateUserWallet(int userId, BigDecimal amount);
	String generateToken(String userName,String role);
	void validateToken(String token);
}
