package com.medicow.service;

import com.medicow.model.dto.NoticeFormDto;
import com.medicow.model.dto.NoticeSearchDto;
import com.medicow.model.entity.Notice;
import com.medicow.repository.inter.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true)
    public Page<Notice> getNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable){
        return noticeRepository.getMainNoticePage(noticeSearchDto, pageable);
    }

    public Long saveNotice(NoticeFormDto noticeFormDto) throws Exception{
        Notice notice = noticeFormDto.createNotice() ;
        noticeRepository.save(notice) ;


        return notice.getId() ;
    }

    public Long updateNotice(NoticeFormDto noticeFormDto) throws Exception{
        Notice notice = noticeRepository.findById(noticeFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        notice.updateNotice(noticeFormDto);

        return notice.getId();
    }

    @Transactional(readOnly = true)
    public NoticeFormDto getNoticeDtl(Long noticeId){

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(EntityNotFoundException::new) ;
        NoticeFormDto noticeFormDto = NoticeFormDto.of(notice) ;

        return noticeFormDto ;
    }
    @Transactional
    public void deleteNotice(Long id){
        noticeRepository.deleteById(id);
    }

    public List<NoticeFormDto> findTop3OrderByDesc() {
        List<Notice> notices = noticeRepository.findTop3ByOrderByIdDesc();
        System.out.println(notices.size());
        List<NoticeFormDto> noticeFormDtos = new ArrayList<NoticeFormDto>();
        for (Notice notice : notices) {
            NoticeFormDto noticeFormDto = new NoticeFormDto();
            noticeFormDto.setContent(notice.getContent());
            noticeFormDto.setId(notice.getId());
            noticeFormDto.setRegTime(notice.getRegTime());
            noticeFormDto.setSubject(notice.getSubject());
            noticeFormDtos.add(noticeFormDto);
        }
        return noticeFormDtos;
    }

}
