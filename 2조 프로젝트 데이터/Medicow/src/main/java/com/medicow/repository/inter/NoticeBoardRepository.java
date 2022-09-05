package com.medicow.repository.inter;

import com.medicow.model.entity.NoticeBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long>, NoticeBoardRepositoryCustom {

    NoticeBoard findNotice_boardById(Long noticeId);

}
