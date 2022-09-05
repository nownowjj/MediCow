package com.medicow.repository.inter;

import com.medicow.model.entity.Hospital;
import com.medicow.model.entity.HospitalImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HospitalImgRepository extends JpaRepository<HospitalImg, Long> {

    List<HospitalImg>  findByHospitalOrderByIdAsc(Hospital hosNo);

    @Query(" select max(h.id) from HospitalImg h")
    Long findByMaxId(HospitalImg sdf);
    // 대표 이미지는 repimgYn 매개변수에 "Y"가 입력되어야 합니다.
    @Query(" select hi from HospitalImg hi where hi.hospital=:hosNo and hi.repImgYn='Y' ")
    HospitalImg findByHospitalAndRepImgYn(@Param(value="hosNo") Hospital hosNo);

    List<HospitalImg> findByHospital(Hospital hospital);
}
