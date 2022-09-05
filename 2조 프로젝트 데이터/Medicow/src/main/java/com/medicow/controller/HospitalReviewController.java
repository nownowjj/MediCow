package com.medicow.controller;

import com.medicow.model.dto.ReviewDto;
import com.medicow.model.entity.Hospital;
import com.medicow.service.HospitalReviewService;
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
public class HospitalReviewController {
    private final HospitalReviewService hospitalReviewService;
    private final HospitalService hospitalService;
    // 페이징 처리를 추가한 병원 리뷰 페이지(병원사이드)를 조회하는 컨트롤러 메소드
    @GetMapping(value={"/hospital/review","/hospital/review/{page}"})
    public String getMyReviews(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){
        // 페이징 객체 생성
        Pageable pageable = PageRequest.of(page.isPresent()?page.get():0,5);
        // Principal객체를 활용하여 현재 접속자의 아이디를 가지고와서 병원 Entity를 가져옴
        String hosId = principal.getName();
        Hospital hospital = hospitalService.findByHosId(hosId);
        // 해당 Entity와 페이징 객체를 넘겨서 페이징 처리가 된 리뷰Dto객체를 가져옴
        Page<ReviewDto> reviews = hospitalReviewService.findReviewByHospital(hospital,pageable);
        // 모델에 해당 리뷰를 바인딩
        model.addAttribute("reviews",reviews);
        // 한 화면에 보여줄 최대 페이지 수는 5
        model.addAttribute("maxPage",5);

        return "/hospital/review/review";
    }
    // 리뷰 내용에 대한 삭제 요청을 보내는 컨트롤러 메소드
    @PatchMapping(value="/hospital/deleteRequest/{reviewId}")
    public ResponseEntity deleteRequest(@PathVariable("reviewId")Long reviewId){
        hospitalReviewService.deleteRequest(reviewId);
        return new ResponseEntity<Long>(reviewId,HttpStatus.OK);
    }


}
