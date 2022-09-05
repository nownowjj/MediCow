package com.medicow.model.entity;


import com.medicow.model.constant.Role;
import com.medicow.model.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter @Setter
public class Member extends BaseEntity{
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String email; //

    private String name ;

    private String password ;

    private String address ; //

    @Enumerated(EnumType.STRING)
    private Role role ; // 일반인, 병원관리자 ,관리자 구분

    private String gender; // radio-button

    private String phone;

    private String age; //

    @Column(nullable = false ,precision = 10)
    private double user_x;

    @Column(nullable = false ,precision = 10)
    private double user_y;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){ // PasswordEncoder 추후 설정
        Member member = new Member();

        member.setName(memberFormDto.getName());

        member.setAge(memberFormDto.getAge());
        member.setEmail(memberFormDto.getEmail());

        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setAddress(memberFormDto.getAddress());
        member.setPhone(memberFormDto.getPhone());
        member.setGender(memberFormDto.getGender());
        member.setRole(Role.USER);
        member.setRegTime(LocalDateTime.now());
        member.setUser_x(memberFormDto.getUser_x());
        member.setUser_y(memberFormDto.getUser_y());

        return member;
    }

    public void updateMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        System.out.println("=========================================");
        System.out.println("Member updateMember");
        System.out.println("Member updateMember password : " + memberFormDto.getPassword());
        if(!memberFormDto.getPassword().isEmpty()){
            System.out.println("Member updateMember if 문 실행 왜 되니?");
            String password = passwordEncoder.encode(memberFormDto.getPassword());
            this.password = password;
        }

        this.age = memberFormDto.getAge();
        this.phone = memberFormDto.getPhone();
        this.address = memberFormDto.getAddress();
        this.user_x=memberFormDto.getUser_x();
        this.user_y=memberFormDto.getUser_y();
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Reservation> reservationList = new ArrayList<Reservation>();
}
