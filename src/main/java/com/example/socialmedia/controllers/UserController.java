package com.example.socialmedia.controllers;

import com.example.socialmedia.Services.UserService;
import com.example.socialmedia.models.User;
import com.example.socialmedia.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "https://socialmedia-frontend.herokuapp.com", allowCredentials = "true")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public User createNewUser(@RequestBody User newUser) {
        return userService.createUser(newUser);
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable long id) throws Exception {
       return userService.getUser(id);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable long id) {
        try {
            Optional<User> userToDelete = userService.deleteUser(id);
            return ResponseEntity.status(200).body(userToDelete);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(id);
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUserById(@PathVariable long id, @RequestBody User newUserParams,
                                            @RequestHeader(name = "Authorization") String jwt) {
        try {
            String userDoingUpdating = jwtUtil.extractUsername(jwt);
            User updatedUser =  userService.updateUser(id, newUserParams, userDoingUpdating);
            return ResponseEntity.status(200).body(updatedUser);
        } catch(Exception e) {
           return ResponseEntity.status(404).body(id);
        }
    }
}
