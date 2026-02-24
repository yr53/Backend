package com.coreon.faq.service;

import com.coreon.faq.config.OpenAiProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class OpenAiEmbeddingClient {

    private final OpenAiProperties props;
    private final ObjectMapper om = new ObjectMapper();
    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public OpenAiEmbeddingClient(OpenAiProperties props) {
        this.props = props;
    }

    public List<Double> embed(String input) {
        try {
            String apiKey = props.getApiKey();
            if (apiKey == null || apiKey.isBlank()) {
                throw new IllegalStateException("OpenAI api-key가 비어있습니다. OPENAI_API_KEY 환경변수를 확인하세요.");
            }

            String model = props.getEmbeddingModel();
            if (model == null || model.isBlank()) model = "text-embedding-3-small";

            // request body
            String body = om.writeValueAsString(Map.of(
                    "model", model,
                    "input", input
            ));

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/embeddings"))
                    .timeout(Duration.ofSeconds(30))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());

            if (res.statusCode() / 100 != 2) {
                throw new RuntimeException("Embeddings API 실패: HTTP " + res.statusCode() + " / " + res.body());
            }

            JsonNode root = om.readTree(res.body());
            JsonNode embeddingArr = root.path("data").get(0).path("embedding");

            List<Double> vec = new ArrayList<>(embeddingArr.size());
            for (JsonNode n : embeddingArr) vec.add(n.asDouble());
            return vec;

        } catch (Exception e) {
            throw new RuntimeException("임베딩 생성 중 오류", e);
        }
    }
    
    
}


