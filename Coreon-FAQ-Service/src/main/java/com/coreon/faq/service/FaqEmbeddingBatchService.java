package com.coreon.faq.service;

import com.coreon.faq.domain.Faq;
import com.coreon.faq.mapper.FaqMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FaqEmbeddingBatchService {

    private final FaqMapper faqMapper;
    private final OpenAiEmbeddingClient embeddingClient;
    private final ObjectMapper om = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(FaqEmbeddingBatchService.class);

    public FaqEmbeddingBatchService(FaqMapper faqMapper, OpenAiEmbeddingClient embeddingClient) {
        this.faqMapper = faqMapper;
        this.embeddingClient = embeddingClient;
    }

   // faq 처리마다 실패하면 실패한 건만 제외하고 계속 진행됨 
    
    @Transactional 
    public BatchResult embedMissing(int limit) {
    	if (limit <= 0) limit = 100;
    	    limit = Math.min(limit, 300); 
        long start = System.currentTimeMillis();

        List<Faq> targets = faqMapper.selectForEmbedding(limit);
        int total = targets.size();
        int success = 0;
        int fail = 0;

        for (Faq faq : targets) {
            try {
                String input = buildEmbeddingInput(faq);

                List<Double> vec = embeddingClient.embed(input);

                String embeddingJson = om.writeValueAsString(vec);

                String model = "text-embedding-3-small";

                int updated = faqMapper.updateFaqEmbedding(
                        faq.getId(),
                        embeddingJson,
                        model,
                        LocalDateTime.now()
                );

                if (updated == 1) success++;
                else fail++;

            } catch (Exception e) {
                fail++;
                
                log.warn("[EMBED FAIL] id={} msg={}", faq.getId(), e.getMessage(), e);
            }
        }

        long ms = System.currentTimeMillis() - start;

        int embedded = faqMapper.countEmbedded();
        int unembedded = faqMapper.countUnembedded();

        return new BatchResult(total, success, fail, ms, embedded, unembedded);
    }
    
    

     //임베딩 품질을 위해 구조화된 텍스트로 합성
    private String buildEmbeddingInput(Faq faq) {
        StringBuilder sb = new StringBuilder();

        sb.append("Q: ").append(safe(faq.getQuestionTitle())).append("\n");
        sb.append("A: ").append(safe(faq.getAnswer())).append("\n");

        if (faq.getCategory() != null && !faq.getCategory().isBlank()) {
            sb.append("CATEGORY: ").append(faq.getCategory()).append("\n");
        }
        if (faq.getTagsJson() != null && !faq.getTagsJson().isBlank()) {
            sb.append("TAGS: ").append(faq.getTagsJson()).append("\n");
        }
        if (faq.getOwnerTeam() != null && !faq.getOwnerTeam().isBlank()) {
            sb.append("OWNER_TEAM: ").append(faq.getOwnerTeam()).append("\n");
        }

        String out = sb.toString().trim();
        if (out.isBlank()) out = "FAQ_ID=" + faq.getId(); 
        return out;
    }

    private String safe(String s) {
        return (s == null ? "" : s.trim());
    }

    public static class BatchResult {
        public final int total;
        public final int success;
        public final int fail;
        public final long tookMs;
        public final int embeddedCount;
        public final int unembeddedCount;

        public BatchResult(int total, int success, int fail, long tookMs, int embeddedCount, int unembeddedCount) {
            this.total = total;
            this.success = success;
            this.fail = fail;
            this.tookMs = tookMs;
            this.embeddedCount = embeddedCount;
            this.unembeddedCount = unembeddedCount;
        }

        public Map<String, Object> toMap() {
            Map<String, Object> m = new HashMap<>();
            m.put("total", total);
            m.put("success", success);
            m.put("fail", fail);
            m.put("tookMs", tookMs);
            m.put("embeddedCount", embeddedCount);
            m.put("unembeddedCount", unembeddedCount);
            return m;
        }
    }
}
