package com.medicow.model.dto;

import com.medicow.model.entity.Hospital;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class MyHospitalDto {
    private String hosName;
    private String hosTel;
    private Integer hosPostNum;
    private String hosAddress;
    private String hosDAddress;
    private String[] hosSubject;
    private Long hosNo;
    private String hosImgUrl;

    public static MyHospitalDto createHospitalFormDto(Hospital hospital) {
        MyHospitalDto myHospitalDto= new MyHospitalDto();
        myHospitalDto.setHosName(hospital.getHosName());
        if (hospital.getHosTel() != null ) {
            myHospitalDto.setHosTel(hospital.getHosTel());
        }else {
            myHospitalDto.setHosTel("입력 값이 없습니다.");
        }
        myHospitalDto.setHosPostNum(hospital.getHosPostNum());
        myHospitalDto.setHosAddress(hospital.getHosAddress());
        if (hospital.getHosDAddress() != null ) {
            myHospitalDto.setHosDAddress(hospital.getHosDAddress());
        } else {
            myHospitalDto.setHosDAddress("입력 값이 없습니다.");
        }
        myHospitalDto.setHosSubject(hospital.getHosSubject().split(" "));
        myHospitalDto.setHosNo(hospital.getHosNo());

        System.out.println("ho51");

        return myHospitalDto;
    }
}
