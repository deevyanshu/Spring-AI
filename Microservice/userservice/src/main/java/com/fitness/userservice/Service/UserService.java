package com.fitness.userservice.Service;

import com.fitness.userservice.Repository.UserRepository;
import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.models.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse register(RegisterRequest request)
    {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exist");
        }

        User user=new User();
        user.setEmail(request.getEmail());
        user.setFirstname(request.getFirstName());
        user.setPassword(request.getPassword());
        user.setLastname(request.getLastName());

        User savedUser=userRepository.save(user);
        UserResponse response=new UserResponse();
        response.setEmail(savedUser.getEmail());
        response.setFirstname(savedUser.getFirstname());
        response.setId(savedUser.getId());
        response.setLastname(savedUser.getLastname());
        response.setPassword(savedUser.getPassword());
        response.setCreatedAt(savedUser.getCreatedAt());
        response.setUpdatedAt(savedUser.getUpdatedAt());
        return response;
    }

    public UserResponse getUserProfile(String userId)
    {
        User user=userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        UserResponse response=new UserResponse();
        response.setEmail(user.getEmail());
        response.setFirstname(user.getFirstname());
        response.setId(user.getId());
        response.setLastname(user.getLastname());
        response.setPassword(user.getPassword());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

    public Boolean existByUserId(String userId) {
        return userRepository.existsById(userId);
    }
}
