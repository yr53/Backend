package com.coreon.notice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.coreon.notice.dto.AttachResponseDTO;

@Mapper
public interface NoticeAttachmentMapper {
    int insertAttachment(AttachResponseDTO dto);
    List<AttachResponseDTO> selectAttachmentsByNoticeId(@Param("noticeId") Long noticeId);
    AttachResponseDTO selectAttachmentById(@Param("attachmentId") Long attachmentId);
    int deleteAttachmentById(@Param("attachmentId") Long attachmentId);
}
