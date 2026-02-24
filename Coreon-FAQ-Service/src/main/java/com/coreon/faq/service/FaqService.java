package com.coreon.faq.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coreon.faq.domain.Faq;
import com.coreon.faq.dto.response.FaqAskRes;
import com.coreon.faq.mapper.FaqMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional(readOnly = true)

public class FaqService {

	private static final Logger log = LoggerFactory.getLogger(FaqService.class);
    private final FaqSemanticSearchService searchService;
    private final FaqMapper faqMapper;

    private static final double THRESHOLD = 0.5;

    public FaqService(FaqMapper faqMapper,FaqSemanticSearchService searchService) {
        this.searchService = searchService;
        this.faqMapper = faqMapper;
    }

    public FaqAskRes ask(String message) {
        if (message == null || message.trim().isEmpty()) {
            return new FaqAskRes(null, "질문을 입력해주세요.", "IT운영팀");
        }
        String q = message.trim();

        // 1) exact match 먼저 실행 -> DB 질문 그대로 입력 시 무조건 답변 제공
        Faq exact = faqMapper.selectByExactQuestionTitle(q);
        if (exact != null) {
            return new FaqAskRes(exact.getId(), exact.getAnswer(), exact.getOwnerTeam());
        }
        
        //semantic search 실행 
        var top5 = searchService.searchTopK(q, 5);
        
        //로그 출력
        log.info("[ASK] q='{}'", q);
        for (var t : top5) {
            log.info("[ASK] cand id={} score={} title={}",
                t.faq().getId(), t.score(), t.faq().getQuestionTitle());
        }

        if (top5.isEmpty() || top5.get(0).score() < THRESHOLD) {
            return new FaqAskRes(null,
                "관련 FAQ를 찾지 못했습니다. 질문을 조금 더 구체적으로 입력해주시거나 IT운영팀에 문의해주세요.",
                "IT운영팀");
        }

        var faq = top5.get(0).faq();
        return new FaqAskRes(faq.getId(), faq.getAnswer(), faq.getOwnerTeam());

}}

