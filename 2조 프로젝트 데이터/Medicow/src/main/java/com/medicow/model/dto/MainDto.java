package com.medicow.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class MainDto {
    private Long curReservationNo; // 현재 예약 환자수
    private Long curWaitingNo; // 현재 예약 대기 인원 수
    private String startDateTime;
    private String endDateTime;
    private Long reviewCount; // 총 병원 리뷰 수
    private String reviewTotal; // 병원 리뷰 총점
    List<ContactFormDto> contactFormDtos = new ArrayList<ContactFormDto>(); // 문의사항 게시판
    List<NoticeFormDto> noticeFormDtos = new ArrayList<NoticeFormDto>(); // 공지사항 게시판
}
