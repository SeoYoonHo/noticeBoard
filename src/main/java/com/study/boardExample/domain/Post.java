package com.study.boardExample.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class Post extends BaseTimeEntity{
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String contents;

    @ManyToOne
    @JoinColumn(name = "post_type_id")
    private PostType postType;

    @ManyToOne
    @JoinColumn(name = "members_id")
    private Member member;
}
