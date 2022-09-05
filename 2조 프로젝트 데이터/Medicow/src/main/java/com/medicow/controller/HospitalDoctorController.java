package com.medicow.controller;

import com.medicow.model.dto.DoctorFormDto;
import com.medicow.model.entity.Doctor;
import com.medicow.service.HospitalDoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(value = "/hospital/doctors")
@RequiredArgsConstructor
public class HospitalDoctorController {
    private final HospitalDoctorService hospitalDoctorService;

    @GetMapping(value = "/start") // 추후에 병원 정보도 넘어가게 바꿔줘야 함!
    public String commonInPage(Model model, Principal principal){

        String hosId = principal.getName(); // 로그인 정qh
        return "hospital/member/myDoctors"; // 이건 병원정보 페이지로 변경되어야 함
    }
    @PostMapping(value = "/start") // 추후에 병원 정보도 넘어가게 바꿔줘야 함!
    public String commonOutPage(Model model, Principal principal){

        String hosId = principal.getName(); // 로그인 정보
        model.addAttribute("hosId",hosId); // 로그인 정보를 바인딩해서 넘겨줌

        return "hospital/member/myDoctors"; // 의사정보조회 페이지로 넘어감. 나중엔 병원정보페이지로 바뀌어야 함.
    }

    @GetMapping(value = "/new")
    public String addDoctorForm(Model model){
        model.addAttribute("doctorFormDto", new DoctorFormDto());
        return "hospital/member/addDoctor"; // 의사 추가 페이지
    }
    @PostMapping(value = "/new")
    public String addDoctor(@Valid DoctorFormDto doctorFormDto, BindingResult bindingResult, Model model, @RequestParam("doctorImgFile")MultipartFile doctorImgFile, Principal principal){
        String whenError = "hospital/member/addDoctor";
        System.out.println("포스트 매핑 들어옴");
        if (bindingResult.hasErrors()) {
            System.out.println("폼에 문제가 있음");
            return whenError;
        }
        if (doctorImgFile.isEmpty()) {
            System.out.println("이미지 비었음");
            model.addAttribute("errorMessage", "이미지는 필수입니다");
            return whenError;
        }
        List<Doctor> doctorList = null;

        try {
            Doctor doctor = Doctor.createDoctor(doctorFormDto);
            String hosId = principal.getName();
            doctorList = hospitalDoctorService.searchMyDoctors(hosId);
            hospitalDoctorService.addDoctor(doctor,doctorImgFile,hosId);

            model.addAttribute("doctorDtoList", doctorList);
            return "redirect:/hospital/doctors/lists"; // 의사 조회 페이지
        } catch(Exception ex){
            model.addAttribute("errorMessage", ex.getMessage());
            return whenError;
        }

    }

    @GetMapping(value = "/lists")
    public String selectDoctors(Model model, Principal principal) {
        List<Doctor> doctorList = null;
        try {
            String hosId = principal.getName();
            doctorList = hospitalDoctorService.searchMyDoctors(hosId);
            System.out.println("내가 닥터리스트 들고옴");
            System.out.println(doctorList.toString());
            model.addAttribute("doctorDtoList", doctorList);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "의사 명단이 존재하지 않습니다. \n 설정해주세요");
            model.addAttribute("doctorDtoList", doctorList);
            e.printStackTrace();
        }
        return "hospital/member/myDoctors";
    }

    @GetMapping(value="/{docId}")
    public String detailDoctor(@PathVariable("docId") Long docId, Model model) {
        DoctorFormDto doctorFormDto = null;
        try {
            doctorFormDto = hospitalDoctorService.findDoctor(docId);
            model.addAttribute("doctorFormDto", doctorFormDto);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("doctorFormDto", doctorFormDto);
            model.addAttribute("errorMessage", "해당 의사가 존재하지 않습니다.");
        }
        return "hospital/member/detailDoctor";
    }

    @GetMapping(value="/{docId}/update")
    public String updateDoctor(@PathVariable("docId") Long docId, Model model) {
        DoctorFormDto doctorFormDto = null;
        try {
            doctorFormDto = hospitalDoctorService.findDoctor(docId);
            model.addAttribute("doctorFormDto", doctorFormDto);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("doctorFormDto", doctorFormDto);
            model.addAttribute("errorMessage", "해당 의사가 존재하지 않습니다.");
        }
        return "hospital/member/addDoctor";
    }

    @PostMapping(value = "/{docId}/update")
    public String updateDoctorComplete(@Valid DoctorFormDto doctorFormDto, BindingResult bindingResult, Model model, @RequestParam("doctorImgFile") MultipartFile doctorImgFile, Principal principal){
        String whenError = "hospital/member/addDoctor";
        System.out.println("포스트 매핑 들어옴");
        if (bindingResult.hasErrors()) {
            System.out.println("폼에 문제가 있음");
            return whenError;
        }
        if (doctorImgFile.isEmpty()) {
            System.out.println("이미지 비었음");
            model.addAttribute("errorMessage", "이미지는 필수입니다");
            return whenError;
        }
        try {
            hospitalDoctorService.updateDoctor(doctorFormDto, doctorImgFile);
            String url = "/hospital/doctors/"+doctorFormDto.getDocId();
            System.out.println(url+"여기로 이동할 것이야!!!");
            return "redirect:"+url;
        } catch(Exception ex){
            model.addAttribute("errorMessage", ex.getMessage());
            ex.printStackTrace();
            return whenError;
        }
    }
}
