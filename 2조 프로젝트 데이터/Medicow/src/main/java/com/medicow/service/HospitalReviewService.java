package com.medicow.service;

import com.medicow.model.constant.ActiveStatus;
import com.medicow.model.constant.DeleteStatus;
import com.medicow.model.dto.ReviewDto;
import com.medicow.model.entity.Hospital;
import com.medicow.model.entity.Review;
import com.medicow.repository.inter.HospitalReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor @Transactional
public class HospitalReviewService {
    // 리뷰 ORM을 위한 hospitalReviewRepository를 주입
    final HospitalReviewRepository hospitalReviewRepository;

    // 헤당 병원의 후기를 가져오는 메소드
    public Page<ReviewDto> findReviewByHospital(Hospital hospital, Pageable pageable) {
        // Repository에서 리뷰 목록을 받음
        List<Review> reviews = hospitalReviewRepository.findReviewByHospital(hospital, DeleteStatus.NO, ActiveStatus.ABLE, pageable);
        // 전체 수를 받음
        Integer totalCount = hospitalReviewRepository.countReviewByHospital(hospital, DeleteStatus.NO, ActiveStatus.ABLE);
        System.out.println(totalCount);
        // 새로운 ReviewDto 리스트 생성
        List<ReviewDto> reviewDtos = new ArrayList<ReviewDto>();
        // 해당 리스트에 받아온 리뷰 Entity의 데이터를 집어 넣음
        for (Review review : reviews) {
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setReviewId(review.getReviewId());
            reviewDto.setContents(review.getContent());
            reviewDto.setPoint(review.getPoint());
            reviewDto.setWriteTime(review.getRegTime());
            reviewDto.setMemberName(review.getReservation().getMember().getName());
            System.out.println(reviewDto.getMemberName());
            // 리뷰를 작성한 사람의 이름의 가운데에 *로 바꿔주는 알고리즘(?)
            String names=reviewDto.getMemberName();
            int num = names.length()/2 -1;
            if(names.length()%2==0){
                if(names.length()==2){
                    names.replace(names.charAt(1),'*');
                }else{
                    names.replace(names.charAt(num),'*');
                    names.replace(names.charAt(num+1),'*');
                }
            }else if(names.length()%2==1){
                num = names.length()/2;
                names.replace(names.charAt(num),'*');
            }
            if(review.getDeleteStatus()==null){
                reviewDto.setDeleteStatus((DeleteStatus.NO).toString());
            }else{
                reviewDto.setDeleteStatus(review.getDeleteStatus().toString());
            }
            reviewDto.setHosName(review.getReservation().getHospital().getHosName());
            reviewDtos.add(reviewDto);
        }
        return new PageImpl<ReviewDto>(reviewDtos, pageable, totalCount);
    }

    // 병원에서 삭제를 요청하면 해당 삭제 요청에 의해 동작하는 메소드
    public void deleteRequest(Long id){
        hospitalReviewRepository.deleteRequest(DeleteStatus.YES,id);
    }

    public Long curReviewCount(Hospital hospital, DeleteStatus deleteStatus) {

        Long curReviewCount = hospitalReviewRepository.curReviewCount(hospital, deleteStatus);
        return curReviewCount;
    }

    public String curReviewTotal(Hospital hospital, DeleteStatus deleteStatus) {

        List<Double> curReviewTotal = hospitalReviewRepository.curReviewTotal(hospital, deleteStatus);

        Double  total = 0.0;
        for (Double review : curReviewTotal) {
            System.out.println(review+"-----------------------------");
            total+=review;
        }
        System.out.println(" 음음음음음ㅇ므 토탈 평점"+total);

        if(curReviewTotal.size()==0) {
            return "0.0";
        }
        String result = String.format("%.2f",total/curReviewTotal.size());
        System.out.println("평균~~"+result);
        return result;
    }
}