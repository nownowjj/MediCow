package com.medicow.repository.inter;

import com.medicow.model.constant.ContactStatus;
import com.medicow.model.entity.Contact;
import com.medicow.model.entity.Hospital;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ContactRepository extends JpaRepository<Contact, Long>, QuerydslPredicateExecutor<Contact>, ContactRepositoryCustom {

    @Query(value=" select c from Contact c where c.hospital = :hospital order By c.regTime desc ")
    List<Contact> findContactAllByHosNo(@Param("hospital") Hospital hospital, Pageable pageable);
    @Query(value=" select count(c) from Contact c  where c.hospital = :hospital order By c.regTime desc ")
    Long countByHosNo(@Param("hospital") Hospital hospital);

    // 제목으로 검색
    @Query(value =" select c from Contact c join Hospital h on c.hospital = h.hosNo where  c.subject like %:subject% and c.hospital = :hospital order By c.regTime desc ")
    List<Contact> findContactBySubject(@Param("subject") String subject, @Param("hospital") Hospital hospital, Pageable pageable);

    @Query(value=" select count(c) from Contact c where c.subject like %:subject% and c.hospital = :hospital ")
    Long countBySubject(@Param("subject") String subject, @Param("hospital") Hospital hospital);

    // 작성 내용으로 검색
    @Query(value=" select c from Contact c where c.content like %:content% and c.hospital = :hospital ")
    List<Contact> findContactByContent(@Param("content") String content, @Param("hospital") Hospital hospital, Pageable pageable);

    @Query(value=" select count(c) from Contact c where c.content like %:content% and hospital = :hospital ")
    Long countByContent(@Param("content") String content, @Param("hospital") Hospital hospital);

    // 답변여부로 내용으로 검색
    @Query(value=" select c from Contact c where c.contactStatus =:contactStatus and c.hospital = :hospital ")
    List<Contact> findContactByAnswer(@Param("contactStatus") ContactStatus contactStatus, @Param("hospital") Hospital hospital, Pageable pageable);

    @Query(value=" select count(c) from Contact c where c.contactStatus =:contactStatus and hospital = :hospital ")
    Long countByAsnwer(@Param("contactStatus") ContactStatus contactStatus, @Param("hospital") Hospital hospital);


    // 게시글 Dtl 을 위한 정보 불러오기
    @Query(value=" select c from Contact c where c.id=:id")
    Contact findContact(@Param("id") Long id);



    // 게시글 병원측 수정
    @Modifying
    @Query (value=" update Contact c set c.subject=:subject, c.content=:content where c.id=:id ")
    Integer contactUpdate(@Param("id") Long id,@Param("subject") String subject,@Param("content") String content);

    @Modifying
    @Query (value=" delete from Contact c where c.id=:id ")
    Integer contactDelete(@Param("id") Long id);

//    @Query(value=" select top 3 (c) from Contact where c.hospital =:hospital order By c.regTime desc ")

    List<Contact> findTop3ByHospitalOrderByRegTimeDesc(Hospital hospital);
}
