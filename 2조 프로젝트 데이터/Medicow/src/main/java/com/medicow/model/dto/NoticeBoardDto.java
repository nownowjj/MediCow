package com.medicow.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter @Setter
public class NoticeBoardDto {
    private Long id;
    private Long memberId;
    @NotBlank(message = "제목은 필수 입력 값 입니다.")
    private String subject;
    private String contents;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
    private int views;

}
