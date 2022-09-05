package com.medicow.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class NoticeDto {
    private Long id ;

    private String subject ;

    private String content ;

    private LocalDateTime regTime ;
}
