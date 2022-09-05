package com.medicow.model.dto;

import com.medicow.model.constant.RegisterStatus;
import com.medicow.model.entity.Hospital;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Getter@Setter
public class HospitalUpdateFormDto {

    private Long hosNo;

    @NotBlank(message="아이디를 입력하세요")
    private String hosId;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String hosPassword;

    @NotBlank(message="병원 이름을 입력하세요")
    private String hosName;

    @NotNull(message="우편 번호를 입력하세요")
    private Integer hosPostNum;

    @NotBlank(message="주소를 입력하세요")
    private String hosAddress;

    private String hosDAddress;

//  배열형태에서 오류나면 리스트 고려해보기
    @NotNull(message="진료과목을 선택해주세요")
    private String[] hosSubject;

    private String yImgUrl;
    private String hosTel;

//    @NotNull(message="")
    private Double hosPosX;

//    @NotNull(message="")
    private Double hosPosY;

    @NotBlank(message="대표자 이름을 입력해주세요")
    private String hosCeo;

    @NotBlank(message="대표자 이메일을 입력해주세요")
    private String hosCeoEmail;

    private RegisterStatus hosRegister; //Enum

    private String hosPath;

    private String hosRemark; //16  12개

    private List<HospitalImgDto> hospitalImgDtoList = new ArrayList<HospitalImgDto>();

    // 상품 수정시 해당 이미지들의 unique id 값을 저장할 리스트 컬렉션입니다.
    private List<Long> hospitalImgIds = new ArrayList<Long>();
    private static ModelMapper modelMapper = new ModelMapper();

    public  Hospital createHospital(){
        HospitalUpdateFormDto hospitalFormDto = new HospitalUpdateFormDto();
        Hospital hospital = new Hospital();
        hospital.setHosPassword(hospitalFormDto.getHosPassword());
        hospital.setHosAddress(hospitalFormDto.getHosAddress());
        hospital.setHosPostNum(hospitalFormDto.getHosPostNum());
        hospital.setHosName(hospitalFormDto.getHosName());
        hospital.setHosNo(hospitalFormDto.getHosNo());
        StringBuilder subject= new StringBuilder();
        for (int i = 0; i <hospitalFormDto.getHosSubject().length ; i++) {
            subject.append(hospitalFormDto.getHosSubject()[i]).append(" ");
        }
        hospital.setHosSubject(String.valueOf(subject));
        hospital.setHosTel(hospitalFormDto.getHosTel());
        hospital.setHosDAddress(hospitalFormDto.getHosDAddress());
        hospital.setHosCeo(hospitalFormDto.getHosCeo());
        hospital.setHosCeoEmail(hospitalFormDto.getHosCeoEmail());
        hospital.setHosId(hospitalFormDto.getHosId());
        hospital.setHosRegister(hospitalFormDto.getHosRegister());
        return hospital;
    }

    public static HospitalUpdateFormDto of(Hospital hospital){
        HospitalUpdateFormDto hospitalFormDto = new HospitalUpdateFormDto();
        hospitalFormDto.setHosPassword(hospital.getHosPassword());
        hospitalFormDto.setHosAddress(hospital.getHosAddress());
        hospitalFormDto.setHosPostNum(hospital.getHosPostNum());
        hospitalFormDto.setHosName(hospital.getHosName());
        hospitalFormDto.setHosNo(hospital.getHosNo());
        hospitalFormDto.setHosSubject(hospital.getHosSubject().split(" "));
        hospitalFormDto.setHosTel(hospital.getHosTel());
        hospitalFormDto.setHosDAddress(hospital.getHosDAddress());
        hospitalFormDto.setHosCeo(hospital.getHosCeo());
        hospitalFormDto.setHosCeoEmail(hospital.getHosCeoEmail());
        hospitalFormDto.setHosId(hospital.getHosId());
        hospitalFormDto.setHosRegister(hospital.getHosRegister());
        return hospitalFormDto;
    }

}
