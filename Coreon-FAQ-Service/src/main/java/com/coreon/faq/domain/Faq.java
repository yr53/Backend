package com.coreon.faq.domain;

import java.time.LocalDateTime;

public class Faq {
    private Long id;
    private String category;
    private String questionTitle;
    private String answer;
    private String tagsJson;
    private String ownerTeam;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String embeddingJson;
    private String embeddingModel;
    private LocalDateTime embeddedAt;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getQuestionTitle() { return questionTitle; }
    public void setQuestionTitle(String questionTitle) { this.questionTitle = questionTitle; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public String getTagsJson() { return tagsJson; }
    public void setTagsJson(String tagsJson) { this.tagsJson = tagsJson; }

    public String getOwnerTeam() { return ownerTeam; }
    public void setOwnerTeam(String ownerTeam) { this.ownerTeam = ownerTeam; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
	public String getEmbeddingJson() {return embeddingJson;}
	public void setEmbeddingJson(String embeddingJson) {this.embeddingJson = embeddingJson;}
	
	public String getEmbeddingModel() {return embeddingModel;}
	public void setEmbeddingModel(String embeddingModel) {this.embeddingModel = embeddingModel;}
	
	public LocalDateTime getEmbeddedAt() {return embeddedAt;}
	public void setEmbeddedAt(LocalDateTime embeddedAt) {this.embeddedAt = embeddedAt;}
	
	
}
