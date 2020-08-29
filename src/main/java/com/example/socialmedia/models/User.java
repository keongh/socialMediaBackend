package com.example.socialmedia.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Valid
public class User {

    @NotBlank(message = "Cannot be blank")
    private String userName;

    @NotBlank(message = "Cannot be blank")
    private String password;

    private boolean active = true;
    private String role = "USER";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private final LocalDateTime registrationDate = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    List<User> followers;

    @ManyToMany(mappedBy = "followers")
    @JsonIgnore
    List<User> following;

    public User() {
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
