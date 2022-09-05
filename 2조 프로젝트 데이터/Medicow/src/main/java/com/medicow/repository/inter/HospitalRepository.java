package com.medicow.repository.inter;

import com.medicow.model.constant.RegisterStatus;
import com.medicow.model.entity.Hospital;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HospitalRepository extends JpaRepository<Hospital, Long> , QuerydslPredicateExecutor<Hospital>, HospitalRepositoryCustom  {

    Hospital findByHosId(String id);

    Hospital findByHosNo(Long hosNo);

    @Query(" select max(h.hosNo) from Hospital h")
    Long findByHosNo(Hospital sdf);

    @Query(value=" select h from Hospital h where h.hosName like %:hName% and h.activeStatus = null " +
            " order by h.hosNo asc ")
    List<Hospital> findHospitalBy(@Param("hName") String hName, Pageable pageable);

    @Query(value=" select count(h) from Hospital h where h.hosName like %:hName% and h.activeStatus = null ")
    Long countHospital(@Param("hName") String hName);

    @Modifying
    @Query(value= " update Hospital h set h.hosId = :hosId, h.hosPassword = :hosPassword, h.hosDAddress = :hosDAddress, h.hosCeo = :hosCeo, h.hosCeoEmail = :hosCeoEmail, h.hosSubject = :hosSubject ,h.hosRegister = :hosRegister, h.hosName= :hosName, h.hosAddress =:hosAddress, h.hosPostNum =:hosPostNum, h.activeStatus= 'ABLE' where h.hosNo = :hosNo")
    Integer updateHospital(@Param("hosNo") Long hosNo, @Param("hosId") String hosId, @Param("hosPassword") String hosPassword, @Param("hosDAddress") String hosDAddress, @Param("hosCeo") String hosCeo, @Param("hosCeoEmail") String hosCeoEmail, @Param("hosSubject") String hosSubject, @Param("hosRegister") RegisterStatus hosRegister,@Param("hosName")String hosName,@Param("hosAddress")String hosAddress,@Param("hosPostNum")Integer hosPostNum) ;


    @Query(value = " select h.hosNo from Hospital h where h.hosId = :hosId ")
    Long findHosNoByHosId(@Param("hosId") String hosId);


}
