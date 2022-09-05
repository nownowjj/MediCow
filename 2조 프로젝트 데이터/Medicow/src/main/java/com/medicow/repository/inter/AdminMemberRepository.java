package com.medicow.repository.inter;

import com.medicow.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminMemberRepository  extends JpaRepository<Member, Long> {
    // 관리자 테스트(김근오)
    // 멤버 회원 가입일자를 받아와 월별로 카운트를 계산해 줍니다.
    @Query(value = " select count(m) from Member m where m.regTime >= to_timestamp(:firstDate, 'mm-dd') and m.regTime <= to_timestamp(:secondDate, 'mm-dd') ")
    Long countByRegTime(@Param("firstDate") String firstDate, @Param("secondDate") String secondDate);
}
