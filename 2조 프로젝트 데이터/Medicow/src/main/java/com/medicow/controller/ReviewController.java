package com.medicow.controller;

import com.medicow.model.dto.ReviewDto;
import com.medicow.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor

public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping(value = {"/user/member/review", "/user/member/review/{reservationId}"})
    public String review(ReviewDto reviewDto, @PathVariable("reservationId") Long reservationId, Model model){
        reviewDto = reviewService.getReservationInfo(reservationId);
        model.addAttribute("reviewDto",reviewDto);
        return "/reviews/review";
    }

    @PostMapping(value = "/user/member/review/{reservationId}")
    public String createReview(ReviewDto reviewDto, @PathVariable("reservationId") Long reservationId){
        ReviewDto reviewDto1 = reviewService.getReservationInfo(reservationId);

        if(reviewDto1.getContents() == null){
            reviewService.createReview(reviewDto);
        }else{
            reviewService.updateReview(reviewDto);
        }
        return "redirect:/user/member/reservation/";
    }

}
