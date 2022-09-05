package com.medicow.model.entity;

import com.medicow.model.dto.NoticeBoardDto;
import com.medicow.model.dto.NoticeIdDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "noticeBoard")
@Getter @Setter @ToString
public class NoticeBoard extends BaseEntity{
    @Id
    @Column(name = "notice_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    private Integer views;
    private String subject;
    private String contents;

    public static NoticeBoard createNotice(NoticeBoardDto noticeBoardDto, Member member){
        System.out.println("============================ NoticeBoard createNotice");
        NoticeBoard noticeBoard = new NoticeBoard();

        noticeBoard.setCreatedBy(member.getName());
        noticeBoard.setContents(noticeBoardDto.getContents());
        noticeBoard.setMember(member);
        noticeBoard.setSubject(noticeBoardDto.getSubject());
        noticeBoard.setRegTime(LocalDateTime.now());
        noticeBoard.setUpdateTime(LocalDateTime.now());
        noticeBoard.setViews(0);

        return noticeBoard;
    }

    public void viewsUpdate() {
        this.views += 1;
    }

    public void updateNoticeId(NoticeIdDto noticeIdDto) {
        this.subject = noticeIdDto.getSubject();
        this.contents = noticeIdDto.getContents();
        this.setUpdateTime(LocalDateTime.now());
    }
}
