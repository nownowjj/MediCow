package com.medicow.model.dto;

import com.medicow.model.constant.RegisterStatus;
import com.medicow.model.entity.Hospital;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter@Setter
public class HospitalInfoDto {

    private Long hosNo;

    @NotBlank(message="아이디를 입력하세요")
    private String hosId;

    @NotBlank(message="비밀번호를 입력하세요")
    private String hosPassword;

    @NotBlank(message="병원 이름을 입력하세요")
    private String hosName;

    @NotNull(message="우편 번호를 입력하세요")
    private Integer hosPostNum;

    @NotBlank(message="주소를 입력하세요")
    private String hosAddress;

    private String hosDAddress;

    private String hosImgUrl;

    //  배열형태에서 오류나면 리스트 고려해보기
//    @NotBlank(message="진료과목을 선택해주세요")
    private String[] hosSubject;

    private String hosTel;

//    private Double hosPosX;

//    private Double hosPosY;

    @NotBlank(message="대표자 이름을 입력해주세요")
    private String hosCeo;

    @NotBlank(message="대표자 이메일을 입력해주세요")
    private String hosCeoEmail;

    private RegisterStatus hosRegister; //Enum

//    private String hosPath;

    private String hosRemark; //16  12개

    private List<HospitalImgDto> hospitalImgDtoList = new ArrayList<HospitalImgDto>();

    // 상품 수정시 해당 이미지들의 unique id 값을 저장할 리스트 컬렉션입니다.
    private List<Long> hospitalImgIds = new ArrayList<Long>();
    private static ModelMapper modelMapper = new ModelMapper();

    public  Hospital createHospital(){
        HospitalInfoDto hospitalInfoDto = new HospitalInfoDto();
        Hospital hospital = new Hospital();
        hospital.setHosPassword(hospitalInfoDto.getHosPassword());
        hospital.setHosAddress(hospitalInfoDto.getHosAddress());
        hospital.setHosPostNum(hospitalInfoDto.getHosPostNum());
        hospital.setHosName(hospitalInfoDto.getHosName());
        hospital.setHosNo(hospitalInfoDto.getHosNo());
        StringBuilder subject= new StringBuilder();
        for (int i = 0; i <hospitalInfoDto.getHosSubject().length ; i++) {
            subject.append(hospitalInfoDto.getHosSubject()[i]).append(" ");
        }
        hospital.setHosSubject(String.valueOf(subject));
        hospital.setHosTel(hospitalInfoDto.getHosTel());
        hospital.setHosDAddress(hospitalInfoDto.getHosDAddress());
        hospital.setHosCeo(hospitalInfoDto.getHosCeo());
        hospital.setHosCeoEmail(hospitalInfoDto.getHosCeoEmail());
        hospital.setHosId(hospitalInfoDto.getHosId());
        hospital.setHosRegister(hospitalInfoDto.getHosRegister());
        return hospital;
    }

    public static HospitalInfoDto of(Hospital hospital){
        HospitalInfoDto hospitalInfoDto = new HospitalInfoDto();
        hospitalInfoDto.setHosPassword(hospital.getHosPassword());
        hospitalInfoDto.setHosAddress(hospital.getHosAddress());
        hospitalInfoDto.setHosPostNum(hospital.getHosPostNum());
        hospitalInfoDto.setHosName(hospital.getHosName());
        hospitalInfoDto.setHosNo(hospital.getHosNo());
        hospitalInfoDto.setHosSubject(hospital.getHosSubject().split(" "));
        hospitalInfoDto.setHosTel(hospital.getHosTel());
        hospitalInfoDto.setHosDAddress(hospital.getHosDAddress());
        hospitalInfoDto.setHosCeo(hospital.getHosCeo());
        hospitalInfoDto.setHosCeoEmail(hospital.getHosCeoEmail());
        hospitalInfoDto.setHosId(hospital.getHosId());
        hospitalInfoDto.setHosRegister(hospital.getHosRegister());
        return hospitalInfoDto;
    }

}
