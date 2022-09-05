package com.medicow.model.dto;

import com.medicow.model.constant.DiagnosisStatus;
import com.medicow.model.constant.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReservationSearchDto {
    // 병원 사이드 검색 분류 -> 환자이름 , 승인여부, 진료여부
    // 환자 사이드 검색 분류 -> ???
    private String searchBy;
    // 예약 검색 키워드(사용자가 입력)
    private String searchQuery="";

    // 예약 승인 상태에 따라 Enum 타입으로 리턴
    public ReservationStatus getReservationStatus(){
        //RESERVATION,DENY,WAIT
        if(this.getSearchQuery().equals("예약승인")){
            return ReservationStatus.RESERVATION;
        }else if(this.getSearchQuery().equals("거절")){
            return ReservationStatus.DENY;
        }else if(this.getSearchQuery().equals("대기중")){
            return ReservationStatus.WAIT;
        }else{
            return null;
        }
    }

    public DiagnosisStatus getDiagnosisStatus() {
        if(this.getSearchQuery().equals("진료전")){
            return DiagnosisStatus.NOTYET;
        }else if(this.getSearchQuery().equals("노쇼")){
            return DiagnosisStatus.NOSHOW;
        }else if(this.getSearchQuery().equals("진료완료")){
            return DiagnosisStatus.SUCCESS;
        }else{
            return null;
        }
    }
}
