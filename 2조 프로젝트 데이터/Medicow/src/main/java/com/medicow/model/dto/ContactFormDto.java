package com.medicow.model.dto;

import com.medicow.model.constant.ContactStatus;
import com.medicow.model.entity.Contact;
import com.medicow.model.entity.Hospital;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
public class ContactFormDto {

    private ContactStatus contactStatus;

    private Long id;

    private Hospital hospital;

    @NotEmpty(message = "제목은 필수 입력입니다.")
    private String subject;

    @NotEmpty(message = "내용은 필수 입력입니다.")
    private String content;

    private String adminContent;

    private LocalDateTime regTime ;

    private LocalDateTime updateTime;

    private static ModelMapper modelMapper = new ModelMapper() ;

    public Contact createContact(){
        return modelMapper.map(this, Contact.class) ;
    }
}
