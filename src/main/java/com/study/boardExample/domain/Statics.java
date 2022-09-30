package com.study.boardExample.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class Statics extends BaseTimeEntity{
    @Id
    @GeneratedValue
    @Column(name = "statics_id")
    private Long id;
}
