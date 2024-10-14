package com.study.boardExample.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // 또는 JOINED 전략도 선택 가능
@DiscriminatorColumn(name = "node_type")
public abstract class Node extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "sourceNode")
    private List<FirewallRequest> sourceFirewallRequests;

    @OneToMany(mappedBy = "targetNode")
    private List<FirewallRequest> targetFirewallRequests;

    // 공통 속성들 추가
}