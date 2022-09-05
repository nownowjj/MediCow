package com.medicow.repository.inter;

import com.medicow.model.constant.ActiveStatus;
import com.medicow.model.constant.DeleteStatus;
import com.medicow.model.entity.Hospital;
import com.medicow.model.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HospitalReviewRepository extends JpaRepository<Review, Long> {
    // 병원 아이디를 이용하여 해당 병원에 대한 모든 리뷰를 페이징 처리해서 돌려주는 메소드
    @Query(value=" select v from Review v where v.reservation in ( select r from Reservation r where r.hospital = :hospital ) and v.deleteStatus = :deleteStatus and v.activeStatus = :activeStatus order By v.regTime desc")
    public List<Review> findReviewByHospital(@Param("hospital") Hospital hospital, @Param("deleteStatus") DeleteStatus deleteStatus, @Param("activeStatus") ActiveStatus activeStatus, Pageable pageable);

    // 병원 아이디를 통해서 해당 병원에 대한 리뷰 수를 셀렉하는 메소드
    @Query(value=" select count(v) from Review v where v.reservation in ( select r from Reservation r where r.hospital = :hospital ) and v.deleteStatus = :deleteStatus and v.activeStatus = :activeStatus ")
    Integer countReviewByHospital(@Param("hospital") Hospital hospital, @Param("deleteStatus") DeleteStatus deleteStatus, @Param("activeStatus") ActiveStatus activeStatus);

    @Modifying
    @Query(value=" update Review v set v.deleteStatus = :deleteStatus where v.reviewId = :reviewId ")
    void deleteRequest(@Param("deleteStatus")DeleteStatus deleteStatus, @Param("reviewId") Long reviewId);

    @Query(value=" select count(v) from Review v join Reservation r on v.reservation = r.id where r.hospital = :hospital and v.deleteStatus = :deleteStatus ")
    Long curReviewCount(@Param("hospital") Hospital hospital,@Param(value="deleteStatus") DeleteStatus deleteStatus);

    @Query(value=" select v.point from Review v join Reservation r on v.reservation = r.id where r.hospital=:hospital and v.deleteStatus = :deleteStatus ")
List<Double> curReviewTotal(@Param("hospital") Hospital hospital,@Param(value="deleteStatus") DeleteStatus deleteStatus);
//    @Query(value=" select v.point from Review v where v.id in (select r.id from Reservation r where r.hospital= :hospital) and v.deleteStatus = :deleteStatus ")
//   List<Double> curReviewTotal(@Param("hospital") Hospital hospital,@Param(value="deleteStatus") DeleteStatus deleteStatus);

}
