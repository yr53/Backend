package com.coreon.faq.service;

import com.coreon.faq.domain.Faq;
import com.coreon.faq.mapper.FaqMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FaqSemanticSearchService {

    private final FaqMapper faqMapper;
    private final OpenAiEmbeddingClient embeddingClient;
    private final ObjectMapper om = new ObjectMapper();

    public FaqSemanticSearchService(FaqMapper faqMapper, OpenAiEmbeddingClient embeddingClient) {
        this.faqMapper = faqMapper;
        this.embeddingClient = embeddingClient;
    }

    public List<ScoredFaq> searchTopK(String query, int k) {
    	
        // 1) 질문 임베딩, OpenAI Embeddings API 호출
        double[] q = toArray(embeddingClient.embed(query));

        // 2) FAQ 임베딩 로드
        List<Faq> faqs = faqMapper.selectActiveWithEmbedding();

        // 3) 점수 계산(각 FAQ 임베딩 파싱 + cosine유사도 계산)
        List<ScoredFaq> scored = new ArrayList<>();
        for (Faq f : faqs) {
            if (f.getEmbeddingJson() == null || f.getEmbeddingJson().isBlank()) continue;

            double[] v = parseEmbeddingJson(f.getEmbeddingJson());
            if (v == null || v.length != q.length) continue;

            double score = VectorUtils.cosine(q, v);
            scored.add(new ScoredFaq(f, score));
        }

        // 4) 내림차순 정렬 후에 TopK 반환
        scored.sort(Comparator.comparingDouble(ScoredFaq::score).reversed());
        if (k <= 0) k = 1;
        return scored.subList(0, Math.min(k, scored.size()));
    }

    private double[] parseEmbeddingJson(String json) {
        try {
            List<Double> list = om.readValue(json, new TypeReference<List<Double>>() {});
            return toArray(list);
        } catch (Exception e) {
            return null;
        }
    }

    private double[] toArray(List<Double> list) {
        double[] arr = new double[list.size()];
        for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
        return arr;
    }

    public record ScoredFaq(Faq faq, double score) {}
}
