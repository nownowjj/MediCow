package com.medicow.controller;

import com.medicow.model.dto.HospitalFormDto;
import com.medicow.model.dto.HospitalSearchDto;
import com.medicow.model.entity.Hospital;
import com.medicow.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/hospital/members")
@RequiredArgsConstructor //final 객체에 값을 주입시킬수 있는 어노테이션
public class HospitalMemberController {
    private final HospitalService hospitalService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String hospitalMemberForm(Model model){
        //dto 객체(화면을 통하여 넘겨주거나 받은 객체)를 모델에 바인딩하면 실제 request 영역에 데이터가 들어갑니다.
        System.out.println("넘겨줄거양~~ 받아~~~");
        model.addAttribute("hospitalFormDto", new HospitalFormDto());
        return "hospital/member/memberForm";
    }

    @PostMapping(value = "/new")
    public String saveHospitalMemberForm(@Valid HospitalFormDto hospitalFormDto, BindingResult bindingResult, Model model,@RequestParam("hospitalImgFile") List<MultipartFile> hospitalImgFileList){


        if(bindingResult.hasErrors()){
            model.addAttribute(hospitalFormDto);
            return "hospital/member/memberForm";
        }
        if(hospitalImgFileList.get(0).isEmpty() && hospitalFormDto.getHosNo()==null){
            model.addAttribute("errorMessage","첫번째 이미지는 필수 입력 값입니다.");
            return "hospital/member/memberForm";
        }

        //유효성검사에 문제가 없으면 Dto객체를 JPA로 보내줄려면 엔터티로 만들어야 합니다.
        try{
            Hospital hospital = Hospital.createHospital(hospitalFormDto, passwordEncoder);
            System.out.println("병원 이미지 정보");
            System.out.println(hospitalImgFileList.get(0).getOriginalFilename()+" 이거 존재해????!!!!?!?!?!?!?!??!?!!!!!");
            hospitalService.saveHospital(hospital,hospitalImgFileList,hospitalFormDto);

            return "hospital/front/frontMain"; //메인페이지로 이동
        }catch(IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "hospital/member/memberForm";
        } catch (Exception e) {
            e.printStackTrace();
            return "hospital/member/memberForm";
        }
    }

    @GetMapping(value = "/front")
    public String hospitalFrontForm(Model model){
        return "hospital/front/frontMain";
    }


    @GetMapping(value = "/login") //form 태그와 SecurityConfig 파일에 정의 되어 있습니다.
    public String loginMember(){
        return "hospital/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        //"loginErrorMsg"와 관련된 내용은 파일 memberLoginForm.html 안에 구현 되어 있습니다.
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "hospital/member/memberLoginForm";
    }

    @GetMapping(value={"/findHospital","/findHospital/{page}"})
    public String hospitalList(HospitalSearchDto hospitalSearchDto, @PathVariable("page") Optional<Integer> page, Model model){
        // 페이징 객체 생성_ 한번에 3개씩 보여줌
        Pageable pageable = PageRequest.of(page.isPresent()?page.get():0,10);
        // Service로 검색조건(hospitalSearchDto와 페이징 객체를 넘김)
        Page<HospitalFormDto> hospitalsList = hospitalService.findHospitalBy(hospitalSearchDto,pageable);

        // 모델에 바인딩
        model.addAttribute("hospitals",hospitalsList);
        model.addAttribute("hospitalSearchDto",hospitalSearchDto);
        model.addAttribute("maxPage",5);

        return "hospital/member/findHospital";
    }



}
