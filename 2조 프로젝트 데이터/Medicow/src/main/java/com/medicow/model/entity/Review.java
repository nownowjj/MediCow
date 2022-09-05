package com.medicow.model.entity;

import com.medicow.model.constant.ActiveStatus;
import com.medicow.model.constant.DeleteStatus;
import com.medicow.model.dto.ReviewDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Getter @Setter @Table(name="review")
public class Review extends BaseEntity{
    // review 고유 번호
    @Id @Column(name="review_id") @GeneratedValue(strategy = GenerationType.AUTO)
    private Long reviewId;
    // 예약 고유번호
    @JoinColumn(name="id")
    @OneToOne(fetch = FetchType.LAZY)
    private Reservation reservation;
    // 평점
    private double point;
    // 리뷰 내용
    private String content;
    // 삭제요청 여부
    private DeleteStatus deleteStatus;

    public static Review createReview(ReviewDto reviewDto, Reservation reservation) {
        Review review = new Review();
        review.setDeleteStatus(DeleteStatus.NO);
        review.setActiveStatus(ActiveStatus.ABLE);
        review.setReservation(reservation);
        review.setPoint(reviewDto.getPoint());
        review.setContent(reviewDto.getContents());
        review.setRegTime(LocalDateTime.now());

        return review;
    }

}
