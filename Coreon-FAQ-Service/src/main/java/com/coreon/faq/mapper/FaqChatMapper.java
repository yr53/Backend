package com.coreon.faq.mapper;

import com.coreon.faq.domain.FaqChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FaqChatMapper {

    int insertMessage(
            @Param("userId") String userId,
            @Param("role") String role,
            @Param("content") String content,
            @Param("faqId") Long faqId,
            @Param("score") Double score
    );

    List<FaqChatMessage> selectRecentByUser(
            @Param("userId") String userId,
            @Param("limit") int limit
    );
}
