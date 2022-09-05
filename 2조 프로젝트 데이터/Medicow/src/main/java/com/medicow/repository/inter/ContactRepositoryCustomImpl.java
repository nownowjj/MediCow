package com.medicow.repository.inter;

import com.medicow.model.dto.ContactSearchDto;
import com.medicow.model.entity.Contact;
import com.medicow.model.entity.QContact;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class ContactRepositoryCustomImpl implements ContactRepositoryCustom{
    private JPAQueryFactory queryFactory;

    public ContactRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Contact> getMainContactPage(ContactSearchDto contactSearchDto, Pageable pageable) {
        QueryResults<Contact> results = this.queryFactory
                .selectFrom(QContact.contact)
                .where(
                        searchByLike(contactSearchDto.getSearchBy(), contactSearchDto.getSearchQuery()))
                .orderBy(QContact.contact.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults() ;

        List<Contact> content = results.getResults() ;
        long total = results.getTotal() ;

        // PageImpl 클래스는 Page 인터페이스의 구현체입니다.
        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if(StringUtils.equals("subject", searchBy)){
            return QContact.contact.subject.like("%" + searchQuery + "%") ;

        }else if(StringUtils.equals("content", searchBy)){
            return QContact.contact.content.like("%" + searchQuery + "%") ;
        }
        return null ;
    }
}
