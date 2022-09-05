package com.medicow.repository.inter;

import com.medicow.model.dto.ReviewSearchDto;
import com.medicow.model.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminReviewRepositoryCustom {
    Page<Review> getMainReviewPage(ReviewSearchDto reviewSearchDto, Pageable pageable) ;
}
