package com.example.socialmedia.controllers;

import com.example.socialmedia.Services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.socialmedia.models.Comment;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{id}/comments")
    public Comment createComment(@RequestBody Comment comment, @PathVariable("id") long postId, @RequestHeader("Authorization") String jwt) throws Exception {
        return commentService.createComment(comment, postId, jwt);
    }

    @GetMapping("/posts/{id}/comments")
    public List<Comment> getAllComments(@PathVariable long postId) throws Exception {
        return commentService.getAllComments(postId);
    }
}
