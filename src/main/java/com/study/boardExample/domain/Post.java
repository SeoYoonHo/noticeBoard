package com.study.boardExample.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Post {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String contents;

    @ManyToOne
    @JoinColumn(name = "post_type_id")
    private PostType postType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
