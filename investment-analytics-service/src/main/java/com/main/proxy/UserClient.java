package com.main.proxy;
import com.main.dto.UserDto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
 
@FeignClient("user-service")
public interface UserClient {
    @GetMapping("/finguard/user/{userId}")
    UserDto getUserById(@PathVariable int userId);
    
    @PutMapping("/finguard/user/{userId}")
    UserDto updateUser(@PathVariable int userId, @RequestBody UserDto user);
}