package com.example.socialmedia.Services;

import com.example.socialmedia.Repositories.UserRepository;
import com.example.socialmedia.models.User;
import com.example.socialmedia.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void setUserPassword(User newUser) {
        String encodedPassword = bCryptPasswordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encodedPassword);
    }

    public User createUser(User newUser) throws Exception {
        setUserPassword(newUser);
        if (validateUsername(newUser.getUserName())) {
            return userRepository.save(newUser);
        } else {
            throw new Exception("Username already in use");
        }
    }

    public boolean validateUsername(String username) {
        if (userRepository.findByUserName(username).isPresent()) {
            return false;
        }
        else {
            return true;
        }
    }

    public User getUser(long id) throws Exception {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()) return foundUser.get();
        else throw new Exception(String.format("Could not find user with id %d", id));
    }

    public User getUser(String userName) throws Exception {
        Optional<User> foundUser = userRepository.findByUserName(userName);
        if (foundUser.isPresent()) return foundUser.get();
        else throw new Exception(String.format("Could not find user with username %s", userName));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> deleteUser(long id) throws Exception {
        Optional<User> toDelete = userRepository.deleteById(id);
        if (toDelete.isPresent()) return toDelete;
        else throw new Exception(String.format("Could not delete user with id %d", id));
    }

    public User updateUser(long id, User newParams, String userDoingUpdating) throws Exception {
        try {
            User toUpdate = getUser(id);
            if (!userDoingUpdating.equals(toUpdate.getUserName())) {
                throw new Exception("Cannot update another user's information");
            }
            if (!toUpdate.getUserName().equals(newParams.getUserName())) {
                if (validateUsername(newParams.getUserName())) {
                    toUpdate.setUserName(newParams.getUserName());
                }
                else {
                    throw new Exception("Username already in use");
                }
            }
            if (!toUpdate.getRole().equals(newParams.getRole())) {
                toUpdate.setRole(newParams.getRole());
            }
            if (!bCryptPasswordEncoder.encode(toUpdate.getPassword()).equals(bCryptPasswordEncoder.encode(
                    newParams.getPassword()))) {
                setUserPassword(toUpdate);
            }
            return userRepository.save(toUpdate);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(String.format("Could not update user %d",  id));
        }
    }

    public void followUser(long id, String requestingUserName) throws Exception {
        User userToFollow = getUser(id);
        List<User> followers = userToFollow.getFollowers();
        User requestingUser = getUser(requestingUserName);
        List<User> following = requestingUser.getFollowing();
        if (userToFollow.equals(requestingUser)) {
            throw new Exception("Cannot follow yourself");
        } else if (following.contains(userToFollow)) {
            throw new Exception("You are already following this user");
        } else {
            followers.add(requestingUser);
            userToFollow.setFollowers(followers);
            following.add(userToFollow);
            requestingUser.setFollowing(following);
            userRepository.save(userToFollow);
            userRepository.save(requestingUser);
        }
    }

    public void unfollowUser(long id, String requestingUser) throws Exception {
        try {
            User userToUnfollow = getUser(id);
            List<User> followers = userToUnfollow.getFollowers();
            User userUnfollowing = getUser(requestingUser);
            List<User> following = userUnfollowing.getFollowing();
            if (!followers.contains(userUnfollowing)) {
                throw new Exception("You are not following this user");
            }
            else {
                followers.remove(userUnfollowing);
                following.remove(userToUnfollow);
                userUnfollowing.setFollowing(following);
                userToUnfollow.setFollowers(followers);
                userRepository.save(userToUnfollow);
                userRepository.save(userUnfollowing);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllFollowers(long id) throws Exception {
        try {
            return userRepository.findById(id).get().getFollowers();
        }
        catch (Exception e) {
            throw new Exception("Could not find requested user");
        }
    }
    public String getUsernameFromToken(String jwt) {
        return jwtUtil.extractUsername(jwt);
    }

}
