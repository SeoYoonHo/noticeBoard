package com.study.boardExample.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CountEvent extends ApplicationEvent {
    private final Long id;

    public CountEvent(Object source, Long id) {
        super(source);
        this.id = id;
    }
}
