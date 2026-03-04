package com.coreon.faq.bootstrap;

import com.coreon.faq.service.FaqEmbeddingBatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FaqEmbeddingStartupRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(FaqEmbeddingStartupRunner.class);

    private final FaqEmbeddingBatchService batchService;

    @Value("${app.embedding.batch-on-startup:false}")
    private boolean batchOnStartup;

    @Value("${app.embedding.startup-limit:200}")
    private int startupLimit;

    public FaqEmbeddingStartupRunner(FaqEmbeddingBatchService batchService) {
        this.batchService = batchService;
    }

    @Override
    public void run(String... args) {
        if (!batchOnStartup) {
            log.info("[EmbeddingStartup] batch-on-startup=false -> skip");
            return;
        }

        log.info("[EmbeddingStartup] START embedMissing(limit={})", startupLimit);
        try {
            Object result = batchService.embedMissing(startupLimit);
            log.info("[EmbeddingStartup] DONE result={}", result);
        } catch (Exception e) {
            // 중요: 여기서 앱이 죽지 않게 (ECS 재시작 루프 방지)
            log.error("[EmbeddingStartup] FAILED (continue running)", e);
        }
    }
}