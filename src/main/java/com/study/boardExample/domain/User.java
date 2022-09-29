package com.study.boardExample.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String name;
    private Integer age;
    private String gender;
    private String level;
    private String tel;
    private Date createDt;
    private Date lastLoginDt;

    @OneToMany(mappedBy = "user")
    private List<Post> postList;
}
