package com.coreon.faq.controller;

import com.coreon.faq.mapper.FaqMapper;
import com.coreon.faq.service.FaqEmbeddingBatchService;
import com.coreon.faq.service.FaqSemanticSearchService;
import com.coreon.faq.service.OpenAiEmbeddingClient;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Profile("dev")
@RestController
@RequestMapping("/api/faq/dev")
public class FaqDevController {

    private final FaqEmbeddingBatchService batchService;
    private final FaqMapper faqMapper;
    private final FaqSemanticSearchService searchService;
    

    public FaqDevController(FaqEmbeddingBatchService batchService, FaqMapper faqMapper,
            FaqSemanticSearchService searchService) {
    	this.batchService = batchService;
    	this.faqMapper = faqMapper;
    	this.searchService = searchService;
    }

    @PostMapping("/embed-batch")
    public Object embedBatch(@RequestParam(defaultValue = "100") int limit) {
        return batchService.embedMissing(limit);
    }

    @GetMapping("/debug/embedded-count")
    public int embeddedCount() {
        return faqMapper.selectActiveWithEmbedding().size();
    }
    
    @GetMapping("/search")
    public Object search(@RequestParam("q") String q,
                         @RequestParam(defaultValue = "3") int k) {
        return searchService.searchTopK(q, k);
    }
    
    @PostMapping("/search-mini")
    public Object searchMini(@RequestBody SearchReq req) {
        int k = (req.k() == null ? 3 : req.k());
        return searchService.searchTopK(req.q(), k).stream()
                .map(x -> new SearchMiniRes(
                        x.faq().getId(),
                        x.faq().getQuestionTitle(),
                        x.faq().getOwnerTeam(),
                        x.score()
                ))
                .toList();
    }

    public record SearchReq(String q, Integer k) {}
    public record SearchMiniRes(Long id, String questionTitle, String ownerTeam, double score) {}

}