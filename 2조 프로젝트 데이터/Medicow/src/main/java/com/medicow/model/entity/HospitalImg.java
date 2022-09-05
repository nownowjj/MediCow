package com.medicow.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="hospital_img")
@Getter
@Setter
public class HospitalImg extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String imgName; // UUID 형식으로 업로드된 이미지 파일 이름
    private String oriImgName; // 이미지 원본 이름
    private String imgUrl;
    private String repImgYn; // 대표 이미지는 값이 Y

    @ManyToOne//(fetch = FetchType.LAZY) // 데이터를 보여주는 방식(지연로딩으로)
    @JoinColumn(name="hos_no") // Hospital의 id와 join!
    private Hospital hospital;

    // 이미지 정보를 업데이트 해주는 메소드
    public void updateHospitalImg(String oriImgName, String imgName, String imgUrl){
        this.oriImgName=oriImgName;
        this.imgName=imgName;
        this.imgUrl = imgUrl;
    }
}
