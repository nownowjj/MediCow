package com.medicow.repository.inter;

import com.medicow.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findByReviewId(Long reviewId);
}