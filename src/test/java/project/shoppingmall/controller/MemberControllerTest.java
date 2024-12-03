package project.shoppingmall.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import project.shoppingmall.dto.MemberFormDto;
import project.shoppingmall.entity.Member;
import project.shoppingmall.service.MemberService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@AutoConfigureMockMvc //MockMVC 테스트를 위해 선언
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberControllerTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;
    //MockMvc 클래스를 이용해 실제 객체와 비슷하지만 테스트에 필요한 기능만 가지는 가짜 객체
    //MockMvc 객체를 이용하면 웹브라우저에서 요청을 하는 것처럼 테스트 할 수 있습니다.

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(String email, String password) { //로그인 예제 진행을 위해 로그인 전 회원을 등록하는 메서드
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword(password);
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        return memberService.saveMember(member);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception {
        String email = "test@email.com";
        String password = "1234";
        this.createMember(email, password); //회원가입 메서드 실행

        //가입된 회원 정보로 로그인 되는지 테스트
        mockMvc.perform(formLogin().userParameter("email") //userParameter()를 이용하여 이메일을 아이디로 세팅하고 로그인 URL에 요청
                .loginProcessingUrl("/members/login")
                .user(email).password(password))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    //회원가입은 정상적으로 진행했는데, 비밀번호를 잘못 입력하여 인증되지 않은 결과값이 나오는 테스트코드
    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception {
        String email = "test@email.com";
        String password = "1234";
        this.createMember(email, password); //회원가입 메서드 실행
        mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/members/login")
                .user(email).password("12345"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated()); //인증되지 않은 결과값이 나올거임 -> 테스트 성공
    }

}