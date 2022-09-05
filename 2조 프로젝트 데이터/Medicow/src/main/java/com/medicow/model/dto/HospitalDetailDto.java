package com.medicow.model.dto;

import com.medicow.model.entity.Hospital;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class HospitalDetailDto {
    private Long hosNo;
    private String hosId;
    private String hosName;
    private Integer hosPostNum;
    private String hosAddress;
    private String hosDAddress;
    private String hosTel;
    private Double hosPosX;
    private Double hosPosY;
    private String hosPath;
    private String hosSubject;

    private static ModelMapper modelMapper = new ModelMapper() ;

    public static HospitalDetailDto of(Hospital hospital) {
        return modelMapper.map(hospital, HospitalDetailDto.class);
    }
}
