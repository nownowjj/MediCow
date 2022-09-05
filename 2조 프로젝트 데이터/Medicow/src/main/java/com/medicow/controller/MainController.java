package com.medicow.controller;

import com.medicow.model.dto.HospitalFindDto;
import com.medicow.model.dto.NoticeSearchDto;
import com.medicow.model.entity.Member;
import com.medicow.model.entity.NoticeBoard;
import com.medicow.repository.inter.MemberRepository;
import com.medicow.repository.inter.NoticeBoardRepository;
import com.medicow.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final MemberRepository memberRepository;
    private final NoticeBoardRepository noticeBoardRepository;
    private final AdminService adminService;

    @GetMapping(value = "/user/")
    public String main(NoticeSearchDto noticeSearchDto, Model model){
        Pageable pageable = PageRequest.of(0,5);
        Page<NoticeBoard> noticeBoards = noticeBoardRepository.getNoticePage(noticeSearchDto, pageable);
        model.addAttribute("noticeBoards", noticeBoards);

        Member member   = new Member();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("================== email : " + email);
        if(email == "anonymousUser" ){
            member.setAddress("미가입 회원 입니다.");
        }else{
            member = memberRepository.findByEmail(email);
        }

        /* 메인페이지 병원수, 회원수*/
        int hosCnt = adminService.getHospitalCnt();
        int memCnt = adminService.getMemberCnt();

        model.addAttribute("hosCnt",hosCnt);
        model.addAttribute("memCnt",memCnt);

        HospitalFindDto hospitalFindDto = new HospitalFindDto();
        hospitalFindDto.setUser_addr(member.getAddress());
        hospitalFindDto.setUser_x(member.getUser_x());
        hospitalFindDto.setUser_y(member.getUser_y());
        model.addAttribute("hospitalFindDto",hospitalFindDto);

        return "/main";
    }

    @GetMapping(value = "/user/health")
    public String health(){
        return "/health/healthInformation";
    }

    @GetMapping(value = "/user/privacy/privacypolicy")
    public String privacypolicy(){
        return "privacy/privacypolicy";
    }

    @GetMapping(value = "/user/privacy/termsofservice")
    public String Termsofservice(){
        return "privacy/termsofservice";
    }

}
