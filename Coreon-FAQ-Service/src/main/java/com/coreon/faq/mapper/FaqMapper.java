package com.coreon.faq.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.coreon.faq.domain.Faq;

@Mapper
public interface FaqMapper {

	Faq selectByExactQuestionTitle(String questionTitle);

    
    List<Faq> selectForEmbedding(@Param("limit") int limit);
    
    List<Faq> selectActiveWithEmbedding();


    int updateFaqEmbedding(
        @Param("id") Long id,
        @Param("embeddingJson") String embeddingJson,
        @Param("embeddingModel") String embeddingModel,
        @Param("embeddedAt") LocalDateTime embeddedAt
    );
    
    //상태 확인용
    int countEmbedded();
    int countUnembedded();
}



