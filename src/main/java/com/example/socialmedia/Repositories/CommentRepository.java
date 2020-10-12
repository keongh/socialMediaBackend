package com.example.socialmedia.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.socialmedia.models.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findById(long id);
    Comment deleteById(long id);
}
