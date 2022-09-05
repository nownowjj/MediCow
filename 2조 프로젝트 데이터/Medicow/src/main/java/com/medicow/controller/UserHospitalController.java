package com.medicow.controller;

import com.medicow.model.dto.HospitalDetailDto;
import com.medicow.model.dto.HospitalFindDto;
import com.medicow.model.entity.Hospital;
import com.medicow.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;



@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/user/hospital")
public class UserHospitalController {
    private final HospitalService hospitalService;

    @GetMapping(value = {"/find", "/find/{page}"})
    public String hosp_main(HospitalFindDto hospitalFindDto, @PathVariable("page") Optional<Integer> page, Principal principal, Model model){
        System.out.println("=========================================== HospitalController");
        Pageable pageable = PageRequest.of(0, 100);

        Page<Hospital> hospitalList = hospitalService.getHospList(hospitalFindDto, pageable);

        model.addAttribute("hospitalFindDto", hospitalFindDto);
        model.addAttribute("hospitalList", hospitalList);
        model.addAttribute("maxPage", 5);

        return "/hospital/hospFind";
    }

    @GetMapping(value = "/detail/{hosNo}")
    public String hosDetail(Model model, @PathVariable("hosNo") Long hosNo){
        System.out.println("================예약 페이지로 이동===================== ");
        HospitalDetailDto hospitalDetailDto = hospitalService.getHospDtl(hosNo);
        model.addAttribute("hospital",hospitalDetailDto);
        return "/hospital/hospDetail";
    }


}
