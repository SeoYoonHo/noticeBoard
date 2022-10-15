package com.study.boardExample.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class Post extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String contents;

    private int cnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_type_id")
    private PostType postType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id")
    private Member member;
}
