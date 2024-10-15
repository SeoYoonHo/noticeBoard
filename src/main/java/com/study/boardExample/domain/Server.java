package com.study.boardExample.domain;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("SERVER")
public class Server extends Node {
    private String ipAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lb_id")
    private LoadBalancer loadBalancer;
    // 기타 서버 관련 속성
}