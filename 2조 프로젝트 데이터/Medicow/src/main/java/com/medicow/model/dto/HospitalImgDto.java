package com.medicow.model.dto;

import com.medicow.model.entity.HospitalImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class HospitalImgDto {
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;

    // mapper 객체 생성
    private static ModelMapper modelMapper = new ModelMapper();

    public static HospitalImgDto of(HospitalImg hospitalImg){
        // 상품의 이미지 정보를 이용하여 상품 dto 객체로 변환해줍니다.
        return modelMapper.map(hospitalImg,HospitalImgDto.class);
    }
}




