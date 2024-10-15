package com.study.boardExample.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class Statics extends BaseTimeEntity{
    @Id
    @GeneratedValue
    @Column(name = "statics_id")
    private Long id;
}
