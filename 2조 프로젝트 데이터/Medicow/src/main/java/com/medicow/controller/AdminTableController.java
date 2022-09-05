package com.medicow.controller;

import com.medicow.model.entity.Hospital;
import com.medicow.model.entity.Member;
import com.medicow.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
public class AdminTableController {

    private final AdminService adminService;

    // 병원 테이블로 이동합니다.
    @GetMapping(value = "/hospital")
    public String adminHospitalTable(Model model) {
        List<Hospital> datas = adminService.getRegisteredHospitalPage();

        model.addAttribute("datas", datas);

        return "/admin/hospitalTable";
    }

    // 멤버 테이블로 이동합니다.
    @GetMapping(value = "/member")
    public String adminMemberTable(Model model) {
        List<Member> datas = adminService.getAllMember();

        model.addAttribute("datas", datas);

        return "/admin/memberTable";
    }

    // 차트 테이블로 이동합니다.
    @GetMapping(value = "/chart")
    public String adminChartTable(Model model){
        return "/admin/charts";
    }

}
