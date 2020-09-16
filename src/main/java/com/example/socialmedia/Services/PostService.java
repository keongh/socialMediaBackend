package com.example.socialmedia.Services;

import com.example.socialmedia.Repositories.PostRepository;
import com.example.socialmedia.Repositories.UserRepository;
import com.example.socialmedia.models.Comment;
import com.example.socialmedia.models.Post;
import com.example.socialmedia.models.User;
import com.example.socialmedia.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final JwtUtil jwtUtil;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public PostService(JwtUtil jwtUtil, PostRepository postRepository, UserRepository userRepository, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public String getUsernameFromToken(String jwt) {
       return jwtUtil.extractUsername(jwt);
    }

    public void saveComment(Comment comment, Post post) throws Exception {
        List<Comment> comments = post.getComments();
        comments.add(comment);
    }

    public Post createPost(Post newPost, String jwt) {
        newPost.setPostedBy(getUsernameFromToken(jwt.substring(7)));
        newPost.setCreatedAt(LocalDateTime.now());
        newPost.setAuthor(userRepository.findByUserName(newPost.getPostedBy()).get());
        return postRepository.save(newPost);
    }

    public Post findPost(long id) throws Exception {
        Optional<Post> foundPost = postRepository.findById(id);
        if (foundPost.isPresent()) return foundPost.get();
        else throw new Exception(String.format("Could not find post with id %d", id));
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    public Post deletePost(long id) throws Exception {
        Optional<Post> foundPost = postRepository.findById(id);
        if (foundPost.isPresent()) {
            postRepository.deleteById(id);
            return foundPost.get();
        }
        else {
            throw new Exception(String.format("Post with id %d not found", id));
        }
    }

    public Post getPost(long id) throws Exception {
        Optional<Post> foundPost = postRepository.findById(id);
        if (foundPost.isPresent()) {
            return foundPost.get();
        }
        else {
            throw new Exception("Could not find post");
        }
    }

    public Post likePost(long id, String userNameLikingPost) throws Exception {
       Post postToLike = getPost(id);
       User userLikingPost = userService.getUser(userNameLikingPost);
       if (postToLike.getLikes().contains(userLikingPost)) {
           throw new Exception("User already likes this post");
       }
       else {
           List<User> likedBy = postToLike.getLikes();
           likedBy.add(userLikingPost);
           postToLike.setLikes(likedBy);
           userLikingPost.getLikedPosts().add(postToLike);
           userRepository.save(userLikingPost);
           return postRepository.save(postToLike);
       }
    }

    public Post unlikePost(long id, String userNameUnliking) throws Exception {
        Optional<Post> foundPost = postRepository.findById(id);
        Optional<User> foundUser = userRepository.findByUserName(userNameUnliking);
        Post postToUnlike;
        User userUnliking;
        if (foundPost.isPresent()) {
            postToUnlike = foundPost.get();
        }
        else throw new Exception("Could not find post");
        if (foundUser.isPresent()) {
            userUnliking = foundUser.get();
        }
        else throw new Exception("Could not find user");
        if (!postToUnlike.getLikes().contains(userUnliking)) {
            throw new Exception("User does not like this post!");
        }
        else {
            List<User> likes = postToUnlike.getLikes();
            likes.remove(userUnliking);
            postToUnlike.setLikes(likes);
            userUnliking.getLikedPosts().remove(postToUnlike);
            userRepository.save(userUnliking);
            return postRepository.save(postToUnlike);
        }
    }
}
