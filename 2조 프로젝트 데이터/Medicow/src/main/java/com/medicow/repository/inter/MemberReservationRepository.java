package com.medicow.repository.inter;

import com.medicow.model.entity.Hospital;
import com.medicow.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberReservationRepository extends JpaRepository<Reservation,Long>, MemberReservationRepositoryCustom {
    @Query(value="select h from Hospital h where h.hosId = :hosId")
    public Hospital findByHosId(@Param("hosId")String hosId);

}
