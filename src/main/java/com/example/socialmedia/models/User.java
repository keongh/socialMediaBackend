package com.example.socialmedia.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Valid
public class User {

    @NotBlank(message = "Cannot be blank")
    private String userName;

    @NotBlank(message = "Cannot be blank")
    @JsonIgnore
    private String password;

    private boolean active = true;
    private String role = "USER";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private final LocalDateTime registrationDate = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonIgnore
    List<User> followers = new ArrayList<>();

    @ManyToMany(mappedBy = "followers")
    @JsonIgnore
    List<User> following = new ArrayList<>();

    @ManyToMany
    @JsonIgnore
    List<Post> likedPosts = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    @JsonIgnore
    List<Post> createdPosts = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    @JsonIgnore
    List<Comment> comments = new ArrayList<>();

    public User() {
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    @JsonProperty
    public void setPassword(final String password) {
        this.password = password;
    }
}
