package com.example.socialmedia.controllers;

import com.example.socialmedia.Services.PostService;
import com.example.socialmedia.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
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
}
