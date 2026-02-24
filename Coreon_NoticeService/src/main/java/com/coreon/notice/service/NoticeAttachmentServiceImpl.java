package com.coreon.notice.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.coreon.notice.dto.AttachResponseDTO;
import com.coreon.notice.dto.NoticeAttachmentAddResponseDTO;
import com.coreon.notice.dto.NoticeAttachmentDownloadUrlResponseDTO;
import com.coreon.notice.mapper.NoticeAttachmentMapper;
import com.coreon.notice.mapper.NoticeMapper;

import jakarta.servlet.http.HttpSession;

@Service
public class NoticeAttachmentServiceImpl implements NoticeAttachmentService {

    private final NoticeAttachmentMapper attachmentMapper;
    private final NoticeMapper noticeMapper;
    private final S3service s3service;

    public NoticeAttachmentServiceImpl(
            NoticeAttachmentMapper attachmentMapper,
            NoticeMapper noticeMapper,
            S3service s3service
    ) {
        this.attachmentMapper = attachmentMapper;
        this.noticeMapper = noticeMapper;
        this.s3service = s3service;
    }

    @Override
    @Transactional
    public NoticeAttachmentAddResponseDTO addAttachments(Long noticeId, List<MultipartFile> files, HttpSession session) {
        String loginId = requireLoginId(session);

        if (files == null || files.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "파일 없음/검증 실패");
        }
        if (!existsNotice(noticeId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "공지 없음");
        }
        requireOwnerOrAdmin(noticeId, loginId, session);

        List<AttachResponseDTO> uploaded = new ArrayList<>();
        List<String> uploadedUrls = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                if (file == null || file.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "파일 없음/검증 실패");
                }

                String storedUrl = s3service.uploadFile(file);
                uploadedUrls.add(storedUrl);

                AttachResponseDTO dto = new AttachResponseDTO();
                dto.setNoticeId(noticeId);
                dto.setOriginalName(file.getOriginalFilename());
                dto.setStoredUrl(storedUrl);
                dto.setContentType(file.getContentType());
                dto.setSizeBytes(file.getSize());

                int rows = attachmentMapper.insertAttachment(dto); // useGeneratedKeys 전제
                if (rows != 1 || dto.getAttachmentId() == null) {
                    safeRollbackS3(uploadedUrls);
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "첨부 저장 실패");
                }

                uploaded.add(dto);
            }
        } catch (IOException e) {
            safeRollbackS3(uploadedUrls);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "S3 업로드 실패");
        } catch (RuntimeException e) {
            safeRollbackS3(uploadedUrls);
            throw e;
        }

        NoticeAttachmentAddResponseDTO res = new NoticeAttachmentAddResponseDTO();
        res.setMessage("첨부 추가 완료");
        res.setAttachments(uploaded);
        return res;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttachResponseDTO> listAttachments(Long noticeId, HttpSession session) {
        requireLoginId(session);

        if (!existsNotice(noticeId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "공지 없음");
        }
        return attachmentMapper.selectAttachmentsByNoticeId(noticeId);
    }

    // ★ 인터페이스 요구대로 delete 구현
    @Override
    @Transactional
    public void delete(Long attachmentId, HttpSession session) {
        String loginId = requireLoginId(session);

        AttachResponseDTO row = attachmentMapper.selectAttachmentById(attachmentId);
        if (row == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "첨부 없음");
        }

        requireOwnerOrAdmin(row.getNoticeId(), loginId, session);

        int rows = attachmentMapper.deleteAttachmentById(attachmentId);
        if (rows != 1) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "첨부 삭제 실패");
        }

        safeDeleteS3(row.getStoredUrl());
    }

    @Override
    @Transactional(readOnly = true)
    public NoticeAttachmentDownloadUrlResponseDTO issueDownloadUrl(Long attachmentId, HttpSession session) {
        requireLoginId(session);

        AttachResponseDTO row = attachmentMapper.selectAttachmentById(attachmentId);
        if (row == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "첨부 없음");
        }

        String url = s3service.generatePresignedDownloadUrl(
                row.getStoredUrl(),
                row.getOriginalName(),
                300
        );

        NoticeAttachmentDownloadUrlResponseDTO res = new NoticeAttachmentDownloadUrlResponseDTO();
        res.setAttachmentId(row.getAttachmentId());
        res.setOriginalName(row.getOriginalName());
        res.setDownloadUrl(url);
        return res;
    }

    private String requireLoginId(HttpSession session) {
        Object v = session.getAttribute("id");
        if (v == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "미로그인");
        String id = String.valueOf(v).trim();
        if (id.isEmpty()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "미로그인");
        return id;
    }

    private boolean isAdmin(HttpSession session) {
        Object v = session.getAttribute("role");
        return v != null && "ADMIN".equals(String.valueOf(v).trim());
    }

    private boolean existsNotice(Long noticeId) {
        Integer exists = noticeMapper.existsNotice(noticeId);
        return exists != null && exists == 1;
    }

    private void requireOwnerOrAdmin(Long noticeId, String loginId, HttpSession session) {
        if (isAdmin(session)) return;

        String writerId = noticeMapper.selectWriterIdByNoticeId(noticeId);
        if (writerId == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "공지 없음");
        if (!loginId.equals(writerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한 없음(작성자/ADMIN 아님)");
        }
    }

    private void safeRollbackS3(List<String> urls) {
        if (urls == null) return;
        for (String u : urls) safeDeleteS3(u);
    }

    private void safeDeleteS3(String url) {
        try {
            if (url != null && !url.isBlank()) {
                s3service.deleteFileByUrl(url);
            }
        } catch (Exception ignore) {}
    }
}
