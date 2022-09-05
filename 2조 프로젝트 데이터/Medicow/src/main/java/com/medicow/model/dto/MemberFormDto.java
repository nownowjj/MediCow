package com.medicow.model.dto;

import com.medicow.model.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter @Setter
public class MemberFormDto {
    private Long id;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    private String email ;  //사용자 이메일 pk

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name ;   // 사용자 이름

    @NotEmpty(message = "비밀 번호는 필수 입력 값입니다.")
    @Length(min = 8, max = 16, message = "비밀 번호는 8자 이상 16자 이하로 입력해 주세요.")
    private String password ;   // 사용자 비밀번호

    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String address ;    // 사용자 주소

    private String phone;   // 사용자 휴대폰 번호

    private String age;    // 사용자 나이

    private LocalDateTime regTime; // 사용자 회원가입 일자

    private String gender;  // 사용자 성별

    private double user_x; // 사용자 입력한 주소의 x 좌표

    private double user_y; // 사용자 입력한 주소의 y 좌표

    private String hand_phone;

    public void findMember(Member member){
        this.name = member.getName();
        this.address = member.getAddress();
    }


}
