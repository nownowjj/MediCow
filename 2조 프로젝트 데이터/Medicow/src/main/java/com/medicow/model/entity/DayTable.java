package com.medicow.model.entity;

import com.medicow.model.constant.DayStatus;
import com.medicow.model.constant.WorkStatus;
import com.medicow.model.dto.DayTableDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity @Table(name="day_table")
@Getter @Setter
public class DayTable extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="day_time_id")
    private Long id; // 자동으로 생성되는 숫자형태의 아이디

    @Column(name="start_date_time")
    private String startDateTime; // 시간

    @Column(name="end_date_time")
    private String endDateTime; // 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="hos_no")
    private Hospital hospital; // 병원 외부키(각 병원의 아이디가 외부키가 됨)

    @Enumerated(EnumType.STRING)
    private DayStatus dayStatus; // 요일을 구분하는 Enum(MON,TUE,WED,WHU,FRI,SAT,SUN)

    @Enumerated(EnumType.STRING)
    private WorkStatus workStatus;

    public static DayTable createDayTable(DayTableDto dayTableDto,Hospital hospital){
        DayTable dayTable = new DayTable();
        switch (dayTableDto.getDayStatus()){
            case "월요일": dayTable.setDayStatus(DayStatus.MON);
                break;
            case "화요일": dayTable.setDayStatus(DayStatus.TUE);
                break;
            case "수요일": dayTable.setDayStatus(DayStatus.WED);
                break;
            case "목요일": dayTable.setDayStatus(DayStatus.THU);
                break;
            case "금요일": dayTable.setDayStatus(DayStatus.FRI);
                break;
            case "토요일": dayTable.setDayStatus(DayStatus.SAT);
                break;
            case "일요일": dayTable.setDayStatus(DayStatus.SUN);
                break;
            case "점심시간": dayTable.setDayStatus(DayStatus.LUNCH);
                break;
        }
        dayTable.setHospital(hospital);
        
        switch(dayTableDto.getWorkStatus()){
            case "휴무일": dayTable.setWorkStatus(WorkStatus.REST);
                break;

            case "진료일": dayTable.setWorkStatus(WorkStatus.WORK);
                break;
        }
        // 휴무날에는 시간이 입력되어도 보는 창에는 표시 되지 않고 저장 되지도 않음
        if(dayTable.workStatus==WorkStatus.WORK){
            dayTable.setStartDateTime(dayTableDto.getStartDateTime());
            dayTable.setEndDateTime(dayTableDto.getEndDateTime());    
        }

        return dayTable;
    }

}
