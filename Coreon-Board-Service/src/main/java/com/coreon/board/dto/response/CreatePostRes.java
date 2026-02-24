package com.coreon.board.dto.response;

public class CreatePostRes {
    private String message;
    private Long boardId;

    public CreatePostRes(String message, Long boardId) {
        this.message = message;
        this.boardId = boardId;
    }

    public String getMessage() { return message; }
    public Long getBoardId() { return boardId; }
}
