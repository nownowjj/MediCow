package com.medicow.controller;

import com.medicow.model.dto.DayTableDto;
import com.medicow.model.dto.HospitalFormDto;
import com.medicow.model.dto.HospitalUpdateFormDto;
import com.medicow.model.dto.MyHospitalDto;
import com.medicow.model.entity.Doctor;
import com.medicow.model.entity.Hospital;
import com.medicow.model.entity.HospitalImg;
import com.medicow.service.HospitalDoctorService;
import com.medicow.service.HospitalImgService;
import com.medicow.service.HospitalInfoService;
import com.medicow.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/hospital/my")
public class HospitalMyController { // 병원 정보에 관한 컨트롤러

    // 병원정보 페이지에서 보여주기 위해 의사 관련 데이터를 와야함.
    private final HospitalDoctorService hospitalDoctorService;
    private final HospitalInfoService hospitalInfoService;
    private final HospitalImgService hospitalImgService;
    private final HospitalService hospitalService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/info")
    public String hospitalInfo(Model model, Principal principal, MyHospitalDto myHospitalDto) {
        String hosId = principal.getName();

        // 현재 로그인한 병원의 정보를 가져옴
        List<Doctor> doctorList = null;
        Hospital hospital = hospitalService.findByHosId(hosId);
         myHospitalDto = MyHospitalDto.createHospitalFormDto(hospital);
        try{
            doctorList = hospitalDoctorService.searchMyDoctors(hosId);
            if (doctorList == null) {
                List<Doctor> doctorDtoList = new ArrayList<Doctor>();
                model.addAttribute("doctorDtoList",doctorDtoList);
            }
            System.out.println("-----------------------------------여기여기여기여기여기여ㅣㄱ여기");
            model.addAttribute("doctorDtoList", doctorList);
            List<DayTableDto> dayTableDtoLists = null;
            dayTableDtoLists = hospitalInfoService.searchMyTime(hosId);
            List<DayTableDto> dayTableDtos = new ArrayList<DayTableDto>();

            if (dayTableDtoLists == null){
                model.addAttribute("errorMessage", "입력된 값이 없습니다.");
                model.addAttribute("dayTableDtos",dayTableDtos); // 빈 시간테이블 리턴
                System.out.println("1111111");

            }else{

            for (DayTableDto dayTableDto : dayTableDtoLists){
                if(dayTableDto.getDayStatus()=="월요일"){
                    dayTableDtos.add(dayTableDto);
                }
            }
            for (DayTableDto dayTableDto : dayTableDtoLists){
                if(dayTableDto.getDayStatus()=="화요일"){
                    dayTableDtos.add(dayTableDto);
                }
            }
            for (DayTableDto dayTableDto : dayTableDtoLists){
                if(dayTableDto.getDayStatus()=="수요일"){
                    dayTableDtos.add(dayTableDto);
                }
            }
            for (DayTableDto dayTableDto : dayTableDtoLists){
                if(dayTableDto.getDayStatus()=="목요일"){
                    dayTableDtos.add(dayTableDto);
                }
            }
            for (DayTableDto dayTableDto : dayTableDtoLists){
                if(dayTableDto.getDayStatus()=="금요일"){
                    dayTableDtos.add(dayTableDto);
                }
            }
            for (DayTableDto dayTableDto : dayTableDtoLists){
                if(dayTableDto.getDayStatus()=="토요일"){
                    dayTableDtos.add(dayTableDto);
                }
            }
            for (DayTableDto dayTableDto : dayTableDtoLists){
                if(dayTableDto.getDayStatus()=="일요일"){
                    dayTableDtos.add(dayTableDto);
                }
            }
            for (DayTableDto dayTableDto : dayTableDtoLists){
                if(dayTableDto.getDayStatus()=="점심시간"){
                    dayTableDtos.add(dayTableDto);
                }
            }
                model.addAttribute("dayTableDtos",dayTableDtos);

            }

            // null이면 catch문으로 이동하고 아래 코드부터는 에러가 없음
            HospitalImg hospitalImg = hospitalImgService.findYnHosImg(hospital);
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // 여기서부터 확인
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            myHospitalDto.setHosImgUrl(hospitalImg.getImgUrl());
            model.addAttribute("myHospitalDto", myHospitalDto);
            System.out.println("여기서어어어어어어");
            return "hospital/hospitalInfo/myHospital";

        }catch(Exception e){
            // null값이면 시간 등록 페이지로 이동
            List<DayTableDto> dayTableDtos = new ArrayList<DayTableDto>();
            model.addAttribute("dayTableDtos",dayTableDtos);
            model.addAttribute("errorMessage",e.getMessage());
            System.out.println("문제가 생겼어");
            e.printStackTrace();

            return "hospital/hospitalInfo/myHospital";

        }

    } // 이전에 저장된 시간을 가져오거나 신규 시간 등록 페이지로 이동하는 컨트롤러 메소드 종료

    @GetMapping(value="/members/{hosNo}/select")
    public String findByHospital(@PathVariable("hosNo") Long hosNo, Model model, Principal principal,MyHospitalDto myHospitalDto) {
        try{
//            String hosId = principal.getName();
//            Hospital hospital = hospitalService.findByHosId(hosId);
//            HospitalImg hospitalImg = hospitalImgService.findYnHosImg(hospital);
//            myHospitalDto.setHosImgUrl(hospitalImg.getImgUrl());
            HospitalUpdateFormDto hospitalFormDto = hospitalService.updateMyHospital(hosNo);
            model.addAttribute("hospitalFormDto", hospitalFormDto);
        }catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 병원입니다.");
            model.addAttribute("hospitalFormDto", new HospitalFormDto());

        }
        return "hospital/hospitalInfo/memberFormSelect";
    }

    @GetMapping(value="/members/{hosNo}/update")
    public String findByHospitalForUpdate(@PathVariable("hosNo") Long hosNo, Model model) {
        try{
            HospitalUpdateFormDto hospitalUpdateFormDto = hospitalService.updateMyHospital(hosNo);
            System.out.println(hospitalUpdateFormDto.getHospitalImgIds());
            model.addAttribute("hospitalFormDto", hospitalUpdateFormDto);
        }catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 병원입니다.");
            model.addAttribute("hospitalFormDto", new HospitalFormDto());

        }
        return "hospital/hospitalInfo/memberFormUpdate";
    }



    @PostMapping(value="/members/{hosNo}/update")
    public String updateByHospital(@Valid HospitalUpdateFormDto hospitalFormDto, BindingResult bindingResult,
                                   @RequestParam("hospitalImgFile") List<MultipartFile> hospitalImgFileList, Model model, Principal principal){ // RequestParam은 파라미터 하나 챙길때 사용
        // itemFormDto) 화면에서 넘어오는 command 객체
        // bindingResult) 폼 유효성 검사시 문제가 있는지 체크하기 위한 클래스
        // itemImgFileList) 폼에서 넘어 오는 여러 개의 <input type="file"> 객체 리스트
        // model) 뷰 영역으로 넘겨줄 정보들을 바인딩하기 위한 모델 객체
        System.out.println("여기로 옴");
        String whenError = "hospital/hospitalInfo/memberFormUpdate";

        if(bindingResult.hasErrors()){

            String hosId = principal.getName();
            Long hosNo = hospitalService.findHosNoByHosId(hosId);
            HospitalUpdateFormDto hospitalUpdateFormDto = hospitalService.updateMyHospital(hosNo);
            System.out.println(hospitalUpdateFormDto.getHospitalImgIds());
            model.addAttribute("hospitalFormDto", hospitalUpdateFormDto);


            model.addAttribute("errorMessage", "비밀번호가 올바르지 않습니다.");
            System.out.println("폼에 문제 있음");
            return whenError;
        }

        if(hospitalImgFileList.get(0).isEmpty()&& hospitalFormDto.getHosNo()==null){
            model.addAttribute("errorMessage","첫 번째 상품 이미지는 필수 입력 값입니다.");
            return whenError;
        }
        try{
            System.out.println("츄라이");
            System.out.println(hospitalFormDto.getHosSubject().length);
            hospitalService.updateHospital(hospitalFormDto, hospitalImgFileList,passwordEncoder);


        }catch (Exception e){
            model.addAttribute("errorMessage","상품 수정 중에 오류가 발생하였습니다");
            e.printStackTrace();
            return whenError;
        }
        return "redirect:/hospital/my/info";
    }

    @GetMapping(value = "/update/pwConfirm")
    public String  pwCheck(){
        return "hospital/hospitalInfo/checkBeforeUpdate";
    }

    @PostMapping(value = "/update/pwConfirm")
    public String  pwConfirm(@RequestParam("password") String password, Model model, Principal principal){
        String hosId = principal.getName();
        Hospital hospital = hospitalService.findByHosId(hosId);
        System.out.println("여긴 오니~~~~");
        int cnt = 0;
        try{
            if(passwordEncoder.matches(password, hospital.getHosPassword())) {
                System.out.println("비밀번호~~~~");
                cnt = 1;
            }else{
                System.out.println("너 안돼");
                return "redirect:/hospital/my/update/pwConfirm";

            }
            Long hosNo = hospitalService.findByHosId(hosId).getHosNo();
            HospitalUpdateFormDto hospitalUpdateFormDto = hospitalService.updateMyHospital(hosNo);
            System.out.println(hospitalUpdateFormDto.getHospitalImgIds());
            model.addAttribute("hospitalFormDto", hospitalUpdateFormDto);

//            HospitalUpdateFormDto hospitalUpdateFormDto = hospitalService.updateMyHospital(hosNo);
//            model.addAttribute("hospitalFormDto", hospitalUpdateFormDto);
        }catch(EntityNotFoundException e){
//            model.addAttribute("errorMessage", "존재하지 않는 병원입니다.");
//            model.addAttribute("hospitalFormDto", new HospitalFormDto());
            model.addAttribute("errorMessage", "비밀버호가 일치하지 않습니다.");
            return "hospital/hospitalInfo/checkBeforeUpdate";
        }
        return "hospital/hospitalInfo/memberFormUpdate";
    }
}
