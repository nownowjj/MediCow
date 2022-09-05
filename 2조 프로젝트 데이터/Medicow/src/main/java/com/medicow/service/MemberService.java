package com.medicow.service;

import com.medicow.model.dto.MemberFormDto;
import com.medicow.model.entity.Member;
import com.medicow.repository.inter.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor // final이나 @NotNull이 붙어 있는 변수에 생성자를 만들어 줍니다.
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository ;

    public Member saveMember(Member member){
        validateDuplicateMember(member) ;
        return memberRepository.save(member) ;
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail()) ;

        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.") ;
        }
    }

    @Override // 이메일을 이용하여 회원 정보를 찾기
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email) ;
        if(member == null){ // 회원이 존재하지 않는 경우
            throw new UsernameNotFoundException(email) ;
        }
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    public Member getMember(String mEmail) {
        Member member = memberRepository.findByEmail(mEmail);
        return member;
    }

    public MemberFormDto memberInfo(String memberEmail) {
        Member member = memberRepository.findByEmail(memberEmail);
        System.out.println("=========================== MemberService");
        System.out.println("member.getId() : " + member.getId());
        System.out.println("member.getEmail() : " + member.getEmail());

        MemberFormDto memberInfo = new MemberFormDto();

        memberInfo.setId(member.getId());
        memberInfo.setAddress(member.getAddress());
        memberInfo.setAge(member.getAge().toString());
        memberInfo.setEmail(member.getEmail());
        memberInfo.setGender(member.getGender());
        memberInfo.setEmail(member.getEmail());
        memberInfo.setName(member.getName());
        memberInfo.setPhone(member.getPhone());
        memberInfo.setRegTime(member.getRegTime());
        memberInfo.setUser_x(member.getUser_x());
        memberInfo.setUser_y(member.getUser_y());

        return memberInfo;
    }

    public Member memberPwInfo(String email) {
        Member member = memberRepository.findByEmail(email);
        return member;
    }

    // 마이페이지에서 내 정보 수정 시
    public void updateMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) throws Exception {
        System.out.println("======MemberService 에서 memberFormDto.get~~");
        System.out.println("======================= MemberService " + memberFormDto.getEmail());
        System.out.println("======================= MemberService " + memberFormDto.getAddress());
        System.out.println("======================= MemberService " + memberFormDto.getPassword());

        Member member = memberRepository.findByEmail(memberFormDto.getEmail());
        member.updateMember(memberFormDto,passwordEncoder);
    }
}


