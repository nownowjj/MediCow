package com.medicow.repository.inter;

import com.medicow.model.constant.ActiveStatus;
import com.medicow.model.constant.DeleteStatus;
import com.medicow.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface AdminReviewRepository extends JpaRepository<Review, Long>, QuerydslPredicateExecutor<Review>, AdminReviewRepositoryCustom  {

    // 리뷰 삭제 상태 업데이트 하는 쿼리문
    @Modifying
    @Query(value= " update Review r set r.deleteStatus = :deleteStatus  where r.id = :id " )
    public Integer updateDeleteStatus(@Param("deleteStatus") DeleteStatus deleteStatus, @Param("id") Long id);

    // 액티브 상태 즉, 삭제를 요청하는 쿼리문
    @Modifying
    @Query(value = " update Review r set r.activeStatus = :activeStatus where r.id = :id " )
    public Integer updateActiveStatus(@Param("activeStatus")ActiveStatus activeStatus, @Param("id") Long id);
}
