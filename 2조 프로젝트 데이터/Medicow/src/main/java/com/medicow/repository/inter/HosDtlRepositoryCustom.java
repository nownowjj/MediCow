package com.medicow.repository.inter;

import com.medicow.model.dto.HosDtlSearchDto;
import com.medicow.model.entity.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HosDtlRepositoryCustom {
    // 메인 페이지에서 보여줄 공공데이터 병원 리스트를 구해 줍니다.
    Page<Hospital> getMainHosDtlPage(HosDtlSearchDto hosDtlSearchDto, Pageable pageable);
}
