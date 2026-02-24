package com.coreon.auth.service;

import java.util.ArrayList;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.coreon.auth.dto.RegistDTO;
import com.coreon.auth.mapper.AuthMemberMapper;

@Service
public class AuthService {
	 @Autowired
	    private AuthMemberMapper mapper;
	 
    @Autowired private HttpSession session;

    public String registProc(RegistDTO member) {
        // ---- 1) 기본 검증
        if(member.getId() == null || member.getId().trim().isEmpty()) {
            return "아이디를 입력하세요.";
        }
        if(member.getPw() == null || member.getPw().trim().isEmpty()) {
            return "비밀번호를 입력하세요.";
        }
        if(!member.getPw().equals(member.getConfirm())) {
            return "두 비밀번호를 일치하여 입력하세요.";
        }
        if(member.getUsername() == null || member.getUsername().trim().isEmpty()) {
            return "이름을 입력하세요.";
        }

        // ---- 2) 중복체크
        RegistDTO check = mapper.login(member.getId());
        if(check != null) {
            return "이미 사용중인 아이디 입니다.";
        }

        // ---- 3) 비밀번호 암호화
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String secretPass = encoder.encode(member.getPw());
        member.setPw(secretPass);

        // ---- 4) Member 저장
        int result1 = mapper.registProc(member);

    

        // ---- 7) 결과
        if (result1 == 1 ) {
            return "회원 등록 완료";
        } else {
            return "회원 등록을 다시 시도하세요.";
        }
    }


    public String loginProc(String id, String pw) {
        if(id == null || id.trim().isEmpty()) {
            return "아이디를 입력하세요.";
        }
        if(pw == null || pw.trim().isEmpty()) {
            return "비밀번호를 입력하세요.";
        }

        RegistDTO check = mapper.login(id);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(check != null && encoder.matches(pw, check.getPw())) {
            session.setAttribute("id", check.getId());
            session.setAttribute("username", check.getUsername());
            session.setAttribute("position", check.getPosition());
            session.setAttribute("role", check.getRole());
            session.setAttribute("dept", check.getDept());



            return "로그인 성공";
        }

        return "아이디 또는 비밀번호를 확인 후 다시 입력하세요.";
    }

  //auth
    public RegistDTO saveMember(RegistDTO member) {
        mapper.registProc(member);
        return member;
    }

    public RegistDTO getMemberById(String id) {
        return mapper.login(id);
    }

    

    public RegistDTO userInfoById(String id) {
        return mapper.login(id);
    }

   




  

}
