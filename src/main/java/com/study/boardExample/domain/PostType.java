package com.study.boardExample.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class PostType extends BaseTimeEntity{
    @Id
    @GeneratedValue
    @Column(name = "post_type_id")
    private Long id;

    @OneToMany(mappedBy = "postType")
    private List<Post> postList;

    private String typeName;
}
