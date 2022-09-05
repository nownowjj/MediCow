package com.medicow.repository.inter;

import com.medicow.model.constant.ActiveStatus;
import com.medicow.model.constant.DeleteStatus;
import com.medicow.model.dto.ReviewSearchDto;
import com.medicow.model.entity.QReview;
import com.medicow.model.entity.Review;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class AdminReviewRepositoryCustomImpl implements AdminReviewRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public AdminReviewRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Review> getMainReviewPage(ReviewSearchDto reviewSearchDto, Pageable pageable) {
        QueryResults<Review> results = this.queryFactory
                .selectFrom(QReview.review)
                .where(
                        QReview.review.activeStatus.eq(ActiveStatus.ABLE).and(QReview.review.deleteStatus.eq(DeleteStatus.YES)).and(searchByLike(reviewSearchDto.getSearchBy(), reviewSearchDto.getSearchQuery()))
                        )
                .orderBy(QReview.review.reviewId.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        // orderBy를 수정하면 데이터를 어떤 순서대로 보여줄 수 있는지 처리가능합니다.

        List<Review> content = results.getResults();
        long total = results.getTotal();

        // PageImpl 클래스는 Page 인터페이스의 구현체입니다.
        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if(StringUtils.equals("subject", searchBy)){
            return QReview.review.reviewId.like("%" + searchQuery + "%") ;

        }else if(StringUtils.equals("content", searchBy)){
            return QReview.review.content.like("%" + searchQuery + "%") ;
        }
        return null ;
    }
}
