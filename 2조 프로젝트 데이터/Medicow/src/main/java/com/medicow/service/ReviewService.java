package com.medicow.service;

import com.medicow.model.dto.ReviewDto;
import com.medicow.model.entity.Hospital;
import com.medicow.model.entity.Reservation;
import com.medicow.model.entity.Review;
import com.medicow.repository.inter.ReservationRepository;
import com.medicow.repository.inter.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final HospitalService hospitalService;

    public ReviewDto getReservationInfo(Long resId) {

        Reservation reservation = reservationRepository.findByResId(resId);

        ReviewDto reviewDto = new ReviewDto();

        reviewDto.setReviewId(reservation.getId());
        reviewDto.setHosNo(reservation.getHospital().getHosNo());
        reviewDto.setHosName(reservation.getHospital().getHosName());
        reviewDto.setWriteTime(reservation.getRegTime());
        reviewDto.setMemberNo(reservation.getMember().getId());
        reviewDto.setReservationId(reservation.getId());
        if(reservation.getReview() != null){
            reviewDto.setContents(reservation.getReview().getContent());
            reviewDto.setPoint(reservation.getReview().getPoint());
        }

        return reviewDto;
    }

    public void createReview(ReviewDto reviewDto) {
        Reservation reservation = reservationRepository.findByResId(reviewDto.getReservationId());


        Review review = Review.createReview(reviewDto, reservation);

        reviewRepository.save(review);

        reservation.setReview(review);
    }

    public void updateReview(ReviewDto reviewDto) {
        System.out.println("==================== updateReview");
        System.out.println(reviewDto.getContents());
        System.out.println("==================== updateReview");
        Reservation reservation = reservationRepository.findByResId(reviewDto.getReservationId());

        reservation.updateReview(reviewDto);
    }


    public Page<Reservation> getReview(Long hosNo, Pageable pageable) {
        Hospital hospital = hospitalService.findByHosNoo(hosNo);

        Page<Reservation> reservations = reservationRepository.findByHospital(hospital, pageable);
        return reservations;
    }
}
