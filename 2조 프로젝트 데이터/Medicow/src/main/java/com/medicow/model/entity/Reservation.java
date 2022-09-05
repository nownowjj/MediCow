package com.medicow.model.entity;

import com.medicow.model.constant.DiagnosisStatus;
import com.medicow.model.constant.ReservationStatus;
import com.medicow.model.dto.ReviewDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@Getter @Setter @Entity @Table(name = "reservation")
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="hosNo")
    private Hospital hospital;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="email")
    private Member member;

    private String content;
    private String symptom;

    //
    private ReservationStatus reservationStatus;

    // 진찰여부
    private DiagnosisStatus diagnosisStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public void updateReview(ReviewDto reviewDto) {
        this.review.setContent(reviewDto.getContents());
        this.review.setPoint(reviewDto.getPoint());
    }

}
