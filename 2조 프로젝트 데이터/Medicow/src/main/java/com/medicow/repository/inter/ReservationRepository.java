package com.medicow.repository.inter;

import com.medicow.model.entity.Hospital;
import com.medicow.model.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query(value = "select r from Reservation r where r.id= :resId")
    Reservation findByResId(@Param("resId") Long resId);


    Page<Reservation> findByHospital(Hospital hospital, Pageable pageable);
}
