package com.medicow.service;

import com.medicow.model.constant.DiagnosisStatus;
import com.medicow.model.constant.ReservationStatus;
import com.medicow.model.dto.ReservationDto;
import com.medicow.model.dto.ReservationSearchDto;
import com.medicow.model.entity.Hospital;
import com.medicow.model.entity.Reservation;
import com.medicow.repository.inter.HospitalReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service @Transactional @RequiredArgsConstructor
public class HospitalReservationService {
    // 예약 쿼리문 실행을 위한 HospitalReservationRepository 선언 및 초기화(@RequiredArgsConstructor)
    private final HospitalReservationRepository hospitalReservationRepository;
    // 전체 검색 및 병원의 각 검색 주제에 따라 검색하는 메소드
    public Page<ReservationDto> findReservationByHosNo(ReservationSearchDto reservationSearchDto, Long hosNo, Pageable pageable){
        Long totalCount =null;
        List<Reservation> reservations=null;
        Hospital hospital = hospitalReservationRepository.findHospitalByhosNo(hosNo);

        if(reservationSearchDto.getSearchBy()==null || reservationSearchDto.getSearchBy().equals("")){
            // 전체검색의 경우
            System.out.println("검색조건 X");
            // 검색 조건이 null인 경우
            // 현재 병원의 전체 예약을 조회하는 메소드
            System.out.println(hosNo+"------------------병원번호~~~~~~~~~~~~~");
            reservations = hospitalReservationRepository.findReservationByHosNo(hosNo, pageable);
            totalCount = hospitalReservationRepository.countByHosNo(hosNo);
        }else{
            // 조건 검색인 경우
            if(reservationSearchDto.getSearchBy().equals("name")) {
                System.out.println("이름");
                // 환자 이름으로 찾을 때
                reservations = hospitalReservationRepository.findReservationByName(reservationSearchDto.getSearchQuery(), hospital, pageable);
                totalCount = hospitalReservationRepository.countByName(reservationSearchDto.getSearchQuery(), hospital);

            } else if(reservationSearchDto.getSearchBy().equals("symptom")){
                System.out.println("증상");
                // 증상으로 찾을 때
                reservations = hospitalReservationRepository.findReservationBySymptom(hospital,reservationSearchDto.getSearchQuery(),pageable);
                totalCount = hospitalReservationRepository.countBySymptom(hospital,reservationSearchDto.getSearchQuery());

            }else if(reservationSearchDto.getSearchBy().equals("email")){
                // 이메일로 찾을 때
                System.out.println("이메일");
                Long id = hospitalReservationRepository.findMemberIdByEmail(reservationSearchDto.getSearchQuery());
                reservations = hospitalReservationRepository.findReservationByEmail(id,hospital,pageable);
                totalCount = hospitalReservationRepository.countByEmail(id);

            }else if(reservationSearchDto.getSearchBy().equals("reservation")){
                // 예약상태로 찾을 때
                System.out.println("예약상태");
                ReservationStatus reservationStatus = reservationSearchDto.getReservationStatus();
                if(reservationStatus == null){
                    // 검색어로 잘못된 값이 입력된 경우의 분기처리 null 리턴
                    reservations = null;
                    totalCount = 0L;
                }else{
                    // 검색어가 제대로 입력된 경우
                    reservations = hospitalReservationRepository.findReservationByReservationStatus(hosNo,reservationStatus,pageable);
                    totalCount = hospitalReservationRepository.countByReservationStatus(hosNo,reservationStatus);
                }

            }else if(reservationSearchDto.getSearchBy().equals("diagnosis")){
                // 진찰여부로 찾을 때
                System.out.println("진찰여부");
                DiagnosisStatus diagnosisStatus = reservationSearchDto.getDiagnosisStatus();
                System.out.println(" 진찰여부는 :"+diagnosisStatus);
                if(diagnosisStatus == null){
                    // 검색어로 잘못된 값이 입력된 경우의 분기처리 null 리턴
                    reservations = null;
                    totalCount = 0L;
                }else{
                    // 검색어가 제대로 입력된 경우
                    reservations = hospitalReservationRepository.findReservationByDiagnosisStatus(hosNo,diagnosisStatus,pageable);
                    totalCount = hospitalReservationRepository.countByDiagnosisStatus(hosNo,diagnosisStatus);
                }

            }else{
                // 혹시 모를 예외... 사실상 없음
                System.out.println("나머지");
            }
        }

        List<ReservationDto> reservationDtos = new ArrayList<ReservationDto>();
        if(reservations == null){
            reservations = hospitalReservationRepository.findReservationByHosNo(hosNo, pageable);
            totalCount = hospitalReservationRepository.countByHosNo(hosNo);
        }
        for(Reservation reservation: reservations){
            ReservationDto reservationDto = new ReservationDto();
            // 신규 Dto를 생성해서  reservationDto에 reservation을 담음
            reservationDto.create(reservation);
            // 해당 Dto를 List에 저장
            reservationDtos.add(reservationDto);
        }

        return new PageImpl<ReservationDto>(reservationDtos,pageable,totalCount);
    }
    // 예약승인여부를 바꾸는 메소드
    public void updateReservationStatus(Long id,ReservationStatus reservationStatus){
        hospitalReservationRepository.updateReservationStatus(reservationStatus,id);
    }

    // 진료완료 여부를 바꾸는 메소드
    public void updateDiagnosisStatus(Long id,DiagnosisStatus diagnosisStatus){
        hospitalReservationRepository.updateDiagnosisStatus(diagnosisStatus, id);
    }

    // 현재 예약 환자 수
    public Long curReservationNo(Hospital hospital, ReservationStatus reservationStatus) {
        Long curNo = hospitalReservationRepository.curReservationNo(hospital, reservationStatus,DiagnosisStatus.NOTYET);
        return curNo;
    }

    // 현재 예약 대기자 수
    public Long curWaitingNo(Hospital hospital) {
        Long waitNo = hospitalReservationRepository.curWaitingNo(hospital,ReservationStatus.WAIT,DiagnosisStatus.NOTYET);
        return waitNo;
    }
// 현재 예약 대기자 수
//    public Long curWaitingNo(Hospital hospital, ReservationStatus reservationStatus, DiagnosisStatus diagnosisStatus) {
//        Long waitNo = hospitalReservationRepository.curWaitingNo(hospital, reservationStatus, diagnosisStatus);
//        return waitNo;
//    }
}
