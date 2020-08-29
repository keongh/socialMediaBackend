package com.example.socialmedia.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public Post() {

    }

    public Post(String contents) {
        this.contents = contents;
    }
}
