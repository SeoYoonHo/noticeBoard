package com.study.boardExample.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Statics {
    @Id
    @GeneratedValue
    @Column(name = "statics_id")
    private Long id;
}
