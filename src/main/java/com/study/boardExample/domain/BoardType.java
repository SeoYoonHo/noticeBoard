package com.study.boardExample.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
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

    @Column(unique = true)
    private String typeName;
}
