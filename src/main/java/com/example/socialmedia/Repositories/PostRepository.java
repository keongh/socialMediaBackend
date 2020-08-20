package com.example.socialmedia.Repositories;

import com.example.socialmedia.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(long id);
    List<Post> findAllByOrderByCreatedAtDesc();
}
