package com.medicow.repository.inter;

import com.medicow.model.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminHospitalRepository extends JpaRepository<Hospital, Long> {
    // 관리자 테스트(김근오)
    // 해당 월에 있는 데이터 출력하고 싶으면 메소드 argument에 시작 일자, 끝 일자를 입력해 주면됩니다.
    // hosRegister가 YES이면 병원이 이 웹에 등록된 것이므로 출력합니다.
    // 정책이 바뀌어 가입된 병원만 조회 할 경우  and h.hosRegister = 'YES' 추가해주세요.
    // 해당 쿼리문 맨 뒤에
    @Query(value = " select count(h) from Hospital h where h.regTime >= to_timestamp(:firstDate, 'yy-mm-dd') and h.regTime <= to_timestamp(:secondDate, 'yy-mm-dd') ")
    Long countByRegTime(@Param("firstDate") String firstDate, @Param("secondDate") String secondDate);

    // 관리자 페이지 (김근오)
    // 이 웹사이트에 등록된 병원 테이블만 조회하는 쿼리
    @Query(value = " select h from Hospital h where h.hosRegister = 'YES' ")
    List<Hospital> getRegisteredHospitalPage();
}