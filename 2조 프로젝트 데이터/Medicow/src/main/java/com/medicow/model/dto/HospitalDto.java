package com.medicow.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HospitalDto {

    private Long hosNo;       // 병원 No pk임
    private String hosAddress;  // 병원 주소
    private String hosName;     // 병원 이름
    private Integer hosPostNum; // 병원 우편번호
    private Double hosPosX;     // 병원 X좌표
    private Double hosPosY;     // 병원 Y좌표

    public HospitalDto(){}

    public HospitalDto(Long hosNo, String hosAddress, String hosName, Integer hosPostNum ,Double hosPosX ,Double hosPosY) {
        this.hosNo = hosNo;
        this.hosAddress = hosAddress;
        this.hosName = hosName;
        this.hosPostNum = hosPostNum;
        this.hosPosX = hosPosX;
        this.hosPosY = hosPosY;
    }
}
