package com.medicow.model.dto;

import com.medicow.model.entity.Notice;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter @Setter
public class NoticeFormDto {
    private Long id ;

    @NotEmpty(message = "제목은 필수 입력입니다.")
    private String subject ;

    @NotEmpty(message = "내용은 필수 입력입니다.")
    private String content ;

    private LocalDateTime regTime ;

    private static ModelMapper modelMapper = new ModelMapper() ;

    public Notice createNotice(){
        return modelMapper.map(this, Notice.class) ;
    }

    public static NoticeFormDto of(Notice notice){
        return modelMapper.map(notice, NoticeFormDto.class);
    }
}
