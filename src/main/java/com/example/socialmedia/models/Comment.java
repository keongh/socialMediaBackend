package com.example.socialmedia.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Comment {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private long id;

   private String contents;
   private String postedBy;
   private LocalDateTime createdAt;

   @ManyToOne
   private Post post;

   public Comment() {
      this.createdAt = LocalDateTime.now();
   }

   public Comment(String contents) {
      this.contents = contents;
      this.createdAt = LocalDateTime.now();
   }
}
