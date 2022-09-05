package com.medicow.repository.inter;

import com.medicow.model.dto.NoticeSearchDto;
import com.medicow.model.entity.Notice;
import com.medicow.model.entity.QNotice;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

@Transactional
public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {

    private JPAQueryFactory queryFactory ;

    public NoticeRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Notice> getMainNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable) {
        QueryResults<Notice> results = this.queryFactory
                .selectFrom(QNotice.notice)
                .where(
                        searchByLike(noticeSearchDto.getSearchBy(), noticeSearchDto.getSearchQuery()))
                .orderBy(QNotice.notice.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults() ;

        List<Notice> content = results.getResults() ;
        long total = results.getTotal() ;

        // PageImpl 클래스는 Page 인터페이스의 구현체입니다.
        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if(StringUtils.equals("subject", searchBy)){
            return QNotice.notice.subject.like("%" + searchQuery + "%") ;

        }else if(StringUtils.equals("content", searchBy)){
            return QNotice.notice.content.like("%" + searchQuery + "%") ;
        }
        return null ;
    }
}
