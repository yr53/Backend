package com.coreon.faq.controller;

import com.coreon.faq.dto.request.FaqChatAskReq;
import com.coreon.faq.dto.response.FaqAskRes;
import com.coreon.faq.dto.response.FaqChatMessageRes;
import com.coreon.faq.service.FaqChatService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faq/chat")
public class FaqChatController {

    private final FaqChatService chatService;

    public FaqChatController(FaqChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/history")
    public ResponseEntity<List<FaqChatMessageRes>> history(HttpServletRequest req,
                                                          @RequestParam(defaultValue = "50") int limit) {
        return ResponseEntity.ok(chatService.history(req, limit));
    }

    // 질문 전송 + 저장 + 답변 저장 
    @PostMapping("/ask")
    public ResponseEntity<FaqAskRes> ask(HttpServletRequest req,
                                        @RequestBody FaqChatAskReq body) {
        return ResponseEntity.ok(chatService.askAndSave(req, body.getMessage()));
    }
}
