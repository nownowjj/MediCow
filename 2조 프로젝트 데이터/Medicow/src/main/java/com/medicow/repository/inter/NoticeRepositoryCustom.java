package com.medicow.repository.inter;

import com.medicow.model.dto.NoticeSearchDto;
import com.medicow.model.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface NoticeRepositoryCustom {
    Page<Notice> getMainNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable) ;

}
