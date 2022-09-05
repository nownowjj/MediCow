package com.medicow.service;

import com.medicow.model.dto.NoticeBoardDto;
import com.medicow.model.dto.NoticeIdDto;
import com.medicow.model.dto.NoticeSearchDto;
import com.medicow.model.entity.Member;
import com.medicow.model.entity.NoticeBoard;
import com.medicow.repository.inter.MemberRepository;
import com.medicow.repository.inter.NoticeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeBoardService {
    private final NoticeBoardRepository noticeBoardRepository;
    private final MemberRepository memberRepository;


    public void createNotice(NoticeBoardDto noticeBoardDto, String email) {
        System.out.println("================ NoticeBoardService");
        System.out.println("noticeBoardDto.getSubject() : " + noticeBoardDto.getSubject());

        Member member = memberRepository.findByEmail(email);
        NoticeBoard noticeBoard = NoticeBoard.createNotice(noticeBoardDto, member);

        noticeBoardRepository.save(noticeBoard);
    }

    public Page<NoticeBoard> getNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable) {
        System.out.println("======================== NoticeBoardService ");

        return noticeBoardRepository.getNoticePage(noticeSearchDto, pageable);
    }


    public NoticeIdDto getNoticeId(Long noticeId) {
        NoticeBoard noticeBoard = noticeBoardRepository.findNotice_boardById(noticeId);
        noticeBoard.viewsUpdate();

        System.out.println("========================== NoticeBoardService");
        System.out.println("noticeBoard.getId() : " + noticeBoard.getId());

        NoticeIdDto noticeIdDto = new NoticeIdDto();

        noticeIdDto.setSubject(noticeBoard.getSubject());
        noticeIdDto.setContents(noticeBoard.getContents());
        noticeIdDto.setCreatedBy(noticeBoard.getCreatedBy());
        noticeIdDto.setNoticeId(noticeBoard.getId());
        noticeIdDto.setRegTime(noticeBoard.getRegTime());
        noticeIdDto.setUpdateTime(noticeBoard.getUpdateTime());
        noticeIdDto.setViews(noticeBoard.getViews());

        return noticeIdDto;
    }

    public void updateNoticeId(NoticeIdDto noticeIdDto) {
        NoticeBoard noticeBoard = noticeBoardRepository.findNotice_boardById(noticeIdDto.getNoticeId());
        noticeBoard.updateNoticeId(noticeIdDto);
    }
}
