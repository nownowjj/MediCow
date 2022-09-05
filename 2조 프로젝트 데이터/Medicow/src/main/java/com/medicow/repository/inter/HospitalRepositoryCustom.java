package com.medicow.repository.inter;

import com.medicow.model.dto.HospitalFindDto;
import com.medicow.model.entity.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HospitalRepositoryCustom {
    Page<Hospital> getHospList(HospitalFindDto hospitalFindDto, Pageable pageable);
}
