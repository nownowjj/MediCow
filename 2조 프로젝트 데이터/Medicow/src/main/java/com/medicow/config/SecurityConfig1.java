package com.medicow.config;


import com.medicow.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// WebSecurityConfigurerAdapter

@Configuration//설정 파일
@Order(1)
@EnableWebSecurity // WebSecurityConfigurerAdapter 과 짝꿍
public class SecurityConfig1 extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("여긴 세큐러티, 보안설정중");
        http.requestMatchers().antMatchers("/hospital/**");

        http.formLogin()
                .loginPage("/hospital/members/login")
                .defaultSuccessUrl("/hospital/main")
                .usernameParameter("hosId")
                .passwordParameter("hosPassword")
                .failureUrl("/hospital/members/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/hospital/members/logout"))
                .logoutSuccessUrl("/hospital/members/front");

        System.out.println("여긴 세큐러티, 보안설정중");
        http.authorizeRequests()
                .mvcMatchers("/hospital/members/**", "/user/boards/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();

        System.out.println("여긴 세큐러티, 보안설정중");
        http.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());
    }
    @Autowired
    HospitalService hospitalService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(hospitalService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**","/js/**","/img/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
