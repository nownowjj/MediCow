package com.medicow.controller;

import com.medicow.model.constant.ContactStatus;
import com.medicow.model.dto.ContactFormDto;
import com.medicow.model.dto.ContactSearchDto;
import com.medicow.model.entity.Hospital;
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
public class HospitalContactController {
    private  final ContactService contactService;
    private final HospitalService hospitalService;

    @GetMapping(value = {"/hospital/contact", "/hospital/contact/{page}"})
    public String contact(ContactSearchDto contactSearchDto,
                          @PathVariable("page") Optional<Integer> page,
                          Model model, ContactFormDto contactFormDto,Principal principal){
        String hosId = principal.getName();
        Long hosNo = hospitalService.findHosNoByHosId(hosId);

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<ContactFormDto> datas = contactService.getContactPage(contactSearchDto, hosNo, pageable);

        model.addAttribute("datas", datas);
        model.addAttribute("contactSearchDto", contactSearchDto); // for 검색 조건 보존
//        model.addAttribute("datas",contactFormDto);
        model.addAttribute("maxPage", 5); // 하단에 보여줄 페이지 번호의 최대 개수

        return "hospital/contact/contactDo" ;
    }
    @GetMapping(value = "/hospital/my/contact/new")
    public String contactForm(Model model, Principal principal){
        //dto 객체(화면을 통하여 넘겨주거나 받은 객체)를 모델에 바인딩하면 실제 request 영역에 데이터가 들어갑니다.
        String hosId = principal.getName();
        Hospital hospital = hospitalService.findByHosId(hosId);
        model.addAttribute("datas", new ContactFormDto());
        model.addAttribute("hospital", hospital);
        return "hospital/contact/contactNew";
    }


    @PostMapping(value = "/hospital/my/contact/new")
    public String contactNewForm(@Valid ContactFormDto contactFormDto, BindingResult bindingResult, Model model, Principal principal, ContactSearchDto contactSearchDto){
        //dto 객체(화면을 통하여 넘겨주거나 받은 객체)를 모델에 바인딩하면 실제 request 영역에 데이터가 들어갑니다.
        String hosId = principal.getName();
        Hospital hospital = hospitalService.findByHosId(hosId);

        if(bindingResult.hasErrors()){
            model.addAttribute("datas", contactFormDto);
            return "redirect:/hospital/contact";
        }

        contactFormDto.setHospital(hospital);
        contactFormDto.setContactStatus(ContactStatus.NO);
        System.out.println("이다음이 안됨");
        contactService.contactForm(contactFormDto);
        return "redirect:/hospital/contact";

    }

    @GetMapping(value = "/hospital/my/contact/{id}")
    public String contactDtl(@PathVariable(value = "id")Long id, ContactFormDto contactFormDto,Model model, Principal principal){
        //dto 객체(화면을 통하여 넘겨주거나 받은 객체)를 모델에 바인딩하면 실제 request 영역에 데이터가 들어갑니다.
        String hosId = principal.getName();
        ContactFormDto data = contactService.findContact(id);
        model.addAttribute("data",data);
        return "hospital/contact/contactDtlDo";
    }
    @PostMapping(value = "/hospital/my/contact/{id}/update")
    public String contactDtlUpdate(@PathVariable(value = "id")Long id, @Valid  ContactFormDto contactFormDto,BindingResult bindingResult,Model model, Principal principal){
        if(bindingResult.hasErrors()){
            model.addAttribute("data",contactFormDto);
            return "hospital/contactDtlDo";
        }
        ContactFormDto data = contactService.contactUpdate(id,contactFormDto.getSubject(),contactFormDto.getContent());
        model.addAttribute("data",data);
        model.addAttribute("updateMessage","게시글 "+ id+"번이 수정되었습니다.");

        return "hospital/contact/contactDtlDo";
    }

    @PostMapping(value = "/hospital/my/contact/{id}/delete")
    public String contactDtlDelete(@PathVariable(value = "id")Long id, Model model, Principal principal){

        contactService.contactDelete(id);
        model.addAttribute("deleteMessage","게시글 "+ id+"번이 삭제되었습니다.");
        return "redirect:/hospital/contact";
    }

}