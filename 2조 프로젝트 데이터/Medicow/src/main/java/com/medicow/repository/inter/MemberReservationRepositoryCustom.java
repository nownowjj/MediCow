package com.medicow.repository.inter;

import com.medicow.model.dto.ReservationListDto;
import com.medicow.model.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberReservationRepositoryCustom {
    Page<Reservation> getReservation(ReservationListDto reservationListDto, Pageable pageable);
}
