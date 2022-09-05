package com.medicow.service;

import com.medicow.model.constant.DiagnosisStatus;
import com.medicow.model.constant.ReservationStatus;
import com.medicow.model.dto.ReservationListDto;
import com.medicow.model.entity.Hospital;
import com.medicow.model.entity.Member;
import com.medicow.model.entity.Reservation;
import com.medicow.repository.inter.HospitalReservationRepository;
import com.medicow.repository.inter.MemberRepository;
import com.medicow.repository.inter.MemberReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberReservationService {
    // RequiredArgsConstructor 통해서 자동으로 주입
    private final MemberReservationRepository memberReservationRepository;
    private final MemberRepository memberRepository;
    private final HospitalReservationRepository hospitalReservationRepository;

    public Long saveReservation(String hosId, String email,String symptom){
        Hospital hospital = memberReservationRepository.findByHosId(hosId);
        System.out.println(hospital.getHosId()+"=======================@@@@@@@@@@@@@@@@@@@@@@@@@@@@===============");

        Reservation reservation = new Reservation() ;

        reservation.setHospital(hospital);
        reservation.setReservationStatus(ReservationStatus.WAIT);
        reservation.setDiagnosisStatus(DiagnosisStatus.NOTYET);
        Member member = memberRepository.findByEmail(email);
        reservation.setSymptom(symptom);
        reservation.setContent(symptom);
        reservation.setMember(member);
        memberReservationRepository.save(reservation);

        return reservation.getId();
    }

    public Page<Reservation> getReservation(ReservationListDto reservationListDto, Pageable pageable) {
        System.out.println("====================================MemberReservationService");

        return memberReservationRepository.getReservation(reservationListDto, pageable);
    }


}
