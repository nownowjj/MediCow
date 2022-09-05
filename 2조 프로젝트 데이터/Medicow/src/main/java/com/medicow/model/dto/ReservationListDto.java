package com.medicow.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReservationListDto {
    private String searchSubject; // 병원 이름
    private String searchDate; // 예약 일자
    private String searchEmail; // 환자 email
}
