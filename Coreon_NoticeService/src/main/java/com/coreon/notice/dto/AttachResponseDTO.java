package com.coreon.notice.dto;

import java.time.LocalDateTime;

public class AttachResponseDTO {
	   private Long attachmentId;
	   private Long noticeId;
	   private String originalName;   // file_name
	   private String storedUrl;      // file_url
	   private String contentType;    // content_type
	   private Long sizeBytes;        // file_size
	   private LocalDateTime createdAt; // created_at
	public Long getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
	}
	public String getOriginalName() {
		return originalName;
	}
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	public String getStoredUrl() {
		return storedUrl;
	}
	public void setStoredUrl(String storedUrl) {
		this.storedUrl = storedUrl;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public Long getSizeBytes() {
		return sizeBytes;
	}
	public void setSizeBytes(Long sizeBytes) {
		this.sizeBytes = sizeBytes;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public Long getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(Long noticeId) {
		this.noticeId = noticeId;
	}
	
	
	   
	   
}
