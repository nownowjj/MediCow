package com.medicow.controller;

import com.medicow.model.dto.MemberFormDto;
import com.medicow.model.entity.Member;
import com.medicow.repository.inter.MemberRepository;
import com.medicow.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/user/members")
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    // 로그인 폼으로 이동 
    @GetMapping(value = "/login")   // form 태그와 SecurityConfig에 정의 되어 있음.
    public String loginMember(){
        System.out.println("로그인 시도");
        System.out.println();
        return "/member/memberLoginForm";
    }
    
    // 로그인 수행 에러시 error문 추가
    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        // "loginErrorMsg"와 관련된 내용은 파일 memberLoginForm.html 안에 구현 되어 있습니다.
        model.addAttribute("loginErrorMsg", "이메일 또는 비밀 번호를 확인해 주세요.") ;
        System.out.println("잘못침 ㅇㅇ");
        return "/member/memberLoginForm" ;
    }

    // 로그인 페이지에서 > 회원가입 페이지로
    @GetMapping(value = "/join")
    public String memberForm(Model model){
        // dto 객체(화면을 통하여 넘겨 주거나 받는 객체)를 모델에 바인딩하면 실제 request 영역에 데이터가 들어 갑니다
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "/member/memberJoinForm";
    }

    @PostMapping(value = "/join")
    public String newMember(@Valid MemberFormDto memberFormDto,
                            BindingResult bindingResult,
                            Model model){
        if(bindingResult.hasErrors()){ // 사용자 회원가입 폼에 문제 발생시
            return "/member/memberJoinForm";
        }

        try{
            Member member = Member.createMember(memberFormDto,passwordEncoder);

            memberService.saveMember(member);
            return "/member/memberLoginForm"; // 회원가입 성공시 로그인 페이지로 이동 시킴

        }catch (IllegalStateException e){
            return "/member/memberJoinForm"; // 에러 발생시 다시 회원가입 페이지
        }

    }

    private final MemberRepository memberRepository;
/*------------- 마이 페이지 --------------- */

    //  > 마이페이지 페이지로
    @GetMapping("/mypagemain")
    public String mypage(){
        return "/mypage/mypagemain";
    }

    // 내 정보 수정 페이지
    @GetMapping(value = "/meUpdate")
    public String memberInfo(Model model){
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        MemberFormDto memberFormDto = memberService.memberInfo(memberEmail);
        model.addAttribute("memberFormDto",memberFormDto);
        return "/mypage/meUpdate";
    }

    // 내 정보 수정 페이지에서 비밀번호 컨펌
    @PostMapping(value = "/meUpdate/pwConfirm")
    public @ResponseBody
    ResponseEntity pwConfirm(@RequestBody String password,
                             BindingResult bindingResult,
                             Principal principal){
        String email = principal.getName();
        Member member = memberService.memberPwInfo(email);
        int cnt = 0;
        if(passwordEncoder.matches(password, member.getPassword())){
            cnt = 1;
        }

        return new ResponseEntity<Integer>(cnt,  HttpStatus.OK);
    }

    // 내 정보 수정 페이지에서 수정시
    @PostMapping(value = "/meUpdate/update")
    public String updateMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult , Principal principal,Model model){
        System.out.println("=====MemberController 내 정보 수정 진행 ===============");

        try {
            memberService.updateMember(memberFormDto,passwordEncoder);
            model.addAttribute("successmsg","회원정보 수정을 성공하였습니다!");
        }catch (Exception e){
            e.printStackTrace();
        }
        MemberFormDto memberInfoDto = memberService.memberInfo(memberFormDto.getEmail());
        model.addAttribute("memberFormDto",memberInfoDto);
        return "/mypage/meUpdate";
    }

}
