package com.medicow.service;

import com.medicow.model.entity.Admin;
import com.medicow.model.entity.Hospital;
import com.medicow.model.entity.Member;
import com.medicow.repository.inter.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService implements UserDetailsService {
    private final AdminRepository adminRepository;
    private final HospitalRepository hospitalRepository;
    private final MemberRepository memberRepository;
    private final AdminHospitalRepository adminHospitalRepository;
    private final AdminMemberRepository adminMemberRepository;

    private void validateDuplicateAdmin(Admin admin){
        Admin findAdmin = adminRepository.findByEmail(admin.getEmail());

        if(findAdmin != null){
            throw new IllegalStateException("이미 가입된 어드민입니다.");
        }
    }

    public Admin saveAdmin(Admin admin){
        validateDuplicateAdmin(admin) ;
        return adminRepository.save(admin) ;
    }

    @Override // 이메일을 이용하여 회원 정보를 찾기
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(email) ;
        if(admin == null){ // 회원이 존재하지 않는 경우
            throw new UsernameNotFoundException(email) ;
        }
        return User.builder()
                .username(admin.getEmail())
                .password(admin.getPassword())
                .roles(admin.getRole().toString())
                .build();
    }

    // 관리자 페이지로 공공데이터 모든 리스트정보 가져오기
    // 모든 리스트인데 사용할지 말지는 추후에 정하도록 합니다.
    public List<Hospital> getAll(){
        return adminHospitalRepository.findAll();
    }

    // 일정 기간을 입력하면 regTime 범위에 따라 데이터(병원)를 카운트 해줍니다.
    public Long getHospitalCountByRegTime(String firstDate, String secondDate){
        return adminHospitalRepository.countByRegTime(firstDate, secondDate);
    }

    // 일정 기간을 입력하면 regTime 범위에 따라 데이터(회원)를 카운트 해줍니다.
    public Long getMemberCountByRegTime(String firstDate, String secondDate){
        return adminMemberRepository.countByRegTime(firstDate, secondDate);
    }

    // 관리자 페이지에서 모든 멤버 리스트 조회
    public List<Member> getAllMember(){
        return adminMemberRepository.findAll();
    }

    // 이 웹사이트에 등록된 병원 리스트 조회
    public List<Hospital> getRegisteredHospitalPage(){
        return adminHospitalRepository.getRegisteredHospitalPage();
    }

    // 메인 페이지에서 병원수 가져옴.
    public int getHospitalCnt(){
        return adminRepository.getHospitalCnt();
    }

    public int getMemberCnt() {
        return adminRepository.getMemberCnt();
    }

}
