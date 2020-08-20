package com.example.socialmedia.Services;

import com.example.socialmedia.Repositories.PostRepository;
import com.example.socialmedia.models.Comment;
import com.example.socialmedia.models.Post;
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

    @Autowired
    public PostService(JwtUtil jwtUtil, PostRepository postRepository) {
        this.jwtUtil = jwtUtil;
        this.postRepository = postRepository;
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
}
