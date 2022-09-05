package com.medicow.repository.inter;

import com.medicow.model.dto.NoticeSearchDto;
import com.medicow.model.entity.NoticeBoard;
import com.medicow.model.entity.QNoticeBoard;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class NoticeBoardRepositoryCustomImpl implements NoticeBoardRepositoryCustom {

    private JPAQueryFactory queryFactory;
    public NoticeBoardRepositoryCustomImpl(EntityManager em){ this.queryFactory = new JPAQueryFactory(em);}

    @Override
    public Page<NoticeBoard> getNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable) {
        System.out.println("======================== NoticeBoardRepositoryCustomImpl");
        System.out.println(searchSubjectLike(noticeSearchDto.getSearchSubject()));

        QueryResults<NoticeBoard> results = this.queryFactory
                .selectFrom(QNoticeBoard.noticeBoard)
                .where(searchSubjectLike(noticeSearchDto.getSearchSubject()))
                .orderBy(QNoticeBoard.noticeBoard.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<NoticeBoard> content = results.getResults();
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

        return QNoticeBoard.noticeBoard.subject.like("%" + searchSubject + "%");
    }

}
