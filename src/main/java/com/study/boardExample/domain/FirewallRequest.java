package com.study.boardExample.domain;


import jakarta.persistence.*;

@Entity
public class FirewallRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fire_wall_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_node_id")
    private Node sourceNode;  // 소스 서버 혹은 로드밸런서

    @ManyToOne
    @JoinColumn(name = "target_node_id")
    private Node targetNode;  // 타겟 서버 혹은 로드밸런서

    private String rule;

    // 기타 방화벽 신청 관련 속성
}
