package com.medicow.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ReviewDto {
    // 리뷰 고유번호
    private Long reviewId;
    // 예약 고유번호
    private Long reservationId;
    // 평점
    private Double point;
    // 리뷰내용
    private String contents;
    //삭제요청
    private String deleteStatus;
    // 병원 고유번호
    private Long hosNo;
    // 병원명
    private String hosName;
    // 환자 고유번호
    private Long memberNo;
    // 환자명
    private String memberName;
    // 활성화/비활성화(삭제시 사용)
    private String activeStatus;

    private LocalDateTime writeTime;
}
