package com.medicow.controller;

import com.medicow.model.dto.NoticeFormDto;
import com.medicow.model.dto.NoticeSearchDto;
import com.medicow.model.entity.Notice;
import com.medicow.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class HospitalNoticeController {
    // 공지사항을 위한 NoticeService 주입
    final private NoticeService noticeService;

    @GetMapping(value = {"hospital/notice", "/hospital/notice/{page}"})
    public String notice(NoticeSearchDto noticeSearchDto,
                         @PathVariable("page") Optional<Integer> page,
                         Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<Notice> datas = noticeService.getNoticePage(noticeSearchDto, pageable);

        model.addAttribute("datas", datas);
        model.addAttribute("noticeSearchDto", noticeSearchDto); // for 검색 조건 보존
        model.addAttribute("maxPage", 5); // 하단에 보여줄 페이지 번호의 최대 개수

        return "hospital/notice/notice" ;
    }

    @GetMapping("/hospital/noticeDtl/{Id}")
    public String noticeDtl(Model model, @PathVariable("Id") Long noticeId){
        NoticeFormDto noticeFormDto = noticeService.getNoticeDtl(noticeId) ;
        model.addAttribute("notice", noticeFormDto) ;
        return "hospital/notice/noticeDtl" ;
    }



}
