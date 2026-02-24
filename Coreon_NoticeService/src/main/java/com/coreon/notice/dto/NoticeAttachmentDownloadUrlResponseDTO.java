package com.coreon.notice.dto;

public class NoticeAttachmentDownloadUrlResponseDTO {

    private Long attachmentId;
    private String originalName;
    private String downloadUrl;

    public NoticeAttachmentDownloadUrlResponseDTO() {}

    public NoticeAttachmentDownloadUrlResponseDTO(Long attachmentId, String originalName, String downloadUrl) {
        this.attachmentId = attachmentId;
        this.originalName = originalName;
        this.downloadUrl = downloadUrl;
    }

    public Long getAttachmentId() { return attachmentId; }
    public void setAttachmentId(Long attachmentId) { this.attachmentId = attachmentId; }

    public String getOriginalName() { return originalName; }
    public void setOriginalName(String originalName) { this.originalName = originalName; }

    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
}
