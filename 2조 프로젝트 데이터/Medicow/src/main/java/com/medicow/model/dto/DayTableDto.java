package com.medicow.model.dto;

import com.medicow.model.constant.DayStatus;
import com.medicow.model.constant.WorkStatus;
import com.medicow.model.entity.DayTable;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DayTableDto {

    private Long id; // 각 시간별 고유 구분 번호

    private String hosId; // 병원 -> principal에서 가져올 것임

    private String startDateTime; // 시간 입력 select-option으로 30분 단위로 끊을 예정

    private String endDateTime; // 시간 입력 select-option으로 30분 단위로 끊을 예정

    private String dayStatus; // 요일 구분 select-option으로 월,화,수,목,금,토,일,점심

    private String workStatus; // 시작/끝 구분 select-option으로 영업시작시간/ 영업종료시작 구분할 예정

    public static DayTableDto createDayTabelDto(DayTable dayTable){
        DayTableDto dayTableDto= new DayTableDto();
        dayTableDto.setId(dayTable.getId());
        dayTableDto.setHosId(dayTable.getHospital().getHosId());

        dayTableDto.setStartDateTime(dayTable.getStartDateTime());
        dayTableDto.setEndDateTime(dayTable.getEndDateTime());

        DayStatus date=dayTable.getDayStatus();
        WorkStatus workStatus = dayTable.getWorkStatus();
        switch(workStatus){
            case REST -> dayTableDto.setWorkStatus("휴무일");
            case WORK -> dayTableDto.setWorkStatus("진료일");
        }

        switch(date){
            case MON -> dayTableDto.setDayStatus("월요일");
            case TUE -> dayTableDto.setDayStatus("화요일");
            case WED -> dayTableDto.setDayStatus("수요일");
            case THU -> dayTableDto.setDayStatus("목요일");
            case FRI -> dayTableDto.setDayStatus("금요일");
            case SAT -> dayTableDto.setDayStatus("토요일");
            case SUN -> dayTableDto.setDayStatus("일요일");
            case LUNCH -> dayTableDto.setDayStatus("점심시간");
        }
        return dayTableDto;
    }
    public DayTableDto(){}

    public DayTableDto(String date){
        this.dayStatus=date;
    }

}
