package com.medicow.controller;

import com.medicow.model.dto.AdminFormDto;
import com.medicow.model.entity.Admin;
import com.medicow.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
public class AdminLoginController {
    // 어드민 로그인 화면으로 이동하는 GetMapping입니다.

    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder ;

    @GetMapping(value = "/login")
    public String adminLogin(){
        return "/admin/login";
    }

    // 어드민 로그인 화면에서 에러가 난다면 이동하는 GetMapping입니다.
    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        // "loginErrorMsg"와 관련된 내용은 파일 memberLoginForm.html 안에 구현 되어 있습니다.
        model.addAttribute("loginErrorMsg", "이메일 또는 비밀 번호를 확인해 주세요.") ;
        return "/admin/login" ;
    }

    // 어드민 회원 등록 페이지로 이동하는 GetMappig입니다.
    // 비밀번호 암호화 때문에 구현하고, 기능이 있지만
    // 화면에 보여주지는 않을 예정입니다.
    @GetMapping(value = "/register")
    public String adminRegister(Model model){
        model.addAttribute("adminFormDto", new AdminFormDto()) ;
        return "/admin/register" ;
    }


    // 어드민 회원 등록 페이지에서 데이터를 입력하고 submit하면
    // 이동하는 PostMapping입니다.
    @PostMapping(value = "/register")
    public String newAdmin(@Valid AdminFormDto adminFormDto,
                           BindingResult bindingResult,
                           Model model){
        // DTO에서 받은 데이터를 넘겨받을 때
        // 오류가 생기면 등록페이지로 다시 이동합니다.
        if(bindingResult.hasErrors()){
            return "/admin/register" ;
        }

        // DTO에서 받은 데이터를 객체에 담고
        // adminService의 메소드를 호출 하고 성공할 시
        // 로그인 페이지로 이동합니다.
        try{
            Admin admin = Admin.createAdmin(adminFormDto, passwordEncoder);
            adminService.saveAdmin(admin) ;
            return "/admin/login" ;
        }catch (IllegalStateException e){
            // 오류 발생 시 등록페이지로 이동합니다.
            return "/admin/register" ;
        }
    }

}
