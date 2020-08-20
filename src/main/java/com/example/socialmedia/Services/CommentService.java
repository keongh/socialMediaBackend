package com.example.socialmedia.Services;

import com.example.socialmedia.Repositories.CommentRepository;
import com.example.socialmedia.Repositories.PostRepository;
import com.example.socialmedia.models.Comment;
import com.example.socialmedia.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final PostRepository postRepository;
    private final PostService postService;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(PostRepository postRepository, PostService postService, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.postService = postService;
        this.commentRepository = commentRepository;
    }

    public List<Comment> getAllComments(long id) throws Exception {
        Optional<Post> maybePost = postRepository.findById(id);
        if (maybePost.isPresent()) {
            Post post = maybePost.get();
            return post.getComments();
        }
        else {
            throw new Exception(String.format("Post with id %d not found", id));
        }
    }

    public Comment createComment(Comment comment, long postId, String jwt) throws Exception {
        Post post;
        Optional<Post> maybePost = postRepository.findById(postId);
        if (maybePost.isPresent()) {
            post = maybePost.get();
        }
        else throw new Exception("Could not find post to comment on!");
        comment.setPostedBy(postService.getUsernameFromToken(jwt.substring(7)));
        comment.setPost(post);
        postService.saveComment(comment, comment.getPost());
        return commentRepository.save(comment);
    }
}
