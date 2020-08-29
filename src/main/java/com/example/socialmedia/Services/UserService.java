package com.example.socialmedia.Services;

import com.example.socialmedia.Repositories.UserRepository;
import com.example.socialmedia.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void setUserPassword(User newUser) {
        String encodedPassword = bCryptPasswordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encodedPassword);
    }

    public User createUser(User newUser) {
        setUserPassword(newUser);
        return userRepository.save(newUser);
    }

    public User getUser(long id) throws Exception {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()) return foundUser.get();
        else throw new Exception(String.format("Could not find user with id %d", id));
    }

    public Optional<User> deleteUser(long id) throws Exception {
        //User toDelete = getUser(id);
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
                toUpdate.setUserName(newParams.getUserName());
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

    public void followUser(long id, String requestingUserName) {
        try {
            User userToFollow = getUser(id);
            List<User> followers = userToFollow.getFollowers();
            User requestingUser = userRepository.findByUserName(requestingUserName).get();
            followers.add(requestingUser);
            userToFollow.setFollowers(followers);
            userRepository.save(userToFollow);
            userRepository.save(requestingUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unfollowUser(long id, String requestingUser) throws Exception {
        try {
            User userToUnfollow = getUser(id);
            List<User> followers = userToUnfollow.getFollowers();
            User userUnfollowing = userRepository.findByUserName(requestingUser).get();
            boolean following = false;
            for (User u : followers) {
                if (userUnfollowing.getUserName().equals(requestingUser)) {
                    following = true;
                }
            }
            if (!following) {
                throw new Exception("You are not following this user");
            }
            else {
                followers.remove(userUnfollowing);
                userRepository.save(userToUnfollow);
                userRepository.save(userUnfollowing);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
