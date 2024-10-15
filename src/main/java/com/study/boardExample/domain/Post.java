package com.study.boardExample.domain;

import com.study.boardExample.enums.BoardTypeEnums;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

    @Enumerated(EnumType.STRING)
    private BoardTypeEnums boardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id")
    private Member member;
}
