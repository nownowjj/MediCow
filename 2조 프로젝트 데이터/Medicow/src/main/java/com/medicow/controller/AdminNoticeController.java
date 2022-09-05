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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class AdminNoticeController {

    final private NoticeService noticeService;

    @GetMapping(value = {"admin/notice", "/admin/notice/{page}"})
    public String notice(NoticeSearchDto noticeSearchDto,
                         @PathVariable("page") Optional<Integer> page,
                         Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Notice> datas = noticeService.getNoticePage(noticeSearchDto, pageable);

        model.addAttribute("datas", datas);
        model.addAttribute("noticeSearchDto", noticeSearchDto); // for 검색 조건 보존
        model.addAttribute("maxPage", 5); // 하단에 보여줄 페이지 번호의 최대 개수

        return "admin/notice" ;
    }

    @GetMapping(value = "/admin/notice/new")
    public String noticeForm(Model model){
        // noticeFormDto : 상품 등록을 하게 되면 이 객체에 데이터 정보가 들어 갑니다.
        model.addAttribute("noticeFormDto", new NoticeFormDto());
        return "admin/noticeForm" ;
    }

    @PostMapping(value = "/admin/notice/new")
    public String noticeNew(@Valid NoticeFormDto noticeFormDto, BindingResult bindingResult, Model model){
        // noticeFormDto) 상품 등록을 위하여 사용자가 기입한 값
        // bindingResult) 유효성 검사에 문제가 있으면, 여기에 기록이 됩니다.
        // model) 저장할 데이터 또는 별도의 메시지 등을 html로 보내기 위한 Model 객체
        // noticeImgFileList) 업로드를 위한 상품 이미지들을 저장하고 있는 리스트 컬렉션

        if(bindingResult.hasErrors()){
            return "/admin/noticeForm" ;
        }


        try{
            noticeService.saveNotice(noticeFormDto) ;
        }catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중에 오류가 발생하였습니다.") ;
            return "/admin/noticeForm" ;
        }

        return "redirect:/admin/notice"; // 메인 페이지로 이동합니다.
    }

    @GetMapping("/admin/noticeDtl/{Id}")
    public String noticeDtl(Model model, @PathVariable("Id") Long noticeId){
        NoticeFormDto noticeFormDto = noticeService.getNoticeDtl(noticeId) ;
        model.addAttribute("notice", noticeFormDto) ;
        return "admin/noticeDtl" ;
    }
    @GetMapping(value = "/admin/noticeForm/{Id}")
    public String noticeDtl(@PathVariable("Id") Long noticeId, Model model){
        // Long noticeId = Long.parseLong(request.getParameter("noticeId")) ;

        try {
            NoticeFormDto noticeFormDto = noticeService.getNoticeDtl(noticeId) ;
            model.addAttribute("noticeFormDto", noticeFormDto) ;

        }catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재 하지 않는 상품입니다.") ;
            model.addAttribute("noticeFormDto", new NoticeFormDto()) ;
        }
        return "admin/noticeForm" ;
    }


    @PostMapping(value = "/admin/noticeForm/{noticeId}")
    public String noticeUpdate(@Valid NoticeFormDto noticeFormDto, BindingResult bindingResult, Model model){
        // noticeFormDto) 화면에서 넘어 오는 command 객체
        // bindingResult) 폼 유효성 검사시 문제가 있는 지 체크하기 위한 클래스
        // noticeImgFileList) 폼에서 넘어 오는 여러 개의 <input type="file"> 객체 리스트
        // model) 뷰 영역으로 넘겨줄 정보들을 바인딩하기 위한 모델 객체

        String whenError = "admin/noticeForm";

        if (bindingResult.hasErrors()){
            return whenError ;
        }


        try{
            noticeService.updateNotice(noticeFormDto) ;
        }catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중에 오류가 발생하였습니다.");
            e.printStackTrace();
            return whenError ;
        }

        return "redirect:/admin/notice"; // 메인 페이지로 이동합니다.
    }
    @GetMapping(value="/admin/noticed/{id}")
    public String noticeDelete(@PathVariable("id") Long id){
        noticeService.deleteNotice(id);

        return "redirect:/admin/notice";
        //return "/admin/notice";
    }
}
