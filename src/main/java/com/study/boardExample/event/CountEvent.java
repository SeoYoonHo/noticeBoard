package com.study.boardExample.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CountEvent extends ApplicationEvent {
    private Long id;

    private int cnt;

    public CountEvent(Object source, Long id, int cnt) {
        super(source);
        this.id = id;
        this.cnt = cnt;
    }
}
