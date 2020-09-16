package com.example.socialmedia.controllers;

import com.example.socialmedia.Services.UserService;
import com.example.socialmedia.models.User;
import com.example.socialmedia.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", allowCredentials = "true", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
@RestController
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createNewUser(@RequestBody User newUser) {
        try {
            return ResponseEntity.status(201).body(userService.createUser(newUser));
        } catch (Exception e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable long id) throws Exception {
       return userService.getUser(id);
    }

    @GetMapping("/user")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
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

    @GetMapping("user/{id}/followers")
    public ResponseEntity<?> getAllFollowers(@PathVariable long id) {
        try {
            List<User> followers;
            followers = userService.getAllFollowers(id);
            return ResponseEntity.ok().body(followers);
        } catch(Exception e) {
            e.printStackTrace();
            return (ResponseEntity<?>) ResponseEntity.notFound();
        }
    }

    @PostMapping("/user/{id}/followers")
    public ResponseEntity<?> followUser(@PathVariable long id, @RequestHeader(name = "Authorization") String jwt) {
        String requestingUser = jwtUtil.extractUsername(jwt.substring(7));
        try {
            userService.followUser(id, requestingUser);
            return ResponseEntity.status(201).body(userService.getUser(id));
        } catch (Exception e) {
            if (e.getMessage().equals("Cannot follow yourself")) {
                return ResponseEntity.status(200).body("");
            }
            else {
                return ResponseEntity.status(404).body(id);
            }
        }
    }

    @DeleteMapping("/user/{id}/followers")
    public ResponseEntity<?> unfollowUser(@PathVariable long id, @RequestHeader(name = "Authorization") String jwt) {
        try {
            String requestingUser = jwtUtil.extractUsername(jwt.substring(7));
            userService.unfollowUser(id, requestingUser);
            return ResponseEntity.status(200).body(userService.getUser(id));
        }
        catch (Exception e) {
                return ResponseEntity.status(404).body(id);
        }
    }

    @GetMapping("/user/{id}/following")
    public ResponseEntity<?> getFollowing(@PathVariable long id, @RequestHeader(name = "Authorization") String jwt) {
        try {
            List<User> usersFollowing = userService.getUser(id).getFollowing();
            return ResponseEntity.ok().body(usersFollowing);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body(id);
        }
    }
}
