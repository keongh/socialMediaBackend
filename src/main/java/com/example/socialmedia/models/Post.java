package com.example.socialmedia.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long postId;
    private String contents;
    private LocalDateTime createdAt;
    private String postedBy;

    @ManyToOne
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany(mappedBy = "likedPosts", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<User> likes = new ArrayList<>();

    public Post() {

    }

    public Post(String contents) {
        this.contents = contents;
    }
}
