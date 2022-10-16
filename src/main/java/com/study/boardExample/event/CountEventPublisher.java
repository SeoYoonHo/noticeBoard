package com.study.boardExample.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CountEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(final Long id, final int cnt){
        CountEvent countEvent = new CountEvent(this, id, cnt);
        applicationEventPublisher.publishEvent(countEvent);
    }
}
