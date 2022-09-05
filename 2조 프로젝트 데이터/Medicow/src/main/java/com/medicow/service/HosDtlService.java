package com.medicow.service;

import com.medicow.model.dto.HosDtlSearchDto;
import com.medicow.model.entity.Hospital;
import com.medicow.repository.inter.HosDtlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class HosDtlService {
    private final HosDtlRepository hosDtlRepository;

    @Transactional(readOnly = true)
    public Page<Hospital> getHosDtlPage(HosDtlSearchDto hosDtlSearchDto, Pageable pageable){
        return hosDtlRepository.getMainHosDtlPage(hosDtlSearchDto, pageable);
    }


}
