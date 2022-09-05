package com.medicow.controller;

import com.medicow.model.dto.ReservationDto;
import com.medicow.model.dto.ReservationListDto;
import com.medicow.model.entity.Reservation;
import com.medicow.service.MemberReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ReservationController {
//    private final HospitalReservationService hospitalReservationService ;
    private final MemberReservationService memberReservationService;

    @PostMapping(value="/user/reservation")
    // @RequestBody와 @ResponseBody는 스프링의 비동기 처리(ajax)시에 사용되는 어노테이션입니다.
    public @ResponseBody ResponseEntity order(@RequestBody @Valid ReservationDto reservationDto,
                                              BindingResult bindingResult,
                                              Principal principal){
        String email = principal.getName() ;
        String hosId = reservationDto.getHosId();
        String symptom = reservationDto.getSymptom();
        Long id =memberReservationService.saveReservation(hosId,email,symptom);

        return new ResponseEntity<Long>(id, HttpStatus.OK) ;
    }
    // @ResponseBody는 자바 객체를 다시 http 응답 객체로 변경해 줍니다.

    @GetMapping(value = {"/user/member/reservation", "/user/member/reservation/{page}"})
    public String memberReservation(ReservationListDto reservationListDto, @PathVariable("page")Optional<Integer> page, Model model, Principal principal){

        Pageable pageable = (Pageable) PageRequest.of(page.isPresent() ? page.get() : 0, 2);

        reservationListDto.setSearchEmail(principal.getName());
        Page<Reservation> reservations = memberReservationService.getReservation(reservationListDto, pageable);

        model.addAttribute("reservationListDto", reservationListDto);
        model.addAttribute("reservations", reservations);
        model.addAttribute("maxPage", 5);

        return "/member/reservation";
    }

}
