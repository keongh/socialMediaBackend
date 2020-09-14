package com.example.socialmedia.Repositories;

import com.example.socialmedia.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    Optional<User> findById(long id);
    Optional<User> deleteById(long id);
    List<User> findAll();
}
