package com.medicow.repository.inter;

import com.medicow.model.dto.NoticeSearchDto;
import com.medicow.model.entity.NoticeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeBoardRepositoryCustom {

    Page<NoticeBoard> getNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable);
}
