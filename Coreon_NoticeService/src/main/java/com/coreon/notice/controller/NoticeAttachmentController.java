package com.coreon.notice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import com.coreon.notice.dto.AttachResponseDTO;
import com.coreon.notice.dto.NoticeAttachmentAddResponseDTO;
import com.coreon.notice.dto.NoticeAttachmentDownloadUrlResponseDTO;
import com.coreon.notice.service.NoticeAttachmentService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/notice")
public class NoticeAttachmentController {

    private final NoticeAttachmentService noticeAttachmentService;

    public NoticeAttachmentController(NoticeAttachmentService noticeAttachmentService) {
        this.noticeAttachmentService = noticeAttachmentService;
    }

    // =========================================================
    // 1) 첨부 추가 업로드
    // POST /api/notice/items/{noticeId}/attachments
    // =========================================================
    @PostMapping(
        value = "/items/{noticeId}/attachments",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<NoticeAttachmentAddResponseDTO> uploadAttachments(
            @PathVariable Long noticeId,
            @RequestPart("files") List<MultipartFile> files,
            HttpSession session
    ) {
        NoticeAttachmentAddResponseDTO res =
                noticeAttachmentService.addAttachments(noticeId, files, session);
        return ResponseEntity.status(201).body(res);
    }

    // =========================================================
    // 2) 첨부 목록 조회
    // GET /api/notice/items/{noticeId}/attachments
    // =========================================================
    @GetMapping("/items/{noticeId}/attachments")
    public ResponseEntity<List<AttachResponseDTO>> listAttachments(
            @PathVariable Long noticeId,
            HttpSession session
    ) {
        List<AttachResponseDTO> res =
                noticeAttachmentService.listAttachments(noticeId, session);
        return ResponseEntity.ok(res);
    }

    // =========================================================
    // 3) 첨부 삭제
    // DELETE /api/notice/attachments/{attachmentId}
    // =========================================================
    @DeleteMapping("/attachments/{attachmentId}")
    public ResponseEntity<Map<String, Object>> delete(
            @PathVariable Long attachmentId,
            HttpSession session
    ) {
        noticeAttachmentService.delete(attachmentId, session);
        return ResponseEntity.ok(Map.of("message", "첨부 삭제 완료"));
    }


    // =========================================================
    // 4) 첨부 다운로드 URL 발급
    // GET /api/notice/attachments/{attachmentId}/download
    // =========================================================
    @GetMapping("/attachments/{attachmentId}/download")
    public ResponseEntity<NoticeAttachmentDownloadUrlResponseDTO> issueDownloadUrl(
            @PathVariable Long attachmentId,
            HttpSession session
    ) {
        NoticeAttachmentDownloadUrlResponseDTO res =
                noticeAttachmentService.issueDownloadUrl(attachmentId, session);
        return ResponseEntity.ok(res);
    }
}
