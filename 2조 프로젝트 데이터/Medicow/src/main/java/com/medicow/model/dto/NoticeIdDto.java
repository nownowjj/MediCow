package com.medicow.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class NoticeIdDto {
    private String subject;
    private String contents;
    private String createdBy;
    private Long noticeId;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
    private Integer views;
}
