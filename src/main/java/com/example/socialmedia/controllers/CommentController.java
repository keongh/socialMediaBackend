package com.example.socialmedia.controllers;

import com.example.socialmedia.Services.CommentService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.socialmedia.models.Comment;

import java.nio.file.Path;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowCredentials = "true")
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

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> removeComment(@PathVariable long postId, @PathVariable long commentId) {
        try {
            Comment removedComment = commentService.removeComment(postId, commentId);
            return ResponseEntity.ok().body(removedComment);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Could not find comment");
        }
    }
}
