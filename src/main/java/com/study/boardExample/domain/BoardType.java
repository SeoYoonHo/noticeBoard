package com.study.boardExample.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class BoardType extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_type_id")
    private Long id;

    @OneToMany(mappedBy = "boardType", cascade = CascadeType.ALL)
    private List<Post> postList;

    private String typeName;
}
