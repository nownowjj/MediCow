package com.medicow.model.entity;

import com.medicow.model.dto.NoticeFormDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="notice")
@Getter @Setter
public class Notice extends BaseEntity{

    @Id
    @Column(name = "notice_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id ;

    @Column(nullable = false, length = 50)
    private String subject ;

    @Column(nullable = false)
    private String content ;

//    @CreatedDate
//    @Column(updatable = false)
//    private LocalDateTime regTime ;

    public void updateNotice(NoticeFormDto noticeFormDto){
        // noticeFormDto는 화면에서 넘겨 주는 수정될 dto 객체 정보로써,
        // 모든 변수들의 내용들을 Entity 변수에 저장시켜 주도록 합니다.
        this.subject = noticeFormDto.getSubject() ;
        this.content = noticeFormDto.getContent() ;

    }
    //private LocalDateTime updateTime ; 수정시간 나중에 우선순위3

    //private enum category; 일단 기능 구현 하고 카테고리는 우선순위2
}
