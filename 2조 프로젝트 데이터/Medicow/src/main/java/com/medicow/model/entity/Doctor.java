package com.medicow.model.entity;

import com.medicow.model.constant.ActiveStatus;
import com.medicow.model.dto.DoctorFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter@Setter
@ToString
public class Doctor extends BaseEntity{
    @Id
    @Column(name = "doc_id")
    private Long docId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hos_id")
    private Hospital hospital;

    private String docSubject;
    private String docName;
    private String history;

    private String docImgName; // UUID 형식으로 업로드된 이미지 파일 이름
    private String docOriImgName;
    private String docImgUrl;

    public static Doctor createDoctor(DoctorFormDto doctorFormDto) {
        Doctor doctor = new Doctor();
        System.out.println(doctorFormDto.getDocName());
        System.out.println(doctorFormDto.getHistory());
        StringBuilder docSubject= new StringBuilder();
        for (int i = 0; i <doctorFormDto.getDocSubject().length ; i++) {
            docSubject.append(doctorFormDto.getDocSubject()[i]).append(" ");
        }
        doctor.docSubject = String.valueOf(docSubject);
        System.out.println(doctor.docSubject);
        doctor.docName = doctorFormDto.getDocName();
        doctor.history = doctorFormDto.getHistory();
        doctor.setActiveStatus(ActiveStatus.ABLE);

        return doctor;
    }
    public void updateDoctor(DoctorFormDto doctorFormDto) {
        StringBuilder subject= new StringBuilder();
        for (int i = 0; i <doctorFormDto.getDocSubject().length ; i++) {
            subject.append(doctorFormDto.getDocSubject()[i]).append(" ");
        }
        this.docSubject = String.valueOf(subject);
//        this.hospital = doctorFormDto.getHospital();
        this.history = doctorFormDto.getHistory();
        this.docName = doctorFormDto.getDocName();
        this.docId = doctorFormDto.getDocId();
    }
    public void updateDoctorImg (String oriImgName, String imgName, String imgUrl){
        this.docOriImgName=oriImgName;
        this.docImgName=imgName;
        this.docImgUrl = imgUrl;
    }
}
