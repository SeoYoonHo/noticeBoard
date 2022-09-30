package com.study.boardExample.controller;

import com.study.boardExample.dto.MemberDTO;
import com.study.boardExample.common.CommonResponse;
import com.study.boardExample.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("api/v1/search/member/{id}")
    public ResponseEntity<CommonResponse.DataResponse<MemberDTO.MemberResponse>> getMemberById(
            @PathVariable(value = "id") Long id) {
        MemberDTO.MemberResponse memberResponses = memberService.findMemberById(id);
        return ResponseEntity.ok(CommonResponse.DataResponse.of("001", "Success", memberResponses));
    }

    @GetMapping("api/v1/search/member")
    public ResponseEntity<CommonResponse.DataResponse<List<MemberDTO.MemberResponse>>> getMemberById(
            @RequestParam String name) {
        List<MemberDTO.MemberResponse> memberListResponses = memberService.findMemberByName(name);
        return ResponseEntity.ok(CommonResponse.DataResponse.of("001", "Success", memberListResponses));
    }

    @PostMapping("api/v1/create/member")
    public ResponseEntity<CommonResponse.DataResponse<HashMap<String, Long>>> createMember(
            @RequestBody MemberDTO.CreateMemberRequest createMemberRequest) {
        Long memberId = memberService.createMember(createMemberRequest);
        HashMap<String, Long> resMap = new HashMap<>();
        resMap.put("memberId", memberId);
        return ResponseEntity.ok(CommonResponse.DataResponse.of("002", "Create member success", resMap));
    }

    @PostMapping("api/v1/update/member")
    public ResponseEntity<CommonResponse.DataResponse<HashMap<String, Long>>> updateMember(
            @RequestBody MemberDTO.UpdateMemberRequest updateMemberRequest) {
        Long memberId = memberService.updateMember(updateMemberRequest);
        HashMap<String, Long> resMap = new HashMap<>();
        resMap.put("memberId", memberId);
        return ResponseEntity.ok(CommonResponse.DataResponse.of("003", "Update member success", resMap));
    }
}
