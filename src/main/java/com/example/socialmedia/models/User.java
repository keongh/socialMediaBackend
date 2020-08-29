package com.example.socialmedia.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private final LocalDateTime registrationDate = LocalDateTime.now();

    public User() {
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
