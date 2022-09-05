package com.medicow.controller;

import com.medicow.model.constant.DiagnosisStatus;
import com.medicow.model.constant.ReservationStatus;
import com.medicow.model.dto.ReservationDto;
import com.medicow.model.dto.ReservationSearchDto;
import com.medicow.service.HospitalReservationService;
import com.medicow.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HospitalReservationController {
    // 예약관련 처리를 위한 서비스 생성 및 주입
    private final HospitalReservationService hospitalReservationService;
    private final HospitalService hospitalService;

    @GetMapping(value = {"/hospital/reservation","/hospital/reservation/{page}"})
    public String showMyReservation(ReservationSearchDto reservationSearchDto, @PathVariable("page") Optional<Integer> page, Principal principal, Model model){
        // 페이징 객체 생성 한번에 10개씩 보여줌
        Pageable pageable = PageRequest.of(page.isPresent()?page.get():0,5);
        System.out.println("검색 주제 "+reservationSearchDto.getSearchBy());
        System.out.println("검색 내용 "+reservationSearchDto.getSearchQuery());
        // principal를 이용해서 아이디를 가져오고, 아이디를 바탕으로 hosNo를 가져옴
        String hosId = principal.getName();
        Long hosNo = hospitalService.findHosNoByHosId(hosId);

        // 페이징 처리를 받은 페이지를 가져옴
        Page<ReservationDto> reservationList = hospitalReservationService.findReservationByHosNo(reservationSearchDto,hosNo,pageable);

        // 모델에 바인딩하여 넘겨줌
        model.addAttribute("reservations", reservationList);
        model.addAttribute("searchDto",reservationSearchDto);
        model.addAttribute("maxPage",5);

        return "hospital/reservation/show";
    }
    // Ajax를 이용한 예약승인여부 변경(예약승인 / 거절 / 대기중)
    @PatchMapping("/reservation/{reservationId}")
    public ResponseEntity updateReservation(@PathVariable("reservationId") Long id, String reservationStatus,Model model){
        // Enum 형태로 담기위한 객체 선언 및 초기화
        ReservationStatus reservationStatus1 = null;
//        System.out.println(reservationStatus);
        if(reservationStatus.equals("예약승인")){
            // 예약을 승인하는 경우
            reservationStatus1 = ReservationStatus.RESERVATION;
        }else if(reservationStatus.equals("거절")){
            // 예약을 거절하는 경우
            reservationStatus1 = ReservationStatus.DENY;
        }else if(reservationStatus.equals("대기중")){
            // 예약 대기중인 겨우
            reservationStatus1 = ReservationStatus.WAIT;
        }else{
            model.addAttribute("errorMessage","값을 제대로 입력해주세요");
        }
        // 예약승인여부를 업데이트 하는 서비스호출
        hospitalReservationService.updateReservationStatus(id,reservationStatus1);
        return new ResponseEntity<Long>(id, HttpStatus.OK);
    }

    // Ajax를 이용한 진료완료여부 변경(진료전/노쇼/진료완료)
    @PatchMapping("/reservations/{reservationId}")
    public ResponseEntity updateDiagnosisStatus(@PathVariable("reservationId") Long id, String diagnosisStatus){
        // Enum 형태로 담기위한 객체 선언 및 초기화

        DiagnosisStatus diagnosisStatus1 = null;
        System.out.println(diagnosisStatus);
        if(diagnosisStatus.equals("진료전")){
            // 진료를 받기 전인 경우
            diagnosisStatus1 = DiagnosisStatus.NOTYET;
        }else if(diagnosisStatus.equals("노쇼")){
            // 노쇼한 경우
            diagnosisStatus1 = DiagnosisStatus.NOSHOW;
        }else {
            // 진료완료인 경우
            diagnosisStatus1 = DiagnosisStatus.SUCCESS;
        }
        // 예약승인여부를 업데이트 하는 서비스호출
        hospitalReservationService.updateDiagnosisStatus(id,diagnosisStatus1);
        return new ResponseEntity<Long>(id, HttpStatus.OK);
    }


}
