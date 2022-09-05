package com.medicow.controller;

import com.medicow.model.dto.HosDtlSearchDto;
import com.medicow.model.entity.Hospital;
import com.medicow.service.AdminService;
import com.medicow.service.HosDtlService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class AdminMainController {

    final private HosDtlService hosDtlService;
    final private AdminService adminService;


    // 테이블 size에 데이터 몇개 보여줄지 숫자 입력하면 됩니다.
    final static int PAGE_SIZE = 500;

    // 메인 페이지로 이동하는 GetMapping입니다.
    @GetMapping(value = {"/admin/main", "/admin/main/{page}"})
    public String adminMain(HosDtlSearchDto hosDtlSearchDto,
                            @PathVariable("page") Optional<Integer> page,
                            Model model){
        // PAGE_SIZE 여기에다 입력하면 됩니다. static으로 있습니다.
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, PAGE_SIZE);
        Page<Hospital> datas = hosDtlService.getHosDtlPage(hosDtlSearchDto, pageable);

        // List자료형으로 받아와서 main에 데이터를 표현할 수도 있는데
        // 데이터를 전부 가져오려다 보니 로딩속도가 느립니다.
        // List 자료형으로 가져오려면 main ${datas.getContent()}를 그냥
        // status: ${datas}로 써주면 됩니다.
        // List<Hospital> datas = adminService.getAll();

        model.addAttribute("datas", datas);

        // bar chart의 데이터 리스트 가져오는 구간.
        // regTime에 따른 데이터 카운트
        // 월별로 시작 일자와 끝일자를 입력해주면 해당 범위만큼 count가 생성됩니다.

        long barChartValue = 0L;
        List<Long> barChartValues = new ArrayList<Long>();

        Long barChartValue1 = adminService.getHospitalCountByRegTime("21-07-01", "21-07-30");
        Long barChartValue2 = adminService.getHospitalCountByRegTime("21-08-01", "21-08-30");
        Long barChartValue3 = adminService.getHospitalCountByRegTime("21-09-01", "21-09-30");
        Long barChartValue4 = adminService.getHospitalCountByRegTime("21-10-01", "21-10-30");
        Long barChartValue5 = adminService.getHospitalCountByRegTime("21-11-01", "21-11-30");
        Long barChartValue6 = adminService.getHospitalCountByRegTime("21-12-01", "21-12-30");
        Long barChartValue7 = adminService.getHospitalCountByRegTime("22-01-01", "22-01-30");
        Long barChartValue8 = adminService.getHospitalCountByRegTime("22-02-01", "22-02-28");
        Long barChartValue9 = adminService.getHospitalCountByRegTime("22-03-01", "22-03-30");
        Long barChartValue10 = adminService.getHospitalCountByRegTime("22-04-01", "22-04-30");
        Long barChartValue11 = adminService.getHospitalCountByRegTime("22-05-01", "22-05-30");
        Long barChartValue12 = adminService.getHospitalCountByRegTime("22-06-01", "22-06-30");

        System.out.println(barChartValue7);
        System.out.println(barChartValue1);

        barChartValues.add(barChartValue1);
        barChartValues.add(barChartValue2);
        barChartValues.add(barChartValue3);
        barChartValues.add(barChartValue4);
        barChartValues.add(barChartValue5);
        barChartValues.add(barChartValue6);
        barChartValues.add(barChartValue7);
        barChartValues.add(barChartValue8);
        barChartValues.add(barChartValue9);
        barChartValues.add(barChartValue10);
        barChartValues.add(barChartValue11);
        barChartValues.add(barChartValue12);

        // 추후 데이터베이스에서 값을 리스트로 가져와 main에 담습니다.
        // 카운팅한 데이터를 List에 담아 main으로 보냅니다.
        model.addAttribute("barChartValues", barChartValues);

        // area-chart의 데이터 리스트 가져오는 구간.
        // regTime에 따른 데이터 카운트
        List<Long> areaChartValues = new ArrayList<Long>();

        Long areaChartValue1 = adminService.getMemberCountByRegTime("01-01", "01-30");
        Long areaChartValue2 = adminService.getMemberCountByRegTime("02-01", "02-28");
        Long areaChartValue3 = adminService.getMemberCountByRegTime("03-01", "03-30");
        Long areaChartValue4 = adminService.getMemberCountByRegTime("04-01", "04-30");
        Long areaChartValue5 = adminService.getMemberCountByRegTime("05-01", "05-30");
        Long areaChartValue6 = adminService.getMemberCountByRegTime("06-01", "06-30");
        Long areaChartValue7 = adminService.getMemberCountByRegTime("07-01", "07-30");

        areaChartValues.add(areaChartValue1);
        areaChartValues.add(areaChartValue2);
        areaChartValues.add(areaChartValue3);
        areaChartValues.add(areaChartValue4);
        areaChartValues.add(areaChartValue5);
        areaChartValues.add(areaChartValue6);
        areaChartValues.add(areaChartValue7);

        model.addAttribute("areaChartValues", areaChartValues);

        return "admin/main";
    }
}
