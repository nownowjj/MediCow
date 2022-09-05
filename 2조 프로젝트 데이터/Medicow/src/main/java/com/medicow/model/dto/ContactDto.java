package com.medicow.model.dto;

import com.medicow.model.entity.Hospital;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ContactDto {

    private Long id;

    private Hospital hosName;

    private String subject;

    private LocalDateTime regTime ;
}
