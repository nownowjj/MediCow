package com.medicow.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MyDoctorsDto {
    private Long docId;
    private String docName;
    private String imgUrl;
    private String docSubject;

    // 의사 명단 페이지에서 사용할 데이터 Dto 클래스
    public MyDoctorsDto(Long docId, String docName, String imgUrl, String docSubject) {
        this.docId = docId;
        this.docName = docName;
        this.docSubject = docSubject;
        this.imgUrl = imgUrl;
    }
}
