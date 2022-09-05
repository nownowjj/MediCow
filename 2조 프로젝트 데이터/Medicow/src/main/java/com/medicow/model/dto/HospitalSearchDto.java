package com.medicow.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HospitalSearchDto {
    // 병원 조회 유형은 병원명과 주소
    private String searchBy;
    // 병원 검색 키워드
    private String searchQuery="";
}
