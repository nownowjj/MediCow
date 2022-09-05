package com.medicow.service;

import com.medicow.model.constant.ActiveStatus;
import com.medicow.model.constant.DeleteStatus;
import com.medicow.model.dto.ReviewSearchDto;
import com.medicow.model.entity.Review;
import com.medicow.repository.inter.AdminReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminReviewService {

    private final AdminReviewRepository adminReviewRepository;

    // 관리자에서 추가(김근오)
    // 모든 병원에 대한 리뷰 데이터를 가져 옵니다.
    @Transactional(readOnly = true)
    public Page<Review> findReviewByAdmin(ReviewSearchDto reviewSearchDto, Pageable pageable){
        // 상품 검색 조건을 이용하여 페이징 객체를 반환합니다.
        return adminReviewRepository.getMainReviewPage(reviewSearchDto, pageable);
    }

    // 관리자가 리뷰 삭제 승인 여부를 바꾸는 메소드(YES, NO, WAIT)
    @Transactional
    public void updateDeleteStatus(Long id, DeleteStatus deleteStatus){
        adminReviewRepository.updateDeleteStatus(deleteStatus,id);
    }

    // 관리자가 삭제를 하는 메소드 (ABLE, UNABLE만 바꿔줍니다.)
    @Transactional
    public void updateActiveStatus(Long id, ActiveStatus activeStatus){
        adminReviewRepository.updateActiveStatus(activeStatus, id);
    }
}
