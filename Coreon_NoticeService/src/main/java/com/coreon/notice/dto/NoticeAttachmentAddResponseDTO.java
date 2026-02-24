package com.coreon.notice.dto;

import java.util.List;

public class NoticeAttachmentAddResponseDTO {

    private String message;
    private List<AttachResponseDTO> attachments;

    public NoticeAttachmentAddResponseDTO() {}

    public NoticeAttachmentAddResponseDTO(String message, List<AttachResponseDTO> attachments) {
        this.message = message;
        this.attachments = attachments;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<AttachResponseDTO> getAttachments() { return attachments; }
    public void setAttachments(List<AttachResponseDTO> attachments) { this.attachments = attachments; }
}
