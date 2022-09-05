package com.medicow.model.dto;

import com.medicow.model.constant.DiagnosisStatus;
import com.medicow.model.constant.ReservationStatus;
import com.medicow.model.entity.Reservation;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ReservationDto {
    // 예약 넘버링
    private Long id;
    // 병원 아이디(외부키)
    private String hosId;
    // 병원 이름
    private String hosName;
    // 환자 이메일(외부키)
    private String email;
    // 환자 이름
    private String name;
    // 증상 적는 칸
    private String symptom;
    // 예약승인여부
    private String reservationStatus;
    //진료완료여부
    private String diagnosisStatus;
    // 예약신청일자
    private LocalDateTime reservationDate;

    // reservation Entity의 내용을 DTO로 저장하는 메소드
    public void create(Reservation reservation) {

        this.id = reservation.getId();
        if(reservation.getDiagnosisStatus() == null){
            this.diagnosisStatus = DiagnosisStatus.NOTYET.toString();
        }else{
            this.diagnosisStatus = reservation.getDiagnosisStatus().toString();
        }
        if(reservation.getReservationStatus() == null){
            this.reservationStatus = ReservationStatus.WAIT.toString();
        }else{
            this.reservationStatus = reservation.getReservationStatus().toString();
        }
        this.reservationDate = reservation.getRegTime();
        this.email = reservation.getMember().getEmail();
        this.name = reservation.getMember().getName();
        this.hosId = reservation.getHospital().getHosId();
        this.hosName = reservation.getHospital().getHosName();
        this.symptom = reservation.getSymptom();
    }
}
