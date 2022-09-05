package com.medicow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration // 저(AuditorConfig)는 설정을 위한 클래스 파일 입니다.
@EnableJpaAuditing // JPA 감사용 파일로 사용할 것입니다.
public class AuditConfig {
    @Bean // 스프링이 저를 객체로 생성해줄 겁니다.
    public AuditorAware<String> auditorProvider(){

        return new AuditorAwareImpl();
    }

}
