package com.medicow.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class AdminFormDto {
    @NotBlank(message = "관리자의 이름을 입력하세요.")
    private String name;

    @NotEmpty(message = "이메일은 필수 입력입니다.")
    @Email(message = "이메일 형식으로 입력하세요.")
    private String email;

    @NotEmpty(message = "비밀 번호는 필수 입력입니다.")
    @Length(min = 8, max = 16, message = "비밀 번호는 8자 이상 16자 이하로 입력해 주세요.")
    private String password;

}
