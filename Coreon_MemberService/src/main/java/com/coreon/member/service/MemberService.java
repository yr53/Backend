package com.coreon.member.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import com.coreon.member.dto.MemberDTO;
import com.coreon.member.dto.ResponseDTO;
import com.coreon.member.mapper.IMemberMapper;

@Service
public class MemberService {

    @Autowired private IMemberMapper mapper;
    @Autowired private BCryptPasswordEncoder passwordEncoder; 
    
    public ResponseDTO getMyProfileById(String id) {
        return mapper.getMyProfileById(id);
    }



  

    // ----------- UPDATE -----------
    public ResponseDTO updateMember(String sessionId, MemberDTO req) {
        // pw 변경이면 confirm 검증 + 암호화
        if (req.getPw() != null && !req.getPw().trim().isEmpty()) {
            if (req.getConfirm() == null || !req.getPw().equals(req.getConfirm())) {
                throw new IllegalArgumentException("비밀번호 확인이 일치하지 않습니다.");
            }
            
            req.setPw(passwordEncoder.encode(req.getPw())); // 권장
        } else {
            req.setPw(null);      // 비번 미변경
            req.setConfirm(null);
        }

        // 빈 문자열 → null 처리(원하면 dept/position/mobile도 동일)
        if (req.getDept() != null && req.getDept().trim().isEmpty()) req.setDept(null);
        if (req.getPosition() != null && req.getPosition().trim().isEmpty()) req.setPosition(null);
        if (req.getMobile() != null && req.getMobile().trim().isEmpty()) req.setMobile(null);

        mapper.updateProc(sessionId, req);

        // 수정 후 최신 값 조회해서 반환
        return mapper.getMyProfileById(sessionId);
    }




}
