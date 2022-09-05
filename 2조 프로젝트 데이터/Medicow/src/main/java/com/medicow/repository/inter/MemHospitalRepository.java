package com.medicow.repository.inter;

import com.medicow.model.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemHospitalRepository extends JpaRepository<Hospital, Long>, HospitalRepositoryCustom {

}
