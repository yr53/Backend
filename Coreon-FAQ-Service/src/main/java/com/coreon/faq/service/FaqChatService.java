package com.coreon.faq.service;

import com.coreon.faq.domain.FaqChatMessage;
import com.coreon.faq.dto.response.FaqAskRes;
import com.coreon.faq.dto.response.FaqChatMessageRes;
import com.coreon.faq.mapper.FaqChatMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FaqChatService {

    private final FaqChatMapper chatMapper;
    private final FaqService faqService;

    public FaqChatService(FaqChatMapper chatMapper, FaqService faqService) {
        this.chatMapper = chatMapper;
        this.faqService = faqService;
    }

    //히스토리 조회 
    @Transactional(readOnly = true)
    public List<FaqChatMessageRes> history(HttpServletRequest req, int limit) {
        String userId = requireUserId(req);
        if (limit <= 0) limit = 50;
        limit = Math.min(limit, 200);

        List<FaqChatMessage> rows = chatMapper.selectRecentByUser(userId, limit);

        Collections.reverse(rows);

        return rows.stream()
                .map(m -> new FaqChatMessageRes(m.getId(), m.getRole(), m.getContent(), m.getCreatedAt()))
                .collect(Collectors.toList());
    }

    //질문 + 저장 + 답변 저장 
    @Transactional
    public FaqAskRes askAndSave(HttpServletRequest req, String message) {
        String userId = requireUserId(req);

        // USER 메시지 저장
        chatMapper.insertMessage(userId, "USER", message, null, null);

        // 기존 FAQ 답변 생성
        FaqAskRes res = faqService.ask(message);

        // BOT 메시지 저장 
        chatMapper.insertMessage(userId, "BOT", res.getAnswer(), res.getFaqId(), null);

        return res;
    }

    //로그인 사용자 식별자 꺼내기
    private String requireUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) throw new IllegalStateException("세션이 없습니다. 로그인 필요");

        /**Object v =
                firstNonNull(
                        session.getAttribute("userId"),
                        session.getAttribute("loginId"),
                        session.getAttribute("employeeNo"),
                        session.getAttribute("id"),
                        session.getAttribute("username")**/
        Object v = session.getAttribute("id");
        
               

        if (v == null) { throw new IllegalStateException("세션에 사용자 식별자가 없습니다. 로그인 저장값 확인 필요");
        }
    
    //private Object firstNonNull(Object... xs) {
        //for (Object x : xs) if (x != null) return x;
        //return null;
    return String.valueOf(v);
    }
}
