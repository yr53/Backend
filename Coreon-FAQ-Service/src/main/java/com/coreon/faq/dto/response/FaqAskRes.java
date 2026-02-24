package com.coreon.faq.dto.response;

public class FaqAskRes {
    private Long faqId;
    private String answer;
    private String ownerTeam; 

    public FaqAskRes() {}
    public FaqAskRes(Long faqId, String answer, String ownerTeam) {
        this.faqId = faqId;
        this.answer = answer;
        this.ownerTeam = ownerTeam;
    }

    public Long getFaqId() { return faqId; }
    public void setFaqId(Long faqId) { this.faqId = faqId; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public String getOwnerTeam() { return ownerTeam; }
    public void setOwnerTeam(String ownerTeam) { this.ownerTeam = ownerTeam; }
}
