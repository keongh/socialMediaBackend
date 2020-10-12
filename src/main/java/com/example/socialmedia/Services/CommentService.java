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
    private final UserService userService;

    @Autowired
    public CommentService(PostRepository postRepository, PostService postService, CommentRepository commentRepository, UserService userService) {
        this.postRepository = postRepository;
        this.postService = postService;
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    public List<Comment> getAllComments(long id) throws Exception {
        Post post = postService.getPost(id);
        return post.getComments();
    }

    public Comment createComment(Comment comment, long postId, String jwt) throws Exception {
        Post post = postService.getPost(postId);
        comment.setAuthor(userService.getUser(userService.getUsernameFromToken(jwt.substring(7))));
        comment.setPost(post);
        postService.saveComment(comment, comment.getPost());
        return commentRepository.save(comment);
    }

    public Integer getCommentCount(long postId) throws Exception {
        List<Comment> comments = getAllComments(postId);
        return comments.size();
    }

    public Comment getComment(long commentId) throws Exception {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isPresent()) {
            return comment.get();
        }
        else throw new Exception("Could not find comment");
    }

    public Comment removeComment(long postId, long commentId) throws Exception {
        Post post = postService.getPost(postId);
        Comment comment = getComment(commentId);
        List<Comment> commentList = post.getComments();
        commentList.remove(comment);
        post.setComments(commentList);
        try {
            getComment(commentId);
        } catch (Exception e) {
            throw new Exception("Could not find comment to delete");
        }
        return commentRepository.deleteById(commentId);
    }
}
