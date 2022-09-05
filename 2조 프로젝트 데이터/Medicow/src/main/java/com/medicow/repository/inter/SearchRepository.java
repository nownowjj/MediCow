package com.medicow.repository.inter;

import com.medicow.model.dto.HosptialSearchResultDto;
import com.medicow.model.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SearchRepository extends JpaRepository<Hospital,Long>, QuerydslPredicateExecutor<Hospital> {

    // 입력한 검색어로 병원을 조회합니다.
//    @Query("select distinct h from Hospital h where  " +
//    "  h.hosName like %:searchQuery% or h.hosSubject like %:searchQuery% ")
//    List<Hospital> findByHospital(@Param("searchQuery") String searchQuery);

    @Query( value = " SELECT * " +
            " FROM ( " +
            " SELECT(6371 * acos ( cos( radians(:user_y)) * cos( radians(hos_posy)) * cos( radians(hos_posx) - radians(:user_x)) + " +
            " sin(radians(:user_y)) * sin(radians(hos_posy)))) AS distance , hos_name , hos_address " +
            " FROM (SELECT distinct * " +
            " FROM Hospital" +
            " WHERE hos_name like %:searchQuery% or hos_subject like %:searchQuery% )" +
            " ORDER BY distance asc" +
            " ) DATA" +
            " WHERE DATA.distance < 3" ,nativeQuery = true)
    List<HosptialSearchResultDto> findByHospital(@Param("searchQuery") String searchQuery,
                                                 @Param("user_x") double user_x,
                                                 @Param("user_y") double user_y);

}
