package com.medicow.repository.inter;


import com.medicow.model.constant.DayStatus;
import com.medicow.model.constant.WorkStatus;
import com.medicow.model.entity.DayTable;
import com.medicow.model.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HospitalInfoRepository extends JpaRepository<DayTable, Long> {

    @Query(value = " select count(d) from DayTable d join Hospital h on d.hospital = h.hosNo where h.hosNo = :hosNo ")
    public int countByHosNo(@Param("hosNo") Long hosNo);

    @Query(value="select h.hosId from Hospital h where h.hosId = :hosId")
    public Hospital findById(@Param("hosId") String hosId);

    // 접속중인 병원의 진료시간 테이블을 가져오는 쿼리문
    @Query(value = " select d from DayTable d join Hospital h on d.hospital = h.hosNo where h.hosNo = :hosNo ")
    public List<DayTable> findByHosNo(@Param("hosNo") Long hosNo);

    @Modifying
    @Query(value ="update DayTable d set d.startDateTime = :startDateTime, d.endDateTime = :endDateTime, d.workStatus = :workStatus where d.id = :id" )
    public Integer updateDayTable(@Param("startDateTime") String startDateTime, @Param("endDateTime") String endDateTime, @Param("workStatus") WorkStatus workStatus, @Param("id") Long id);

    @Query(value=" select d from DayTable d where d.hospital=:hospital and d.dayStatus=:dayStatus")
    DayTable searchTodayTime(@Param("hospital") Hospital hospital,@Param("dayStatus") DayStatus dayStatus);
}
