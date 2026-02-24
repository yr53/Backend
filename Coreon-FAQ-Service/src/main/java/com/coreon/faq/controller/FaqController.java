package com.coreon.faq.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.coreon.faq.dto.request.FaqAskReq;
import com.coreon.faq.dto.response.FaqAskRes;
import com.coreon.faq.service.FaqService;

@RestController
@RequestMapping("/api/faq")
public class FaqController {

    private final FaqService faqService;

    public FaqController(FaqService faqService) {
        this.faqService = faqService;
    }

    @PostMapping("/ask")
    public ResponseEntity<FaqAskRes> ask(@RequestBody FaqAskReq req) {
        FaqAskRes res = faqService.ask(req.getMessage());
        return ResponseEntity.ok(res);
    }
}
