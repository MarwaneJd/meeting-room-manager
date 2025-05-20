package com.mrm.meetingroommanager.dto;

import java.time.LocalDateTime;

public class ChatDTO {
    private String message;
    private String response;
    private Long suggestedRoomId;
    private LocalDateTime suggestedStartTime;
    private LocalDateTime suggestedEndTime;
    private String suggestedTitle;
    private boolean showBookButton;

    // Default constructor
    public ChatDTO() {
    }

    // All-args constructor
    public ChatDTO(String message, String response, Long suggestedRoomId,
                   LocalDateTime suggestedStartTime, LocalDateTime suggestedEndTime,
                   String suggestedTitle, boolean showBookButton) {
        this.message = message;
        this.response = response;
        this.suggestedRoomId = suggestedRoomId;
        this.suggestedStartTime = suggestedStartTime;
        this.suggestedEndTime = suggestedEndTime;
        this.suggestedTitle = suggestedTitle;
        this.showBookButton = showBookButton;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Long getSuggestedRoomId() {
        return suggestedRoomId;
    }

    public void setSuggestedRoomId(Long suggestedRoomId) {
        this.suggestedRoomId = suggestedRoomId;
    }

    public LocalDateTime getSuggestedStartTime() {
        return suggestedStartTime;
    }

    public void setSuggestedStartTime(LocalDateTime suggestedStartTime) {
        this.suggestedStartTime = suggestedStartTime;
    }

    public LocalDateTime getSuggestedEndTime() {
        return suggestedEndTime;
    }

    public void setSuggestedEndTime(LocalDateTime suggestedEndTime) {
        this.suggestedEndTime = suggestedEndTime;
    }

    public String getSuggestedTitle() {
        return suggestedTitle;
    }

    public void setSuggestedTitle(String suggestedTitle) {
        this.suggestedTitle = suggestedTitle;
    }

    public boolean isShowBookButton() {
        return showBookButton;
    }

    public void setShowBookButton(boolean showBookButton) {
        this.showBookButton = showBookButton;
    }
}
