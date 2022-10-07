package com.study.boardExample.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class Member extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;
    private String authority;
    private String name;
    private Integer age;
    private String gender;
    private String level;
    private String tel;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Post> postList = new ArrayList<>();
}
