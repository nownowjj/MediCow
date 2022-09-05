package com.medicow.service;

import com.medicow.model.constant.DayStatus;
import com.medicow.model.constant.WorkStatus;
import com.medicow.model.dto.DayTableDto;
import com.medicow.model.dto.MainDto;
import com.medicow.model.entity.DayTable;
import com.medicow.model.entity.Hospital;
import com.medicow.repository.inter.HospitalInfoRepository;
import com.medicow.repository.inter.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HospitalInfoService{
    private final HospitalInfoRepository hospitalInfoRepository;
    private final HospitalRepository hospitalRepository;
//    private final HospitalImgRepository hospitalImgRepository;
    private final HospitalImgService hospitalImgService;

    // DayTable를 데이터 베이스에 저장하는 메소드
    public DayTable saveDayTable(DayTable dayTable){
        return hospitalInfoRepository.save(dayTable);
    }


    public List<DayTableDto> searchMyTime(String hosId){
        Long hosNo = hospitalRepository.findHosNoByHosId(hosId);
        // 운영시간관련 Entity /Dto 리스트 생성
        int count = hospitalInfoRepository.countByHosNo(hosNo);
        System.out.println(count+"만큼 있음");

        if(count ==0){
            return null;
        }
        List<DayTableDto> dayTableDtoList = new ArrayList<DayTableDto>();
        List<DayTable> dayTableList = new ArrayList<DayTable>();

        // Entity List에 Repository를 이용한 리스트를 담음
        dayTableList = hospitalInfoRepository.findByHosNo(hosNo);

        if(dayTableList==null){
            //dayTableDtoList가 null값을 가질 때
            throw new IllegalStateException("입력하신 시간이 없습니다.");
        }

        // Dto 리스트에 Entity리스트를 담음
        for(DayTable dayTable : dayTableList){
            // Dto 신규 객체 생성
            DayTableDto dayTableDto = DayTableDto.createDayTabelDto(dayTable);

            // DtoList에 추가
            dayTableDtoList.add(dayTableDto);
        }

        // Dto 리스트를 리턴
        return dayTableDtoList;
    }
    public void updateMyTable(String startDateTime, String endDateTime, WorkStatus workStatus, Long id){
        System.out.println(id);
        hospitalInfoRepository.updateDayTable(startDateTime,endDateTime,workStatus,id);
    }

    public void findError(){
        throw new IllegalStateException("시간설정을 다시해주세요.");
    }

    public MainDto searchTodayTime(Hospital hospital, DayStatus dayStatus) {
        DayTable dayTable = hospitalInfoRepository.searchTodayTime(hospital,dayStatus);
        MainDto mainDto = new MainDto();

        if(dayTable==null) {
            mainDto.setStartDateTime(" 현재 운영시간이 입력되지 않았습니다.");
            mainDto.setEndDateTime("운영시간을 입력해주세요.");
        }else{
            mainDto.setStartDateTime(dayTable.getStartDateTime());
            mainDto.setEndDateTime(dayTable.getEndDateTime());
        }

        return mainDto;
    }
}
