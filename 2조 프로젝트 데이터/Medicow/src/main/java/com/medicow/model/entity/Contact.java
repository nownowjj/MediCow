package com.medicow.model.entity;

import com.medicow.model.constant.ContactStatus;
import com.medicow.model.dto.ContactFormDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="contact")
@Getter
@Setter
public class Contact extends BaseEntity{

    @Id
    @Column(name = "contact_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ContactStatus contactStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private Hospital hospital;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String content;

    @Column
    private String adminContent;

    public void updateContact(ContactFormDto contactFormDto){
        // contactFormDto는 화면에서 넘겨 주는 수정될 dto 객체 정보로써,
        // 모든 변수들의 내용들을 Entity 변수에 저장시켜 주도록 합니다.
        this.contactStatus = contactFormDto.getContactStatus() ;
        this.adminContent = contactFormDto.getAdminContent() ;

    }


}
