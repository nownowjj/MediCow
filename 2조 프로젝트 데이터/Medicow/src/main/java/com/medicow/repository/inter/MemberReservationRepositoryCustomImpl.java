package com.medicow.repository.inter;

import com.medicow.model.dto.ReservationListDto;
import com.medicow.model.entity.QReservation;
import com.medicow.model.entity.Reservation;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class MemberReservationRepositoryCustomImpl implements MemberReservationRepositoryCustom {
    private JPAQueryFactory queryFactory;
    public MemberReservationRepositoryCustomImpl(EntityManager em){ this.queryFactory = new JPAQueryFactory(em);}

    @Override
    public Page<Reservation> getReservation(ReservationListDto reservationListDto, Pageable pageable) {
        System.out.println("======================== NoticeBoardRepositoryCustomImpl");
        System.out.println();

        QueryResults<Reservation> results = this.queryFactory
                .selectFrom(QReservation.reservation)
                .where(searchSubjectLike(reservationListDto.getSearchSubject()),
                        searchEmailLike(reservationListDto.getSearchEmail()))
                .orderBy(QReservation.reservation.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Reservation> content = results.getResults();
        System.out.println("===================== content.size() ");
        System.out.println("content.size() : " + content.size());

        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression searchSubjectLike(String searchSubject){
        System.out.println("===================== searchSubject");
        System.out.println(" :::::: " + searchSubject);
        if(StringUtils.equals("", searchSubject) || StringUtils.equals(null, searchSubject)){
            searchSubject = "";
        }
        return QReservation.reservation.hospital.hosName.like("%" + searchSubject + "%");
    }

    private BooleanExpression searchEmailLike(String searchEmail){
        System.out.println("===================== searchEmail");
        System.out.println(" :::::: " + searchEmail);
        return QReservation.reservation.createdBy.like("%" + searchEmail + "%");
    }

}
