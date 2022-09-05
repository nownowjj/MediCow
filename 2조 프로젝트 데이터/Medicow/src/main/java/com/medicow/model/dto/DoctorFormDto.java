package com.medicow.model.dto;

import com.medicow.model.entity.Hospital;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class DoctorFormDto {
    private Long docId;

    private Hospital hospital;

//    @NotBlank
    private String[] docSubject;
    @NotBlank
    private String docName;
    @NotBlank
    private String history;

    private String docImgName; // UUID 형식으로 업로드된 이미지 파일 이름
    private String docOriImgName;
    private String docImgUrl;
}
