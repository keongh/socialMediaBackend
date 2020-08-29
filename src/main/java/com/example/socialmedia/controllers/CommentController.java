package com.example.socialmedia.controllers;

import com.example.socialmedia.Services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.socialmedia.models.Comment;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://socialmedia-frontend.herokuapp.com", allowCredentials = "true")
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
    public List<Comment> getAllComments(@PathVariable long id) throws Exception {
        return commentService.getAllComments(id);
    }
}
