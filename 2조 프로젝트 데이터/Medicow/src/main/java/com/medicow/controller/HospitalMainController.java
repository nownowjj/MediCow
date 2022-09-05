package com.medicow.controller;

import com.medicow.model.constant.DayStatus;
import com.medicow.model.constant.DeleteStatus;
import com.medicow.model.constant.ReservationStatus;
import com.medicow.model.dto.*;
import com.medicow.model.entity.Hospital;
import com.medicow.model.entity.HospitalImg;
import com.medicow.model.entity.Reservation;
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
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/*
@Controller와 @GetMapping은 짝꿍(반드시 같이 써줘야 합니다)
 */

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/hospital")
public class HospitalMainController {
    private final HospitalInfoService hospitalInfoService;
    private final HospitalService hospitalService;
    private final HospitalReservationService hospitalReservationService;
    private final ContactService contactService;
    private final HospitalReviewService hospitalReviewService;
    private final NoticeService noticeService;
    private final ReviewService reviewService;
    private final HospitalImgService hospitalImgService;

    @GetMapping(value = "/main")
    public String main(Principal principal, Model model){
        // 병원 운영 시간
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        DayStatus dayStatus = null;
        switch (dayOfWeek) {
            case 1:
                dayStatus= DayStatus.SUN;
                break;
            case 2:
                dayStatus= DayStatus.MON;
                break;
            case 3:
                dayStatus= DayStatus.TUE;

                break;
            case 4:
                dayStatus= DayStatus.WED;
                break;
            case 5:
                dayStatus= DayStatus.THU;
                break;
            case 6:
                dayStatus= DayStatus.FRI;
                break;
            case 7:
                dayStatus= DayStatus.SAT;

                break;
        }
        String hosId = principal.getName();
        Hospital hospital = hospitalService.findByHosId(hosId);
        MainDto mainDto =  hospitalInfoService.searchTodayTime(hospital,dayStatus);

        // 현재 예약 환자 수
        Long curNo= hospitalReservationService.curReservationNo(hospital, ReservationStatus.RESERVATION);
        mainDto.setCurReservationNo(curNo);


        //현재 예약 대기자 수
//        Long waitNo = hospitalReservationService.curWaitingNo(hospital, ReservationStatus.WAIT, DiagnosisStatus.NOTYET);

        Long waitNo = hospitalReservationService.curWaitingNo(hospital);
        mainDto.setCurWaitingNo(waitNo);


        // 총 리뷰 수
        Long  reviewCount= hospitalReviewService.curReviewCount(hospital, DeleteStatus.NO);
        mainDto.setReviewCount(reviewCount);

        String reviewTotal = hospitalReviewService.curReviewTotal(hospital, DeleteStatus.NO);
        mainDto.setReviewTotal(reviewTotal);

        //문의사항 게시판
        List<ContactFormDto> contactFormDtos = contactService.findTop3ByHospitalOrderByDesc(hospital);
        mainDto.setContactFormDtos(contactFormDtos);
        model.addAttribute("mainDto", mainDto);

        //공지사항 게시판
        List<NoticeFormDto> noticeFormDtos = noticeService.findTop3OrderByDesc();
        mainDto.setNoticeFormDtos(noticeFormDtos);
        model.addAttribute("mainDto", mainDto);

        return "hospital/main";
    }

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
    public String hosDetail(@PathVariable("page") Optional<Integer> page,@PathVariable("hosNo") Long hosNo, Model model){
        System.out.println("================예약 페이지로 이동===================== ");

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 1000);
        Page<Reservation> reservations = reviewService.getReview(hosNo, pageable);
        List<HospitalImg> hospitalImgs = hospitalImgService.getHosImg(hosNo);
        HospitalDetailDto hospitalDetailDto = hospitalService.getHospDtl(hosNo);

        System.out.println("================================== HospitalMainController");
        System.out.println(hospitalImgs.size());
        System.out.println("================================== HospitalMainController");

        model.addAttribute("hospital",hospitalDetailDto);
        model.addAttribute("reservations", reservations);
        model.addAttribute("hospitalImgs", hospitalImgs);

        return "/hospital/hospDetail";
    }
}
