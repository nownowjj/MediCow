package com.medicow.repository.inter;

import com.medicow.model.entity.Doctor;
import com.medicow.model.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HospitalDoctorRepository extends JpaRepository<Doctor, Long> {

//    @Query(" select d from Doctor d join Hospital h on d.hospital = h.hosNo where h.hosNo =:hosNo and d.activeStatus='ABLE'")
    @Query(" select d from Doctor d where d.hospital = :hosId and d.activeStatus='ABLE'")
    List<Doctor> findByHosId(@Param(value="hosId") Hospital hosId);

    @Query(" select d from Doctor d where d.docId = :docId")
    Doctor findByDocId(Long docId);

    @Query(" select max(d.docId) from Doctor d where d.hospital=:hosId")
    Long findByMaxNo(Hospital hosId);



}
