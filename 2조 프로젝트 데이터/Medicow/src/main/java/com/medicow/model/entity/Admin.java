package com.medicow.model.entity;

import com.medicow.model.constant.Role;
import com.medicow.model.dto.AdminFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "admin")
@Getter @Setter @ToString
public class Admin {
    @Id
    @Column(name = "admin_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // 테스트를 위한 메소드
    public static Admin createAdmin(AdminFormDto adminFormDto, PasswordEncoder passwordEncoder){
        Admin admin = new Admin();

        admin.setName(adminFormDto.getName());
        admin.setEmail(adminFormDto.getEmail());

        String password = passwordEncoder.encode(adminFormDto.getPassword()) ;
        admin.setPassword(password);
        admin.setRole(Role.ADMIN);

        return admin;
    }
}
