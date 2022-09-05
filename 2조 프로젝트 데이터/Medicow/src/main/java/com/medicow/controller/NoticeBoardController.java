package com.medicow.controller;

import com.medicow.model.dto.*;
import com.medicow.model.entity.Notice;
import com.medicow.service.MemberService;
import com.medicow.service.NoticeBoardService;
import com.medicow.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/user/boards")
public class NoticeBoardController {
    private final MemberService memberService;
    final private NoticeService noticeService;
    private  final NoticeBoardService noticeBoardService;

    @GetMapping(value = {"/", "/{page}"})
    public String main(NoticeSearchDto noticeSearchDto, @PathVariable("page") Optional<Integer> page, Model model){
        System.out.println("========================= NoticeBoardController main : ");

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);

        Page<Notice> noticeBoards = noticeService.getNoticePage(noticeSearchDto, pageable);

        model.addAttribute("noticeBoards", noticeBoards);
        model.addAttribute("noticeSearchDto", noticeSearchDto);
        model.addAttribute("maxPage", 5);

        return "boards/noticeBoard";
    }

    @GetMapping(value = "/noticeId/{noticeId}")
    public String noticeBoard(@PathVariable("noticeId") Long noticeId, Model model){
        NoticeFormDto noticeFormDto = noticeService.getNoticeDtl(noticeId) ;
        model.addAttribute("notice", noticeFormDto) ;
        return "boards/noticeId";
    }

    @PostMapping(value = "/user/noticeId/{noticeId}")
    public String noticeNoard(NoticeIdDto noticeIdDto){
        noticeBoardService.updateNoticeId(noticeIdDto);

        return "redirect:/user/boards/";
    }

    @GetMapping(value = "/new")
    public String insertNotice(Model model, Principal principal){
        String email = principal.getName();
        MemberFormDto memberFormDto = memberService.memberInfo(email);
        NoticeBoardDto noticeBoardDto = new NoticeBoardDto();
        noticeBoardDto.setMemberId(memberFormDto.getId());

        System.out.println("======================= NoticeBoardController get PostMapping");
        System.out.println("noticeBoardDto.getMemberId() : " + noticeBoardDto.getMemberId());
        System.out.println("memberFormDto.getId() : " + memberFormDto.getId());
        System.out.println("memberFormDto.getEmail() : " + memberFormDto.getEmail());

        model.addAttribute("noticeBoardDto", noticeBoardDto);
        return "boards/insertNotice";
    }

    @PostMapping(value = "/new")
    public String insertNotice(@Valid NoticeBoardDto noticeBoardDto, BindingResult bindingResult, Principal principal, Model model){

        String email = principal.getName();

        try{
            noticeBoardService.createNotice(noticeBoardDto, email);
        }catch (Exception e){
            e.printStackTrace();
            return "/";
        }


        return "redirect:/user/boards/";
    }

}