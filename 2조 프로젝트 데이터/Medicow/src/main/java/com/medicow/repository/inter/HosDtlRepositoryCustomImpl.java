package com.medicow.repository.inter;

import com.medicow.model.dto.HosDtlSearchDto;
import com.medicow.model.entity.Hospital;
import com.medicow.model.entity.QHospital;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

public class HosDtlRepositoryCustomImpl implements HosDtlRepositoryCustom {

    // 쿼리를 build 시켜 주는 클래스로써, EntityManager를 매개 변수로 넣어준다.
    private JPAQueryFactory queryFactory;

    public HosDtlRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Hospital> getMainHosDtlPage(HosDtlSearchDto hosDtlSearchDto, Pageable pageable) {
        QueryResults<Hospital> results = this.queryFactory
                .selectFrom(QHospital.hospital)
                .orderBy(QHospital.hospital.hosId.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        // orderBy를 수정하면 데이터를 어떤 순서대로 보여줄 수 있는지 처리가능합니다.

        List<Hospital> content = results.getResults();
        long total = results.getTotal();

        // PageImpl 클래스는 Page 인터페이스의 구현체입니다.
        return new PageImpl<>(content, pageable, total);
    }
}
