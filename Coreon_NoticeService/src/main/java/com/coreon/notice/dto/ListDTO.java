package com.coreon.notice.dto;

import java.time.LocalDateTime;

public class ListDTO {
    private Long noticeId;
    private String category;
    private String title;
    private String publisherDept;
    private LocalDateTime createdAt;
	public Long getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(Long noticeId) {
		this.noticeId = noticeId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPublisherDept() {
		return publisherDept;
	}
	public void setPublisherDept(String publisherDept) {
		this.publisherDept = publisherDept;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

    // getter/setter
}
