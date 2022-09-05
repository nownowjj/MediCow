package com.medicow.controller;

import com.medicow.model.constant.ContactStatus;
import com.medicow.model.dto.ContactFormDto;
import com.medicow.model.dto.ContactSearchDto;
import com.medicow.model.entity.Contact;
import com.medicow.service.ContactService;
import com.medicow.service.HospitalService;
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

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AdminContactController {
    private  final ContactService contactService;
    private final HospitalService hospitalService;

    @GetMapping(value = "/admin/contactDtl/{id}")
    public String contactDtl(@PathVariable(value = "id")Long id, ContactFormDto contactFormDto, Model model, Principal principal){
        //dto 객체(화면을 통하여 넘겨주거나 받은 객체)를 모델에 바인딩하면 실제 request 영역에 데이터가 들어갑니다.
        String hosId = principal.getName();
        ContactFormDto data = contactService.findContact(id);
        model.addAttribute("data",data);
        return "admin/contactDtl";
    }

    @GetMapping(value = {"/admin/contact", "/admin/contact/{page}"})
    public String contact(ContactSearchDto contactSearchDto,
                          @PathVariable("page") Optional<Integer> page,
                          Model model, ContactFormDto contactFormDto,Principal principal){
        String hosId = principal.getName();
        Long hosNo = hospitalService.findHosNoByHosId(hosId);

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<ContactFormDto> datas = contactService.getContactPage(contactSearchDto, hosNo, pageable);

        model.addAttribute("datas", datas);
        model.addAttribute("contactSearchDto", contactSearchDto); // for 검색 조건 보존
//        model.addAttribute("datas",contactFormDto);
        model.addAttribute("maxPage", 5); // 하단에 보여줄 페이지 번호의 최대 개수

        return "admin/contactDo" ;
    }
//    @GetMapping(value = "/hospital/my/contact/new")
//    public String contactForm(Model model, Principal principal){
//        //dto 객체(화면을 통하여 넘겨주거나 받은 객체)를 모델에 바인딩하면 실제 request 영역에 데이터가 들어갑니다.
//        String hosId = principal.getName();
//        Hospital hospital = hospitalService.findByHosId(hosId);
//        model.addAttribute("datas", new ContactFormDto());
//        model.addAttribute("hospital", hospital);
//        return "admin/contactNew";
//    }


//    @PostMapping(value = "/hospital/my/contact/new")
//    public String contactNewForm(@Valid ContactFormDto contactFormDto, BindingResult bindingResult, Model model, Principal principal, ContactSearchDto contactSearchDto){
//        //dto 객체(화면을 통하여 넘겨주거나 받은 객체)를 모델에 바인딩하면 실제 request 영역에 데이터가 들어갑니다.
//        String hosId = principal.getName();
//        Hospital hospital = hospitalService.findByHosId(hosId);
//
//        if(bindingResult.hasErrors()){
//            model.addAttribute("datas", contactFormDto);
//            return "redirect:/admin/contact";
//        }
//
//        contactFormDto.setHospital(hospital);
//        contactFormDto.setContactStatus(ContactStatus.NO);
//        System.out.println("이다음이 안됨");
//        contactService.contactForm(contactFormDto);
//        return "redirect:/admin/contact";
//
//    }

//    @GetMapping(value = "/hospital/my/contact/{id}")
//    public String contactDtl(@PathVariable(value = "id")Long id, ContactFormDto contactFormDto,Model model, Principal principal){
//        //dto 객체(화면을 통하여 넘겨주거나 받은 객체)를 모델에 바인딩하면 실제 request 영역에 데이터가 들어갑니다.
//        String hosId = principal.getName();
//        ContactFormDto data = contactService.findContact(id);
//        model.addAttribute("data",data);
//        return "admin/contactDtlDo";
//    }
//    @PostMapping(value = "/hospital/my/contact/{id}/update")
//    public String contactDtlUpdate(@PathVariable(value = "id")Long id, @Valid  ContactFormDto contactFormDto,BindingResult bindingResult,Model model, Principal principal){
//        if(bindingResult.hasErrors()){
//            model.addAttribute("data",contactFormDto);
//            return "admin/contactDtlDo";
//        }
//        ContactFormDto data = contactService.contactUpdate(id,contactFormDto.getSubject(),contactFormDto.getContent());
//        model.addAttribute("data",data);
//        model.addAttribute("updateMessage","게시글 "+ id+"번이 수정되었습니다.");
//
//        return "admin/contactDtlDo";
//    }
//
//    @PostMapping(value = "/hospital/my/contact/{id}/delete")
//    public String contactDtlDelete(@PathVariable(value = "id")Long id, Model model, Principal principal){
//
//        contactService.contactDelete(id);
//        model.addAttribute("deleteMessage","게시글 "+ id+"번이 삭제되었습니다.");
//        return "redirect:/admin/contact";
//    }

    @GetMapping(value = {"admin/contactad", "/admin/contactad/{page}"})
    public String contact(ContactSearchDto contactSearchDto,
                          @PathVariable("page") Optional<Integer> page,
                          Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Contact> datas = contactService.getContactPage(contactSearchDto, pageable);

        model.addAttribute("datas", datas);
        model.addAttribute("contactSearchDto", contactSearchDto); // for 검색 조건 보존
        model.addAttribute("maxPage", 5); // 하단에 보여줄 페이지 번호의 최대 개수

        return "admin/contact" ;
    }


    //수정버튼클릭
    @PostMapping(value = "/admin/contactForm/{Id}")
    public String contactUpdate(@Valid ContactFormDto contactFormDto, BindingResult bindingResult, Model model) throws Exception {
        // contactFormDto) 화면에서 넘어 오는 command 객체
        // bindingResult) 폼 유효성 검사시 문제가 있는 지 체크하기 위한 클래스
        // contactImgFileList) 폼에서 넘어 오는 여러 개의 <input type="file"> 객체 리스트
        // model) 뷰 영역으로 넘겨줄 정보들을 바인딩하기 위한 모델 객체

        String whenError = "admin/contact";

        contactFormDto.setContactStatus(ContactStatus.YES);
        contactService.updateContact(contactFormDto) ;

//        if (bindingResult.hasErrors()){
//            return whenError ;
//        }
//        try{
//
//            contactFormDto.setContactStatus(ContactStatus.YES);
//            contactService.updateContact(contactFormDto) ;
//        }catch (Exception e){
//            model.addAttribute("errorMessage", "공지 수정 중에 오류가 발생하였습니다.");
//            e.printStackTrace();
//            return whenError ;
//        }
        return "redirect:/admin/contactad"; // 메인 페이지로 이동합니다.
    }

}
