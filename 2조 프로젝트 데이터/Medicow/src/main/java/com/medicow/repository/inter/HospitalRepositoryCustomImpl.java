package com.medicow.repository.inter;

import com.medicow.model.dto.HospitalFindDto;
import com.medicow.model.entity.Hospital;
import com.medicow.model.entity.QHospital;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class HospitalRepositoryCustomImpl implements HospitalRepositoryCustom {
    private JPAQueryFactory queryFactory;
    public HospitalRepositoryCustomImpl(EntityManager em){ this.queryFactory = new JPAQueryFactory(em);}

    @Override
    public Page<Hospital> getHospList(HospitalFindDto hospitalFindDto, Pageable pageable) {
        System.out.println("====================== HospitalRepositoryCustomImpl");
        System.out.println(searchHosNameLike(hospitalFindDto.getHospname()));

        QueryResults<Hospital> results = this.queryFactory
                .selectFrom(QHospital.hospital)
                .where(searchHosNameLike(hospitalFindDto.getHospname()).or(searchHosSubjectLike(hospitalFindDto.getHospname())))
                .orderBy(QHospital.hospital.hosName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Hospital> content = results.getResults();

        System.out.println("===================== content.size() ");
        System.out.println("content.size() : " + content.size());
        /*System.out.println(content.get(1));*/
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression searchHosNameLike(String hosName){
        if(StringUtils.equals("", hosName) || StringUtils.equals(null, hosName)){
            hosName = "";
        }
        return QHospital.hospital.hosName.like("%" + hosName + "%");
    }

    private BooleanExpression searchHosSubjectLike(String hosSubject){
        if(StringUtils.equals("", hosSubject) || StringUtils.equals(null, hosSubject)){
            hosSubject = "";
        }
        return QHospital.hospital.hosSubject.like("%" + hosSubject + "%");
    }
}
