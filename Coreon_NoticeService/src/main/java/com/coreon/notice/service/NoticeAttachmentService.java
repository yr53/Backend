package com.coreon.notice.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.coreon.notice.dto.AttachResponseDTO;
import com.coreon.notice.dto.NoticeAttachmentAddResponseDTO;
import com.coreon.notice.dto.NoticeAttachmentDownloadUrlResponseDTO;

import jakarta.servlet.http.HttpSession;

public interface NoticeAttachmentService {

    NoticeAttachmentAddResponseDTO addAttachments(Long noticeId, List<MultipartFile> files, HttpSession session);

    List<AttachResponseDTO> listAttachments(Long noticeId, HttpSession session);

    // ★ 이름을 delete로 통일 (에러 메시지가 요구하는 시그니처)
    void delete(Long attachmentId, HttpSession session);

    NoticeAttachmentDownloadUrlResponseDTO issueDownloadUrl(Long attachmentId, HttpSession session);
}
