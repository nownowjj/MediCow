package com.medicow.model.entity;


import com.medicow.model.constant.RegisterStatus;
import com.medicow.model.constant.Role;
import com.medicow.model.dto.HospitalFormDto;
import com.medicow.model.dto.HospitalUpdateFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.Valid;

@Entity
@Table(name="hospital")
@Getter@Setter@ToString
public class Hospital extends BaseEntity{
    @Id
    @Column(name="hos_no")
    private Long hosNo;

    @Column(name="hos_id",unique = true)
    private String hosId;

    @Column(name="hos_password")
    private String hosPassword;

    @Column(name="hos_name", nullable = false)
    private String hosName;


    @Column(name="hos_post_num")
    private Integer hosPostNum;

    private String hosAddress;

    @Column(name="hos_daddress")
    private String hosDAddress;

    private String hosSubject;

    private String hosTel;

    private Double hosPosX;

    private Double hosPosY;

    private String hosCeo;

    private String hosCeoEmail;

    @Enumerated(EnumType.STRING)
    private RegisterStatus hosRegister; //Enum

    private String hosPath;

    private String hosRemark;

    @Enumerated(EnumType.STRING) // 새로추가
    private Role role ; // 일반인, 관리자 구분

    //화면에서 넘어 오는 dto 객체와 비번을 암호화 해주는 객체를 사용하여 Member Entity 객체 생성하기
    public static Hospital createHospital(HospitalFormDto hospitalFormDto, PasswordEncoder passwordEncoder){
        Hospital hospital = new Hospital();
        hospital .setHosAddress(hospitalFormDto.getHosAddress());
        hospital .setHosCeo(hospitalFormDto.getHosCeo());
        hospital .setHosCeoEmail(hospitalFormDto.getHosCeoEmail());
        hospital .setHosDAddress(hospitalFormDto.getHosDAddress());
        hospital .setHosName(hospitalFormDto.getHosName());
        hospital .setHosPath(hospitalFormDto.getHosPath());
        hospital .setHosPostNum(hospitalFormDto.getHosPostNum());
        hospital.setHosRegister(RegisterStatus.YES);
        hospital.setHosNo(hospitalFormDto.getHosNo());
        hospital.setHosPosY(hospitalFormDto.getHosPosY());
        hospital.setHosPosX(hospitalFormDto.getHosPosX());
        hospital.setRole(Role.USER); // 새로추가

        StringBuilder subject= new StringBuilder();
        for (int i = 0; i <hospitalFormDto.getHosSubject().length ; i++) {
            subject.append(hospitalFormDto.getHosSubject()[i]).append(" ");
        }
        hospital .setHosSubject(String.valueOf(subject));
        hospital .setHosId(hospitalFormDto.getHosId());

        String  password = passwordEncoder.encode(hospitalFormDto.getHosPassword());
        hospital .setHosPassword(password);

        return hospital;
    }
//    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    private List<DayTable> dayTables = new ArrayList<DayTable>();

    public void updateHospital(@Valid HospitalUpdateFormDto hospitalFormDto, PasswordEncoder passwordEncoder){
        // itemFormDto 는 화면에서 넘겨주는 수정될 dto 객체 정보로써,
        // 모든 변수들의 내용들을 Entity 변수에 저장시켜 주도록 합니다.
        this.hosCeo = hospitalFormDto.getHosCeo();
        this.hosAddress = hospitalFormDto.getHosAddress();
        this.hosCeoEmail = hospitalFormDto.getHosCeoEmail();
        this.hosDAddress = hospitalFormDto.getHosDAddress();
        this.hosName = hospitalFormDto.getHosName();
        this.hosPath = hospitalFormDto.getHosPath();
        this.hosPostNum = hospitalFormDto.getHosPostNum();
        this.hosId = hospitalFormDto.getHosId();

        StringBuilder subject= new StringBuilder();
        for (int i = 0; i <hospitalFormDto.getHosSubject().length ; i++) {
            subject.append(hospitalFormDto.getHosSubject()[i]).append(" ");
        }
        this.hosSubject = String.valueOf(subject);
        this.hosTel = hospitalFormDto.getHosTel();

        String password = passwordEncoder.encode(hospitalFormDto.getHosPassword());
        this.hosPassword = password;
    }
}
