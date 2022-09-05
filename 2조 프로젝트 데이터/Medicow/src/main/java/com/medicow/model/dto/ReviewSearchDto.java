package com.medicow.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReviewSearchDto {
    private String searchDateType ; // 조회 기간 범위
    private String searchBy ; // 검색 필드(상품 이름 또는 등록자 아이디)
    private String searchQuery = ""; // 검색 키워드
}
