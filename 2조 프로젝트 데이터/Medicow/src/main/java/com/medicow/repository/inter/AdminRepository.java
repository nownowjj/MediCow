package com.medicow.repository.inter;

import com.medicow.model.entity.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Long>{
    // 이메일을 이용하여 어드민 검색, 가입시 중복 체크
    Admin findByEmail(String email) ;

    // 쿼리메소드를 테스트합니다. :email 이부분에 값은 @Param을 이용해 담고 ""안에 이름이 같아야합니다.
    @Query(" select a from Admin a where a.email = :email " +
            "   ")
    List<Admin> findByAddress(@Param("email") String email) ;

    // 쿼리메소드 테스트입니다. 이메일형태를 가진 어드민을 전부 조회합니다.
    @Query(" select a from Admin a where a.email like :param ")
    List<Admin> findByAllAddress(@Param("param") String address);

    // 쿼리메소드 테스트입니다. 모든 데이터 컬럼을 추출합니다.
//    Page<Admin> findAll(PageRequest pageRequest);

    // 페이징 처리 테스트입니다.
    public Page<Admin> findById(Long no, Pageable pagebale);

    // 메인페이지 병원 카운터
    @Query(" select count(h) from Hospital h")
    public int getHospitalCnt();

    // 메인페이지 멤버 카운터
    @Query(" select count(m) from Member m")
    public int getMemberCnt();

}
