package com.medicow.controller;

import com.medicow.model.constant.ActiveStatus;
import com.medicow.model.constant.DeleteStatus;
import com.medicow.model.dto.ReviewSearchDto;
import com.medicow.model.entity.Review;
import com.medicow.service.AdminReviewService;
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

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AdminReviewController {

    private final AdminReviewService adminReviewService;

    @GetMapping(value = {"admin/review", "/admin/review/{page}"})
    public String getHospitalReviews(ReviewSearchDto reviewSearchDto,
                                     @PathVariable("page") Optional<Integer> page,
                                     Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Review> datas = adminReviewService.findReviewByAdmin(reviewSearchDto, pageable);

        model.addAttribute("datas", datas);
        model.addAttribute("reviewSearchDto", reviewSearchDto); // for 검색 조건 보존
        model.addAttribute("maxPage", 5); // 하단에 보여줄 페이지 번호의 최대 개수

        return "/admin/review";
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity updateReview(@PathVariable("reviewId") Long id, String deleteStatus){
        // Enum 형태로 담기위한 객체 선언 및 초기화
        System.out.println(deleteStatus);
        DeleteStatus deleteStatus1 = null;
        // 이 메소드를 호출을 못하고 있음
        System.out.println(deleteStatus);
        if(deleteStatus.equals("수락")){
//            System.out.println("호출이 되나요?");
            deleteStatus1 = DeleteStatus.YES;
            ActiveStatus activeStatus1 = ActiveStatus.UNABLE;
            adminReviewService.updateActiveStatus(id, activeStatus1);
        }else if(deleteStatus.equals("반려")){
            // 리뷰 삭제를 거절
            deleteStatus1 = DeleteStatus.NO;

        }else {
            // 리뷰 대기중
            deleteStatus1 = DeleteStatus.WAIT;
        }
        // 리뷰 삭제 여부 업데이트
        adminReviewService.updateDeleteStatus(id, deleteStatus1);

        return new ResponseEntity<Long>(id, HttpStatus.OK);
    }



}
