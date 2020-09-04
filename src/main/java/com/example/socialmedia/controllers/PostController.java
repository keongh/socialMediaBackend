package com.example.socialmedia.controllers;

import com.example.socialmedia.Services.PostService;
import com.example.socialmedia.models.Post;
import com.example.socialmedia.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowCredentials = "true", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    @Autowired
    public PostController(PostService postService, JwtUtil jwtUtil) {
        this.postService = postService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/posts")
    public ResponseEntity<?> createPost(@RequestBody Post newPost, @RequestHeader(name = "Authorization") String jwt) {
        try {
            Post createdPost = postService.createPost(newPost, jwt);
            return ResponseEntity.status(201).body(createdPost);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/posts/{id}")
    public Post getPost(@PathVariable long id) throws Exception {
        return postService.findPost(id);
    }

    @GetMapping("/posts")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @DeleteMapping("/posts/{id}")
    public Post deletePost(@PathVariable long id) throws Exception {
        return postService.deletePost(id);
    }

    @PostMapping("/posts/{id}/like")
    public ResponseEntity<?> likePost(@PathVariable long id, @RequestHeader(name = "Authorization") String jwt) {
        try {
            Post postLiked = postService.likePost(id, jwtUtil.extractUsername(jwt.substring(7)));
            return ResponseEntity.status(200).body(postLiked);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/posts/{id}/unlike")
    public ResponseEntity<?> unlikePost(@PathVariable long id, @RequestHeader(name = "Authorization") String jwt) {
        try {
           Post postUnliked = postService.unlikePost(id, jwtUtil.extractUsername(jwt.substring(7)));
           return ResponseEntity.status(200).body(postUnliked);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
