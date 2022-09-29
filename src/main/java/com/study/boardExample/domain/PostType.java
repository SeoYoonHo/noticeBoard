package com.study.boardExample.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class PostType {
    @Id
    @GeneratedValue
    @Column(name = "post_type_id")
    private Long id;

    @OneToMany(mappedBy = "postType")
    private List<Post> postList;

    private String typeName;
}
