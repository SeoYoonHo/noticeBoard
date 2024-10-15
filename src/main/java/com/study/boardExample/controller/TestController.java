package com.study.boardExample.controller;

import com.study.boardExample.domain.Node;
import com.study.boardExample.repository.NodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final NodeRepository nodeRepository;

    @GetMapping("/tt")
    public void test(){
        List<Node> nodes = nodeRepository.findAll();
    }
}
