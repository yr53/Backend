package com.coreon.member.controller;

import jakarta.servlet.http.HttpSession;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.coreon.member.dto.*;
import com.coreon.member.service.MemberService;
import com.coreon.member.service.S3service;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    @Autowired private MemberService service;
    @Autowired private HttpSession session;
    @Autowired private S3service s3service;

   

    // ======================
    // 2. 내 기본 회원 정보 조회
    // ======================
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo() {
        String sessionId = (String) session.getAttribute("id");
        if (sessionId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인이 필요합니다."));
        }

        ResponseDTO profile = service.getMyProfileById(sessionId); // 응답 DTO로 조회
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "회원 정보를 찾을 수 없습니다."));
        }

        return ResponseEntity.ok(profile);
    }

////
//    // ======================
//    // 3. 특정 회원 조회
//    // ======================
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getUser(@PathVariable String id) {
//        MemberDTO member = service.getMemberById(id);
//        if (member == null)
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(Map.of("message", "회원 정보를 찾을 수 없습니다."));
//
//        return ResponseEntity.ok(member);
//    }






    // ======================
    // 6. 회원 정보 수정
    // ======================
    @PutMapping("/editMember")
    public ResponseEntity<?> editMember(@RequestBody MemberDTO req) {
        String sessionId = (String) session.getAttribute("id");
        if (sessionId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인이 필요합니다."));
        }

        ResponseDTO updated = service.updateMember(sessionId, req);

        return ResponseEntity.ok(Map.of(
                "message", "회원 수정이 완료되었습니다!",
                "data", updated
        ));
    }



  



}
