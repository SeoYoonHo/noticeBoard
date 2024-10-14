package com.study.boardExample.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("LOAD_BALANCER")
public class LoadBalancer extends Node {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lb_id")
    private Long id;

    @OneToMany(mappedBy = "loadBalancer")
    private List<Server> servers;  // 1대 다 관계

    // 기타 로드밸런서 관련 속성
}
