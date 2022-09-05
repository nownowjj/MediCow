package com.medicow.controller;

import com.medicow.model.constant.DayStatus;
import com.medicow.model.dto.DayTableDto;
import com.medicow.model.entity.DayTable;
import com.medicow.model.entity.Hospital;
import com.medicow.service.HospitalInfoService;
import com.medicow.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HospitalInfoController {
    private final HospitalInfoService hospitalInfoService;
    private final HospitalService hospitalService;

    //이전에 저장된 시간을 가져오는 컨트롤러 메소드
    @GetMapping(value = "/hospital/info/time")
    public String takeTime(Principal principal, Model model){
        String hosId = principal.getName();
        // try-catch 문으로 시간이 등록되어있지 않는 경우 시간을 입력하는 페이지로 이동

        try{
            // null인지 아닌지 확인
            List<DayTableDto> dayTableDtoLists = new ArrayList<DayTableDto>();
            dayTableDtoLists = hospitalInfoService.searchMyTime(hosId);
            if (dayTableDtoLists == null){
                model.addAttribute("errorMessage", "입력된 값이 없습니다.");
                return "hospital/info/timeTable";
            }
            List<DayTableDto> dayTableDtos = new ArrayList<DayTableDto>();
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


            // null이면 catch문으로 이동하고 아래 코드부터는 에러가 없음
            model.addAttribute("dayTableDtos",dayTableDtos);

        }catch(Exception e){
            // null값이면 시간 등록 페이지로 이동
            List<DayTableDto> dayTableDtos = new ArrayList<DayTableDto>();
            model.addAttribute("dayTableDtos",dayTableDtos);
            model.addAttribute("errorMessage",e.getMessage());
            return "hospital/info/timeTable";
        }
        return "hospital/info/timeTable";
    } // 이전에 저장된 시간을 가져오거나 신규 시간 등록 페이지로 이동하는 컨트롤러 메소드 종료

    // 영업시간 입력을 하는 컨트롤러 메소드
    @GetMapping(value = "/hospital/info/time/insert")
    // 총 8개의 DayTableDto() 생성
    public String insertTime(Model model) {
        DayTableDto[] dayTableDtos = {new DayTableDto("월요일"), new DayTableDto("화요일"), new DayTableDto("수요일"), new DayTableDto("목요일"), new DayTableDto("금요일"), new DayTableDto("토요일"), new DayTableDto("일요일"), new DayTableDto("점심시간")};
        for (int i = 0; i < 8; i++) {
            dayTableDtos[i].setId((long) i);

        }
        model.addAttribute("dayTableDtos",dayTableDtos);
        return "hospital/info/timeInsert";
    }
    // 신규로 작성한 영업시간을 저장하기 위한 메소드
    @PostMapping(value="/hospital/info/time/insert")
    public String saveTimeTable(DayTableDto dayTableDto, BindingResult bindingResult, Principal principal, Model model){
        // 에러 발생 시 돌아감
        if(bindingResult.hasErrors()){
            return "/hospital/info/time/insert";
        }
        // 현재 접속중인 병원의 아이디를 받아옴
        String hosId = principal.getName();
        // 확인용
        System.out.println("hosid = "+hosId);

        try{
            System.out.println("서비스 들어갑니다.");
            System.out.println(dayTableDto.getDayStatus());
            System.out.println(dayTableDto.getStartDateTime());
            System.out.println(dayTableDto.getEndDateTime());
            System.out.println(dayTableDto.getWorkStatus());
            Hospital hospital = hospitalService.findByHosId(hosId);
            int sum = 0;
            // 모든 요일이 들어왔는지 확인하기 위함 11,111,111이 되어야 모든 요일 + 점심시간이 들어온 것.
            for (int i = 0; i < 8; i++) {
                String days=dayTableDto.getDayStatus().split(",")[i];
                // 스위치 문으로 각 요일이 제대로 들어왔는지 확인
//                switch (days){
//                    case "월요일": sum+=1;
//                        break;
//                    case "화요일": sum+=10;
//                        break;
//                    case "수요일": sum+=100;
//                        break;
//                    case "목요일": sum+=1000;
//                        break;
//                    case "금요일": sum+=10000;
//                        break;
//                    case "토요일": sum+=100000;
//                        break;
//                    case "일요일": sum+=1000000;
//                        break;
//                    case "점심시간": sum+=10000000;
//                        break;
//                }
                // 휴무일은 pass / 진료일인 경우 시간을 비교함
                if(dayTableDto.getWorkStatus().split(",")[i].equals("진료일")) {
                    try {
                        if (Integer.parseInt(dayTableDto.getStartDateTime().split(",")[i].split(":")[0]) < Integer.parseInt(dayTableDto.getEndDateTime().split(",")[i].split(":")[0])) {
                            // 시작 시간보다 끝나는 시간이 더 작은 경우
                        } else if(Integer.parseInt(dayTableDto.getStartDateTime().split(",")[i].split(":")[0]) == Integer.parseInt(dayTableDto.getEndDateTime().split(",")[i].split(":")[0])) {
                            if (Integer.parseInt(dayTableDto.getStartDateTime().split(",")[i].split(":")[1]) > Integer.parseInt(dayTableDto.getEndDateTime().split(",")[i].split(":")[1])) {
                                hospitalInfoService.findError();
                            }
                        } else{
                            hospitalInfoService.findError();
                        }

                    } catch (Exception e) {
                        model.addAttribute("errorMessage","시간이 잘못 입력되었습니다.");
                        DayTableDto[] dayTableDtos = {new DayTableDto("월요일"), new DayTableDto("화요일"), new DayTableDto("수요일"), new DayTableDto("목요일"), new DayTableDto("금요일"), new DayTableDto("토요일"), new DayTableDto("일요일"), new DayTableDto("점심시간")};

                        for (int j = 0; j < 8; j++) {
                            dayTableDtos[j].setId((long) j);
                        }
                        System.out.println("Error발생");
                        model.addAttribute("dayTableDtos",dayTableDtos);
                        return "hospital/info/timeInsert";
                    }
                }
            }
            // 모든 요일이 다 안들어 있는 경우
//            if(sum != 11111111){
//                // 예외발생 alert
//                model.addAttribute("errorMessage","요일지정 및 점심시간을 다시 지정해주세요. 중복으로 입력된 부분이 있습니다.");
//
//                DayTableDto[] dayTableDtos = {new DayTableDto(), new DayTableDto(), new DayTableDto(), new DayTableDto(), new DayTableDto(), new DayTableDto(), new DayTableDto(), new DayTableDto()};
//                for (int i = 0; i < 8; i++) {
//                    dayTableDtos[i].setId((long) i);
//                }
//                model.addAttribute("dayTableDtos",dayTableDtos);
//                return "hospital/info/timeInsert";
//            }
            // 전체 값을 집어 넣는 과정

            String[] dates={DayStatus.MON.toString(),DayStatus.TUE.toString(),DayStatus.WED.toString(),DayStatus.THU.toString(),DayStatus.FRI.toString(),DayStatus.SAT.toString(),DayStatus.SUN.toString(),DayStatus.LUNCH.toString()};

            for (int i = 0; i < 8; i++) {
                // DayTabelDto객체에 값을 넣음
                DayTableDto dto = new DayTableDto();
                dto.setDayStatus(dates[i]);
                dto.setDayStatus(dayTableDto.getDayStatus().split(",")[i]);
                dto.setStartDateTime(dayTableDto.getStartDateTime().split(",")[i]);
                dto.setEndDateTime(dayTableDto.getEndDateTime().split(",")[i]);
                dto.setWorkStatus(dayTableDto.getWorkStatus().split(",")[i]);

                hospitalInfoService.saveDayTable(DayTable.createDayTable(dto,hospital));
            }
        }catch (IllegalStateException e){
            return "/hospital/info/time";
        }
        return "redirect:/hospital/info/time";
    }

    // 영업 시간 수정을 위한 컨트롤러 메소드
    @GetMapping(value="/hospital/info/time/update")
    public String updateTimeTable(Principal principal, Model model){
        String hosId = principal.getName();
        // try-catch 문으로 시간이 등록되어있지 않는 경우 시간을 입력하는 페이지로 이동

        try{
            // null인지 아닌지 확인
            List<DayTableDto> dayTableDtoLists = new ArrayList<DayTableDto>();
            dayTableDtoLists = hospitalInfoService.searchMyTime(hosId);
            if (dayTableDtoLists == null){
                return "hospital/info/timeTable";
            }
            List<DayTableDto> dayTableDtos = new ArrayList<DayTableDto>();
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

        }catch(IllegalStateException e){
            // null값이면 시간 등록 페이지로 이동
            List<DayTableDto> dayTableDtos = new ArrayList<DayTableDto>();
            model.addAttribute("dayTableDtos",dayTableDtos);
            model.addAttribute("errorMessage",e.getMessage());
            return "hospital/info/timeTable";
        }
        return "/hospital/info/timeUpdate";
    }

    @PostMapping(value="/hospital/info/time/update")
    public String updateTimeTable(DayTableDto dayTableDto, BindingResult bindingResult, Principal principal, Model model){
        String hosId = principal.getName();
        Hospital hospital = hospitalService.findByHosId(hosId);

        System.out.println("서비스 들어갑니다.");
        System.out.println(dayTableDto.getDayStatus());
        System.out.println(dayTableDto.getStartDateTime());
        System.out.println(dayTableDto.getEndDateTime());
        System.out.println(dayTableDto.getWorkStatus());
        System.out.println(dayTableDto.getId());

        for (int i = 0; i < 8; i++) {
            // DayTabelDto객체에 값을 넣음
            DayTableDto dto = new DayTableDto();
            dto.setDayStatus(dayTableDto.getDayStatus().split(",")[i]);
            dto.setStartDateTime(dayTableDto.getStartDateTime().split(",")[i]);
            dto.setEndDateTime(dayTableDto.getEndDateTime().split(",")[i]);
            dto.setWorkStatus(dayTableDto.getWorkStatus().split(",")[i]);
            if(dto.getWorkStatus().equals("진료일")){
                System.out.println("여기 오니?"+i);
                try{
                    if(Integer.parseInt(dto.getStartDateTime().split(":")[0]) <= Integer.parseInt(dto.getEndDateTime().split(":")[0])){
                        if(Integer.parseInt(dto.getStartDateTime().split(":")[1]) > Integer.parseInt(dto.getEndDateTime().split(":")[1])){
                            hospitalInfoService.findError();
                        }
                    }else{
                        hospitalInfoService.findError();
                    }
                }catch(IllegalStateException e){
                    model.addAttribute("errorMessage","시간이 잘못 입력되었습니다.");
                    List<DayTableDto> dayTableDtos = new ArrayList<DayTableDto>();
                    dayTableDtos = hospitalInfoService.searchMyTime(hosId);

                    // null이면 catch문으로 이동하고 아래 코드부터는 에러가 없음
                    model.addAttribute("dayTableDtos",dayTableDtos);

                    System.out.println("Error발생");
                    return "hospital/info/timeUpdate";
                }
            }

            dto.setId(dayTableDto.getId()+i);
            DayTable dayTable = DayTable.createDayTable(dto,hospital);
            hospitalInfoService.updateMyTable(dayTable.getStartDateTime(),dayTable.getEndDateTime(),dayTable.getWorkStatus(),dto.getId());
        }

        return "redirect:/hospital/info/time";
    }

}